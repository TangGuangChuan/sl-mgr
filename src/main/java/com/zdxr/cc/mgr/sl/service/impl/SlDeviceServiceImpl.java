package com.zdxr.cc.mgr.sl.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.io.Files;
import com.zdxr.cc.mgr.sl.common.BeanCopyUtil;
import com.zdxr.cc.mgr.sl.common.BizError;
import com.zdxr.cc.mgr.sl.common.ZipUtils;
import com.zdxr.cc.mgr.sl.data.*;
import com.zdxr.cc.mgr.sl.entity.*;
import com.zdxr.cc.mgr.sl.enums.YNStatusEnum;
import com.zdxr.cc.mgr.sl.enums.base.ModelStatusEnum;
import com.zdxr.cc.mgr.sl.mapper.*;
import com.zdxr.cc.mgr.sl.service.ISlDeviceService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 设备型号批次
 */
@Service
public class SlDeviceServiceImpl implements ISlDeviceService {
    private final static Logger logger = LoggerFactory.getLogger(SlDeviceServiceImpl.class);
    @Autowired
    private SlDeviceMapper deviceMapper;
    @Autowired
    private SlDeviceProjectConfigMapper projectConfigMapper;
    @Autowired
    private SlDeviceDocConfigMapper docConfigMapper;
    @Autowired
    private SlDeviceNoMapper deviceNoMapper;
    @Autowired
    private SlDeviceDataItemRecordMapper deviceDataItemRecordMapper;
    @Autowired
    private SlDeviceProjectRecordMapper deviceProjectRecordMapper;
    @Autowired
    private SlDeviceDocRecordMapper deviceDocRecordMapper;
    @Autowired
    private SlAttachMapper attachMapper;

    /**
     * 新增
     *
     * @param voData 批次数据
     */
    @Override
    @Transactional
    public void insert(SlDeviceInfoVoData voData) {
        SlDevice model = new SlDevice();
        BeanCopyUtil.copy(voData, model);
        model.setStatus(ModelStatusEnum.DOING);
        model.setSendStatus(YNStatusEnum.N);
        model.setCompUpload(YNStatusEnum.N);
        deviceMapper.insert(model);

        List<SlDeviceNo> deviceNoList = new ArrayList<>();
        for (int i = 1; i <= voData.getDeviceNum(); i++) {
            SlDeviceNo deviceNo = new SlDeviceNo();
            deviceNo.setDeviceId(model.getId());
            deviceNo.setDeviceNo(i + "号");
            deviceNoList.add(deviceNo);
        }
        if (deviceNoList.size() > 0) {
            deviceNoMapper.insertBatch(deviceNoList);
        }
    }

    @Override
    @Transactional
    public void update(SlDeviceInfoVoData voData) {
        SlDevice model = deviceMapper.selectById(voData.getId());
        if (model == null) {
            throw new BizError("当前型号批次不存在");
        }
        List<SlDeviceProjectConfig> projectConfigList = projectConfigMapper.selectByDeviceId(voData.getId());
        if (projectConfigList != null && projectConfigList.size() > 0) {
            throw new BizError("型号批次已设置,不能修改");
        }
        List<SlDeviceDocConfig> docConfigList = docConfigMapper.selectBydeviceId(voData.getId());
        if (docConfigList != null && docConfigList.size() > 0) {
            throw new BizError("型号批次已设置,不能修改");
        }
        if (voData.getDeviceNum() != model.getDeviceNum()) {
            deviceNoMapper.deleteByDeviceId(voData.getId());
            List<SlDeviceNo> deviceNoList = new ArrayList<>();
            for (int i = 1; i <= voData.getDeviceNum(); i++) {
                SlDeviceNo deviceNo = new SlDeviceNo();
                deviceNo.setDeviceId(voData.getId());
                deviceNo.setDeviceNo(i + "号");
                deviceNoList.add(deviceNo);
            }
            if (deviceNoList.size() > 0) {
                deviceNoMapper.insertBatch(deviceNoList);
            }
        }
        BeanCopyUtil.copy(voData, model);
        deviceMapper.updateById(model);
    }

    /**
     * 删除
     *
     * @param id 任务ID
     */
    @Override
    @Transactional
    public void deleteById(Integer id) {
        deviceMapper.deleteById(id);
    }

    /**
     * 查询编号
     *
     * @param deviceId 任务ID
     * @return
     */
    @Override
    public List<SlDeviceNoVoData> selectDeviceNoList(Integer deviceId) {
        List<SlDeviceNo> list = deviceNoMapper.findByDeviceId(deviceId);
        return BeanCopyUtil.copyList(list, SlDeviceNoVoData.class);
    }

    @Override
    public void updateDeviceNo(SlDeviceNoUpdateVoData voData) {
        if (voData.getUpdateList() != null && voData.getUpdateList().size() > 0) {
            for (SlDeviceNoVoData deviceNoVoData : voData.getUpdateList()) {
                SlDeviceNo slDeviceNo = deviceNoMapper.selectById(deviceNoVoData.getId());
                slDeviceNo.setDeviceNo(deviceNoVoData.getDeviceNo());
                try {
                    deviceNoMapper.updateById(slDeviceNo);
                } catch (Exception e) {
                    if (e.getMessage().contains("sl_device_no_index")) {
                        throw new BizError("出厂编号重复");
                    } else {
                        throw new BizError(e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * 首页总览
     *
     * @param status   状态
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @return
     */
    @Override
    public ModelInvoVoData selectList(ModelStatusEnum status, String keyWords, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SlDeviceVoData> list = deviceMapper.selectList(status, keyWords);
        ModelInvoVoData voData = new ModelInvoVoData();
        voData.setList(new PageInfo<>(list));
        List<SlDeviceVoData> allList = deviceMapper.selectCount(null, keyWords);
        int doing = 0;
        int comp = 0;
        for (SlDeviceVoData vo : allList) {
            if (vo.getStatus() == ModelStatusEnum.COMPLETE) {
                comp++;
            } else {
                doing++;
            }
        }
        voData.setAllNum(allList.size());
        voData.setDoingNum(doing);
        voData.setCompNum(comp);
        return voData;
    }


    @Override
    public void reportZip(Integer deviceId, HttpServletResponse response) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String bs = "|";
        String basePath = File.separator + "sl" + File.separator + "zip" + File.separator;
        String dirPath = System.getProperty("user.dir") + basePath;
        File directory = new File(dirPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new BizError(String.format("创建目录%s失败", dirPath));
            }
        }
        SlDevice device = deviceMapper.selectById(deviceId);

        String devicePath = dirPath + device.getDeviceCode() + File.separator;
        File deviceDir = new File(devicePath);
        if (!deviceDir.exists()) {
            if (!deviceDir.mkdirs()) {
                throw new BizError(String.format("创建目录%s失败", dirPath));
            }
        }
        try {
            List<String> attachIds = new ArrayList<>();

            //单机
            String deviceTab = devicePath + "slDevice";
            File deviceTabFile = new File(deviceTab);
            if (deviceTabFile.exists()) {
                deviceTabFile.delete();
            }
            deviceTabFile.createNewFile();
            FileWriter deviceTabWriter = new FileWriter(deviceTabFile);
            StringBuffer deviceTabsb = new StringBuffer();
            deviceTabsb.append(device.getId()).append(bs).append(device.getModelName()).append(bs).append(device.getModelDesc()).append(bs)
                    .append(device.getClassify()).append(bs).append(device.getDeviceCode()).append(bs).append(device.getDeviceName()).append(bs)
                    .append(device.getDeviceNum()).append(bs).append(device.getSysName()).append(bs).append(device.getSysSubName()).append(bs)
                    .append(device.getSendStatus()).append(bs).append(device.getStatus()).append(bs).append(device.getCompUpload());
            deviceTabWriter.write(deviceTabsb.toString());
            deviceTabWriter.flush();
            deviceTabWriter.close();
            //编号
            String noTab = devicePath + "slDeviceNo";
            File noTabFile = new File(noTab);
            if (noTabFile.exists()) {
                noTabFile.delete();
            }
            noTabFile.createNewFile();
            FileWriter noTabWriter = new FileWriter(noTabFile);
            List<SlDeviceNo> deviceNoList = deviceNoMapper.findByDeviceId(deviceId);
            for (SlDeviceNo no : deviceNoList) {
                StringBuilder noSb = new StringBuilder();
                noSb.append(no.getId()).append(bs).append(no.getDeviceId()).append(bs).append(no.getDeviceNo()).append("\r\n");
                noTabWriter.write(noSb.toString());
            }
            noTabWriter.flush();
            noTabWriter.close();
            //item
            File itemFile = new File(devicePath + "SlDeviceDataItemRecord");
            if (itemFile.exists()) {
                itemFile.delete();
            }
            itemFile.createNewFile();
            FileWriter itemWriter = new FileWriter(itemFile);
            List<SlDeviceDataItemRecord> itemRecordList = deviceDataItemRecordMapper.findByDeviceId(deviceId);
            for (SlDeviceDataItemRecord record : itemRecordList) {
                StringBuilder sb = new StringBuilder();
                sb.append(record.getId()).append(bs).append(record.getDeviceId()).append(bs).append(record.getRecordId()).append(bs)
                        .append(record.getDataItem()).append(bs).append(record.getDataUnit()).append(bs).append(record.getDataVal()).append("\r\n");
                itemWriter.write(sb.toString());
            }
            itemWriter.flush();
            itemWriter.close();
            //项目记录
            File projectFile = new File(devicePath + "slDeviceProjectRecord");
            if (projectFile.exists()) {
                projectFile.delete();
            }
            projectFile.createNewFile();
            FileWriter projectWriter = new FileWriter(projectFile);
            List<SlDeviceProjectRecord> projectRecordList = deviceProjectRecordMapper.findByDeviceId(deviceId);
            for (SlDeviceProjectRecord record : projectRecordList) {
                if (StringUtils.isNotBlank(record.getAttachIds())) {
                    attachIds.addAll(Arrays.asList(record.getAttachIds().split(",")));
                }
                if (StringUtils.isNotBlank(record.getMediaIds())) {
                    attachIds.addAll(Arrays.asList(record.getMediaIds().split(",")));
                }
                if (StringUtils.isNotBlank(record.getSignId())) {
                    attachIds.add(record.getSignId());
                }
                StringBuilder sb = new StringBuilder();
                sb.append(record.getId()).append(bs).append(record.getDeviceId()).append(bs).append(record.getModelName()).append(bs)
                        .append(record.getModelDesc()).append(bs).append(record.getClassify()).append(bs).append(record.getSysName()).append(bs)
                        .append(record.getSysSubName()).append(bs).append(record.getDeviceCode()).append(bs).append(record.getDeviceName()).append(bs)
                        .append(record.getDeviceNum()).append(bs).append(record.getModelState()).append(bs).append(record.getDeviceNo()).append(bs)
                        .append(convert(record.getExpProject())).append(bs).append(convert(record.getExpCondition())).append(bs).append(convert(record.getTestProject())).append(bs)
                        .append(convert(record.getTestMethod())).append(bs).append(convert(record.getTestJudge())).append(bs).append(record.getAttach()).append(bs)
                        .append(record.getAttachIds()).append(bs).append(record.getData()).append(bs).append(convert(record.getDataTxt())).append(bs)
                        .append(record.getRecord()).append(bs).append(convert(record.getRecordTxt())).append(bs).append(record.getMedia()).append(bs)
                        .append(record.getMediaIds()).append(bs).append(record.getQualified()).append(bs).append(record.getSignName()).append(bs)
                        .append(record.getSignId()).append(bs).append(record.getSignDate() == null ? "" : df.format(record.getSignDate())).append(bs).append(record.getClientCode()).append(bs)
                        .append(record.getOperatorClient()).append(bs).append(record.getSendStatus()).append(bs).append(record.getSendDate() == null ? null : df.format(record.getSendDate())).append("\r\n");
                projectWriter.write(sb.toString());
            }
            projectWriter.flush();
            projectWriter.close();
            //文档
            File docFile = new File(devicePath + "SlDeviceDocRecord");
            if (docFile.exists()) {
                docFile.delete();
            }
            docFile.createNewFile();
            FileWriter docWriter = new FileWriter(docFile);
            List<SlDeviceDocRecord> docRecordList = deviceDocRecordMapper.findByDeviceId(deviceId);
            for (SlDeviceDocRecord record : docRecordList) {
                if (StringUtils.isNotBlank(record.getAttachIds())) {
                    attachIds.addAll(Arrays.asList(record.getAttachIds().split(",")));
                }
                if (StringUtils.isNotBlank(record.getMediaIds())) {
                    attachIds.addAll(Arrays.asList(record.getMediaIds().split(",")));
                }
                if (StringUtils.isNotBlank(record.getSignId())) {
                    attachIds.add(record.getSignId());
                }
                StringBuilder sb = new StringBuilder();
                sb.append(record.getId()).append(bs).append(record.getDeviceId()).append(bs).append(record.getModelName()).append(bs).append(record.getModelDesc()).append(bs)
                        .append(record.getClassify()).append(bs).append(record.getDeviceCode()).append(bs).append(record.getDeviceName()).append(bs)
                        .append(record.getDeviceNum()).append(bs).append(convert(record.getCheckProject())).append(bs).append(convert(record.getCheckMethod())).append(bs)
                        .append(convert(record.getTestJudge())).append(bs).append(record.getAttach()).append(bs).append(record.getAttachIds()).append(bs)
                        .append(record.getMedia()).append(bs).append(record.getMediaIds()).append(bs).append(record.getQualified()).append(bs)
                        .append(record.getSignName()).append(bs).append(record.getSignId()).append(bs).append(record.getSignDate() == null ? "" : df.format(record.getSignDate())).append(bs)
                        .append(record.getClientCode()).append(bs).append(record.getOperatorClient()).append(bs).append(record.getSendStatus()).append(bs)
                        .append(record.getSendDate() == null ? null : df.format(record.getSendDate())).append("\r\n");
                docWriter.write(sb.toString());
            }
            docWriter.flush();
            docWriter.close();
            //附件
            File attachFile = new File(devicePath + "slAttach");
            if (attachFile.exists()) {
                attachFile.delete();
            }
            attachFile.createNewFile();
            FileWriter attachWriter = new FileWriter(attachFile);
            if (attachIds.size() > 0) {
                String attachPath = devicePath + File.separator + "attach" + File.separator;
                String attachIdsStr = "'" + StringUtils.join(attachIds, "','") + "'";
                File attachDir = new File(attachPath);
                if (!attachDir.exists()) {
                    attachDir.mkdirs();
                }
                List<SlAttach> attachList = attachMapper.selectByAttachIds(attachIdsStr);
                for (SlAttach attach : attachList) {
                    try {
                        File sourceFile = new File(attach.getFileUrl());
                        File toFile = new File(attachPath + attach.getFileName());
                        if (!toFile.exists()) {
                            logger.info("toFile:{}",toFile.getName());
                            Files.copy(sourceFile, toFile);
                            logger.info("图片拷贝:source{},to{}", sourceFile.getPath(), toFile.getPath());
                            logger.info("图片拷贝名称:{}", toFile.getName());
                        }
                    } catch (Exception e) {
                        logger.error("文件拷贝:" + e.getMessage());
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(attach.getId()).append(bs).append(attach.getAttachId()).append(bs).append(attach.getFileName()).append(bs)
                            .append(attach.getSuffix()).append(bs).append(attach.getFileUrl()).append(bs).append(attach.getOperatorName()).append(bs)
                            .append(attach.getOperatorTime() == null ? null : df.format(attach.getOperatorTime())).append("\r\n");
                    attachWriter.write(sb.toString());
                }
                attachWriter.flush();
            }
            attachWriter.close();

            File zipFile = new File(dirPath + device.getDeviceCode() + ".zip");
            if (zipFile.exists()) {
                zipFile.delete();
            }
            zipFile.createNewFile();
            ZipUtils.zipFiles(devicePath, dirPath + device.getDeviceCode() + ".zip");
            logger.info(devicePath);
            logger.info(dirPath);
            response.setHeader("Content-Disposition", "attachment;Filename=" + URLEncoder.encode(device.getDeviceCode() + ".zip", "UTF-8"));
            OutputStream os = response.getOutputStream();
            byte[] b = new byte[100];
            int len;
            FileInputStream fileInputStream = new FileInputStream(zipFile);
            while ((len = fileInputStream.read(b)) > 0) {
                os.write(b, 0, len);
            }
            fileInputStream.close();
            ZipUtils.deleteDir(new File(devicePath));
            zipFile.delete();
        } catch (Exception e) {
            throw new BizError(e.getMessage());
        }
    }

    private String convert(String str) {
        if (StringUtils.isNotBlank(str)) {
            return str.replaceAll("\t", "^t").replaceAll("\r", "^r").replaceAll("\n", "^n");
        } else {
            return str;
        }
    }
}
