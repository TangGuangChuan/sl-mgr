package com.zdxr.cc.mgr.sl.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.zdxr.cc.mgr.sl.common.BeanCopyUtil;
import com.zdxr.cc.mgr.sl.common.BizError;
import com.zdxr.cc.mgr.sl.data.*;
import com.zdxr.cc.mgr.sl.entity.*;
import com.zdxr.cc.mgr.sl.enums.GroupStatusEnum;
import com.zdxr.cc.mgr.sl.enums.ModelStateEnum;
import com.zdxr.cc.mgr.sl.enums.YNStatusEnum;
import com.zdxr.cc.mgr.sl.enums.base.ModelStatusEnum;
import com.zdxr.cc.mgr.sl.mapper.*;
import com.zdxr.cc.mgr.sl.service.ISlDeviceSendGroupService;
import com.zdxr.cc.mgr.sl.tcp.NettyServer;
import com.zdxr.cc.mgr.sl.tcp.bean.ClientInfo;
import com.zdxr.cc.mgr.sl.tcp.bean.Message;
import com.zdxr.cc.mgr.sl.tcp.bean.type.MsgType;
import com.zdxr.cc.mgr.sl.tcp.util.SeqNoUtil;
import io.netty.channel.socket.SocketChannel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SlDeviceSendGroupServiceImpl implements ISlDeviceSendGroupService {
    private final String ATTACH_PATH = File.separator + "sl" + File.separator + "attach" + File.separator;
    private static final Logger log = LoggerFactory.getLogger(SlDeviceSendGroupServiceImpl.class);
    @Autowired
    private SlDeviceSendGroupMapper sendGroupMapper;
    @Autowired
    private SlDeviceMapper deviceMapper;
    @Autowired
    private SlDeviceProjectRecordMapper projectRecordMapper;
    @Autowired
    private SlDeviceDocRecordMapper docRecordMapper;
    @Autowired
    private NettyServer nettyServer;
    @Autowired
    private SlAttachMapper attachMapper;
    @Autowired
    private SlDeviceClientMapper clientMapper;
    @Autowired
    private SlDeviceDocConfigMapper deviceDocConfigMapper;
    @Autowired
    private SlDeviceProjectConfigMapper deviceProjectConfigMapper;
    @Autowired
    private SlDeviceDataItemRecordMapper itemRecordMapper;
    @Autowired
    private SlDeviceNoMapper deviceNoMapper;

    @Override
    @Transactional
    public void insert(SlDeviceSendGroupInsertVoData voData) {
        if (voData.getModelState() == null || StringUtils.isBlank(voData.getModelState())) {
            voData.setModelState("ZPHJ,ZJCS,YSSY,LXSY,DOC");
        }
        SlDeviceSendGroup group = new SlDeviceSendGroup();
        BeanCopyUtil.copy(voData, group);
        group.setSendStatus(YNStatusEnum.N);
        group.setStatus(GroupStatusEnum.NOT);
        SlDeviceClient client = clientMapper.findByName(group.getOperatorClient());
        if (client == null) {
            throw new BizError("未查询到操作端,操作端名称可能已更新,请刷新页面");
        }
        group.setClientCode(client.getClientCode());
        check(group, false);
        sendGroupMapper.insert(group);
    }

    @Override
    @Transactional
    public void update(SlDeviceSendGroupUpdateVoData voData) {
        if (voData.getModelState() == null || StringUtils.isBlank(voData.getModelState())) {
            voData.setModelState("ZPHJ,ZJCS,YSSY,LXSY,DOC");
        }
        SlDeviceSendGroup group = sendGroupMapper.selectById(voData.getId());
        if (group == null) {
            throw new BizError("未查询到分组");
        }
        if (group.getSendStatus() == YNStatusEnum.Y) {
            throw new BizError("数据已下发,不能修改数据");
        }
        BeanCopyUtil.copy(voData, group);
        SlDeviceClient client = clientMapper.findByName(group.getOperatorClient());
        if (client == null) {
            throw new BizError("未查询到操作端,操作端名称可能已更新,请刷新页面");
        }
        group.setClientCode(client.getClientCode());
        check(group, true);
        sendGroupMapper.updateById(group);
    }

    @Override
    public void deleteById(Integer id) {
        SlDeviceSendGroup group = sendGroupMapper.selectById(id);
        if (group == null) {
            throw new BizError("未查询到分组");
        }
        if (group.getSendStatus() == YNStatusEnum.Y) {
            throw new BizError("数据已下发,不能删除");
        }
        sendGroupMapper.deleteById(id);
    }

    @Override
    public Boolean deleteSendDataById(SendGroupDeleteVoData voData) {
        SlDeviceSendGroup group = sendGroupMapper.selectById(voData.getGroupId());
        if (group == null) {
            throw new BizError("未查询到下发分组");
        }
        if (group.getSendStatus() != YNStatusEnum.Y) {
            throw new BizError("未下发,不需要删除下发数据");
        }
        SocketChannel channel = null;
        for (Integer hashCode : nettyServer.getConnCacheMap().keySet()) {
            ClientInfo clientInfo = nettyServer.getConnCacheMap().get(hashCode);
            if (StringUtils.isNotBlank(clientInfo.getClientCode()) && clientInfo.getClientCode().equals(group.getClientCode())) {
                channel = clientInfo.getChannel();
            }
        }
        if (channel == null || !channel.isActive()) {
            throw new BizError("操作端未连接,删除下发数据失败");
        }
        if (!voData.getIsConfirm()) {
            String[] modelStates = group.getModelState().split(",");
            for (String modelState : modelStates) {
                if (ModelStateEnum.DOC.code().equals(modelState)) {
                    int docCdount = docRecordMapper.selectDataCountByDeviceId(group.getDeviceId(), group.getClientCode());
                    if (docCdount > 0) {
                        return true;
                    }
                } else {
                    String deviceNoStr = "'" + String.join("','", group.getDeviceNo().split(",")) + "'";
                    List<SlDeviceProjectRecord> recordList = projectRecordMapper.selectByYAndState(group.getDeviceId(), modelState, deviceNoStr, group.getClientCode());
                    if (recordList.size() > 0) {
                        return true;
                    }
                }
            }
        }
        try {
            DeleteDataVoData deleteDataVoData = new DeleteDataVoData();
            deleteDataVoData.setGroupId(group.getId());
            deleteDataVoData.setDeviceId(group.getDeviceId());
            deleteDataVoData.setDeviceNos(group.getDeviceNo());
            deleteDataVoData.setModelStates(group.getModelState());
            Gson son = new Gson();
            Message msg = new Message();
            msg.setType(MsgType.ROBBINGORDER);
            msg.setSeqNo(SeqNoUtil.getSeqNo());
            msg.setContent(son.toJson(deleteDataVoData));
            log.info("删除下发数据日志：{}", msg);
            channel.writeAndFlush(msg);
        } catch (Exception e) {
            throw new BizError("删除下发数据:" + group.getOperatorClient() + "异常,请保证操作端已连接上后重试");
        }
        TaskSendLock.isOver("group" + group.getId(), 1000 * 120);
        return false;
    }

    @Override
    public SlDeviceSendGroupInfoVoData selectList(Integer deviceId) {
        SlDevice device = deviceMapper.selectById(deviceId);
        if (device == null) {
            throw new BizError("未查询到设备");
        }
        SlDeviceSendGroupInfoVoData voData = new SlDeviceSendGroupInfoVoData();
        voData.setDeviceId(deviceId);
        voData.setDeviceName(device.getDeviceName());
        voData.setModelName(device.getModelName());
        voData.setDeviceNum(device.getDeviceNum());
        List<SlDeviceSendGroup> list = sendGroupMapper.selectList(deviceId);
        if (list != null && list.size() > 0) {
            voData.setCount(list.size());
            voData.setList(BeanCopyUtil.copyList(list, SlDeviceSendGroupVoData.class));
        } else {
            voData.setCount(0);
        }
        if (voData.getList() != null && voData.getList().size() > 0) {
            for (SlDeviceSendGroupVoData sendGroupVoData : voData.getList()) {
                if (StringUtils.isNotBlank(sendGroupVoData.getDeviceNo())) {
                    String[] deviceNos = sendGroupVoData.getDeviceNo().split(",");
                    List<String> deviceNoList = Arrays.asList(deviceNos);
                    String deviceNoStr = "'"+deviceNoList.stream().collect(Collectors.joining("','")) + "'";
                    List<SlDeviceNo> slDeviceNoList = deviceNoMapper.findByDeviceIdAndDeviceNo(sendGroupVoData.getDeviceId(), deviceNoStr);
                    String deviceNoStrVo = slDeviceNoList.stream().map(s -> s.getDeviceNo()).collect(Collectors.joining(","));
                    sendGroupVoData.setDeviceNo(deviceNoStrVo);
                }
            }
        }
        return voData;
    }

    @Override
    public void sendGroup(String groupIds) {
        Map<String, SocketChannel> map = new HashMap<>();
        List<String> clientList = new ArrayList<>();
        for (Integer hashCode : nettyServer.getConnCacheMap().keySet()) {
            ClientInfo clientInfo = nettyServer.getConnCacheMap().get(hashCode);
            if (StringUtils.isNotBlank(clientInfo.getClientCode())) {
                clientList.add(clientInfo.getClientCode());
                map.put(clientInfo.getClientCode(), clientInfo.getChannel());
            }
        }
        String clientStr = "'" + String.join("','", clientList) + "'";
        List<SlDeviceSendGroup> list = sendGroupMapper.selectListByDeviceId(groupIds, clientStr);
        if (list.size() == 0) {
            throw new BizError("下发失败,没有需要下发的任务");
        }
        Integer deviceId = list.get(0).getDeviceId();
        for (SlDeviceSendGroup group : list) {
            List<SlDeviceDocRecord> docRecordList = null;
            List<SlDeviceProjectRecord> tmpRecordList = new ArrayList<>();
            String[] modelStates = group.getModelState().split(",");
            for (String modelState : modelStates) {
                if (ModelStateEnum.DOC.code().equals(modelState)) {
                    docRecordList = docRecordMapper.selectNoSendList(deviceId);
                } else {
                    String deviceNoStr = "'" + String.join("','", group.getDeviceNo().split(",")) + "'";
                    List<SlDeviceProjectRecord> recordList = projectRecordMapper.selectByNoAndState(deviceId, modelState, deviceNoStr);
                    for (SlDeviceProjectRecord record : recordList) {
                        if (record.getData() == YNStatusEnum.Y) {
                            List<SlDeviceDataItemRecord> records = itemRecordMapper.selectByRecordId(record.getId());
                            record.setItemRecordList(records);
                        }
                    }
                    tmpRecordList.addAll(recordList);
                }
            }
            if ((docRecordList == null || docRecordList.size() == 0) && (tmpRecordList == null || tmpRecordList.size() == 0)) {
                throw new BizError("下发失败,没有需要下发的数据");
            }
            SlSendMsgVoData voData = new SlSendMsgVoData();
            voData.setGroupId(group.getId());
            voData.setList(tmpRecordList);
            voData.setDocList(docRecordList);
            Gson son = new Gson();
            SocketChannel channel = map.get(group.getClientCode());
            if (channel != null && channel.isActive()) {
                try {
                    Message msg = new Message();
                    msg.setType(MsgType.PUSH);
                    msg.setSeqNo(SeqNoUtil.getSeqNo());
                    msg.setContent(son.toJson(voData));
                    log.info("下发日志：{}", msg);
                    channel.writeAndFlush(msg);
                } catch (Exception e) {
                    throw new BizError("下发到操作端:" + group.getOperatorClient() + "异常,请保证操作端已连接上后重试");
                }
            }
        }
    }

    @Override
    @Transactional
    public void clientUpload(MultipartFile[] files, SlClientUploadVoData voData) {
        log.info("返回数据voData:" + JSONObject.toJSON(voData));
        this.doUploadFile(files);
        if (voData.getAttachs() != null && voData.getAttachs().size() > 0) {
            List<SlAttach> attachList = new ArrayList<>();
            for (SlAttachVoData attachVoData : voData.getAttachs()) {
                SlAttach slAttach = attachMapper.selectByAttachId(attachVoData.getAttachId());
                if (slAttach == null) {
                    slAttach = new SlAttach();
                    BeanCopyUtil.copy(attachVoData, slAttach);
                    String dirPath = System.getProperty("user.dir") + ATTACH_PATH;
                    slAttach.setFileUrl(dirPath + slAttach.getAttachId() + "." + slAttach.getSuffix());
                    attachList.add(slAttach);
                }
            }
            if (attachList.size() > 0) {
                attachMapper.insertBatch(attachList);
            }
        }
        Set<Integer> set = new HashSet<>();
        if (voData.getProjectRecords() != null && voData.getProjectRecords().size() > 0) {
            for (SlDeviceProjectRecordVoData recordVoData : voData.getProjectRecords()) {
                SlDeviceProjectRecord record = projectRecordMapper.selectById(recordVoData.getId());
                if (record != null) {
                    record.setAttachIds(recordVoData.getAttachIds());
                    record.setDataTxt(recordVoData.getDataTxt());
                    record.setRecordTxt(recordVoData.getRecordTxt());
                    record.setMediaIds(recordVoData.getMediaIds());
                    record.setQualified(recordVoData.getQualified());
                    record.setSignName(recordVoData.getSignName());
                    record.setSignId(recordVoData.getSignId());
                    record.setSignDate(recordVoData.getSignDate());
                    projectRecordMapper.updateById(record);
                    if (recordVoData.getItemRecordList() != null && recordVoData.getItemRecordList().size() > 0) {
                        for (SlDeviceDataItemRecordVoData itemRecordVoData : recordVoData.getItemRecordList()) {
                            SlDeviceDataItemRecord itemRecord = itemRecordMapper.selectById(itemRecordVoData.getId());
                            if (itemRecord != null) {
                                itemRecord.setDataVal(itemRecordVoData.getDataVal());
                                itemRecordMapper.updateById(itemRecord);
                            }
                        }
                    }
                    set.add(record.getDeviceId());
                } else {
                    log.info("projectRecordId:{}不存在", recordVoData.getId());
                }
            }
        }
        if (voData.getDocRecords() != null && voData.getDocRecords().size() > 0) {
            for (SlDeviceDocRecordVoData docRecordVoData : voData.getDocRecords()) {
                SlDeviceDocRecord record = docRecordMapper.selectById(docRecordVoData.getId());
                if (record != null) {
                    record.setAttachIds(docRecordVoData.getAttachIds());
                    record.setMediaIds(docRecordVoData.getMediaIds());
                    record.setQualified(docRecordVoData.getQualified());
                    record.setSignName(docRecordVoData.getSignName());
                    record.setSignId(docRecordVoData.getSignId());
                    record.setSignDate(docRecordVoData.getSignDate());
                    docRecordMapper.updateById(record);
                    set.add(record.getDeviceId());
                } else {
                    log.info("docRecordId:{}不存在", docRecordVoData.getId());
                }
            }
        }
        if (set.size() != 0) {
            for (Integer deviceId : set) {
                int projectNotCompCount = projectRecordMapper.selectNotComp(deviceId);
                int docNotCompCount = docRecordMapper.selectNotComp(deviceId);
                SlDevice device = deviceMapper.selectById(deviceId);
                if (projectNotCompCount == 0 && docNotCompCount == 0) {
                    device.setStatus(ModelStatusEnum.COMPLETE);
                }
                device.setCompUpload(YNStatusEnum.Y);
                deviceMapper.updateById(device);
                List<SlDeviceSendGroup> sendGroupList = sendGroupMapper.selectList(deviceId);
                if (sendGroupList != null && sendGroupList.size() > 0) {
                    for (SlDeviceSendGroup sendGroup : sendGroupList) {
                        String[] deviceNoLs = sendGroup.getDeviceNo().split(",");
                        String deviceNos = "'" + String.join("','", Arrays.asList(deviceNoLs)) + "'";
                        String[] modelStates = sendGroup.getModelState().split(",");
                        boolean flag = true;
                        for (String modelState : modelStates) {
                            int cos = 0;
                            if (ModelStateEnum.DOC.equals(modelState)) {
                                cos = docRecordMapper.selectNotGroup(deviceId);
                            } else {
                                cos = projectRecordMapper.selectNotGroup(deviceId, deviceNos, modelState);
                            }
                            if (cos != 0) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            sendGroup.setStatus(GroupStatusEnum.COMP);
                            sendGroupMapper.updateById(sendGroup);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void sendGroupCallBack(Integer groupId) {
        log.info("接收到tcp回调groupId:" + groupId);
        SlDeviceSendGroup group = sendGroupMapper.selectById(groupId);
        if (group == null) {
            log.error("下发tcp回调未查询到groupId:[" + groupId + "]的数据");
        } else {
            Date sendDate = new Date();
            String[] modelStates = group.getModelState().split(",");
            for (String modelState : modelStates) {
                if (ModelStateEnum.DOC.code().equals(modelState)) {
                    docRecordMapper.updateSend(group.getClientCode(), group.getOperatorClient(), sendDate, group.getDeviceId());
                } else {
                    String deviceNoStr = "'" + String.join("','", group.getDeviceNo().split(",")) + "'";
                    projectRecordMapper.updateSend(group.getClientCode(), group.getOperatorClient(), sendDate, group.getDeviceId(), modelState, deviceNoStr);
                }
            }
            SlDevice device = deviceMapper.selectById(group.getDeviceId());
            if (device.getSendStatus() == YNStatusEnum.N) {
                device.setSendStatus(YNStatusEnum.Y);
                deviceMapper.updateById(device);
            }
            group.setSendStatus(YNStatusEnum.Y);
            group.setStatus(GroupStatusEnum.DOING);
            group.setSendDate(sendDate);
            sendGroupMapper.updateById(group);
        }
    }

    @Override
    @Transactional
    public void deleteSendDataCallBack(DeleteDataVoData voData) {
        SlDeviceSendGroup group = sendGroupMapper.selectById(voData.getGroupId());
        String[] modelStates = group.getModelState().split(",");
        for (String modelState : modelStates) {
            if (!ModelStateEnum.DOC.code().equals(modelState)) {
                String deviceNoStr = "'" + String.join("','", group.getDeviceNo().split(",")) + "'";
                List<SlDeviceProjectRecord> recordList = projectRecordMapper.selectByYAndState(group.getDeviceId(), modelState, deviceNoStr, group.getClientCode());
                for (SlDeviceProjectRecord record : recordList) {
                    if (record.getData() == YNStatusEnum.Y) {
                        itemRecordMapper.clearByRecordId(record.getId());
                    }
                }
                projectRecordMapper.clearData(group.getDeviceId(), modelState, deviceNoStr, group.getClientCode());
            } else {
                docRecordMapper.clearData(group.getDeviceId(), group.getClientCode());
            }
        }
        sendGroupMapper.updateSendStatus(group.getId());
        int projectCompCount = projectRecordMapper.selectComp(group.getDeviceId());
        int docCompCount = docRecordMapper.selectComp(group.getDeviceId());
        SlDevice device = deviceMapper.selectById(group.getDeviceId());
        device.setStatus(ModelStatusEnum.DOING);
        if (projectCompCount != 0 || docCompCount != 0) {
            device.setCompUpload(YNStatusEnum.Y);
        } else {
            device.setCompUpload(YNStatusEnum.N);
        }
        int sendCount = sendGroupMapper.selectBySendDeviceId(device.getId());
        if (sendCount == 0) {
            device.setSendStatus(YNStatusEnum.N);
        } else {
            device.setSendStatus(YNStatusEnum.Y);
        }
        deviceMapper.updateById(device);
        sendGroupMapper.deleteById(group.getId());
    }

    @Override
    public List<String> selectOperatorClient() {
        List<String> clientList = new ArrayList<>();
        for (Integer hashCode : nettyServer.getConnCacheMap().keySet()) {
            ClientInfo clientInfo = nettyServer.getConnCacheMap().get(hashCode);
            if (StringUtils.isNotBlank(clientInfo.getClientCode())) {
                SlDeviceClient client = clientMapper.findByCode(clientInfo.getClientCode());
                if (client != null) {
                    clientList.add(client.getClientName());
                }
            }
        }
        return clientList;
    }

    /**
     * 上传文件
     *
     * @param files
     */
    private void doUploadFile(MultipartFile[] files) {
        if (files != null && files.length > 0) {
            String dirPath = System.getProperty("user.dir") + ATTACH_PATH;
            File directory = new File(dirPath);
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    throw new BizError("创建附件目录失败");
                }
            }
            try {
                for (MultipartFile file : files) {
                    String fileName = file.getOriginalFilename();
                    InputStream is = file.getInputStream();
                    String fileUrl = dirPath + fileName;
                    File f = new File(fileUrl);
                    if (!f.exists()) {
                        FileOutputStream fos = new FileOutputStream(fileUrl);
                        byte[] buffer = new byte[1024];
                        while ((is.read(buffer)) != -1) {
                            fos.write(buffer);
                        }
                        fos.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void check(SlDeviceSendGroup group, boolean isUpdate) {
        List<SlDeviceProjectConfig> projectConfigList = deviceProjectConfigMapper.selectByDeviceId(group.getDeviceId());
        List<String> configModelStates = new ArrayList<>();
        if (projectConfigList != null && projectConfigList.size() > 0) {
            configModelStates = projectConfigList.stream().map(s -> s.getModelState().code()).collect(Collectors.toList());
        }
        if (group.getModelState().contains(ModelStateEnum.DOC.code())) {
            List<SlDeviceDocConfig> docConfigList = deviceDocConfigMapper.selectBydeviceId(group.getDeviceId());
            if (docConfigList == null || docConfigList.size() == 0) {
                throw new BizError("提交失败,未设置文档检查");
            }
        }
        String[] groupModelStates = group.getModelState().split(",");
        for (String groupModelState : groupModelStates) {
            if (!groupModelState.equals(ModelStateEnum.DOC.code())) {
                if (!configModelStates.contains(groupModelState)) {
                    throw new BizError("提交失败,项目试验阶段" + ModelStateEnum.valueOf(groupModelState).message() + "未设置");
                }
            }
        }
        boolean hasDoc = false;
        List<SlDeviceSendGroup> sendGroupList = null;
        if (isUpdate) {
            sendGroupList = sendGroupMapper.selectListNotId(group.getDeviceId(), group.getId());
        } else {
            sendGroupList = sendGroupMapper.selectList(group.getDeviceId());
        }
        List<String> checkList = new ArrayList<>();
        if (sendGroupList != null && sendGroupList.size() > 0) {
            for (SlDeviceSendGroup sendGroup : sendGroupList) {
                String[] modelStates = sendGroup.getModelState().split(",");
                String[] deviceNos = sendGroup.getDeviceNo().split(",");
                for (String modelState : modelStates) {
                    if (!ModelStateEnum.DOC.code().equals(modelState)) {
                        for (String deviceNo : deviceNos) {
                            checkList.add(modelState + "_" + deviceNo);
                        }
                    } else {
                        hasDoc = true;
                    }
                }
            }
        }
        String[] modelStates = group.getModelState().split(",");
        String[] deviceNos = group.getDeviceNo().split(",");
        for (String modelState : modelStates) {
            if (!ModelStateEnum.DOC.code().equals(modelState)) {
                for (String deviceNo : deviceNos) {
                    if (checkList.contains(modelState + "_" + deviceNo)) {
                        throw new BizError("提交失败,阶段[" + ModelStateEnum.valueOf(modelState).message() + "],编号[" + deviceNo + "]已存在下发分组");
                    }
                }
            } else {
                if (hasDoc) {
                    throw new BizError("提交失败,已存在文档下发分组");
                }
            }
        }
    }
}
