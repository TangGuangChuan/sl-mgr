package com.zdxr.cc.mgr.sl.service.impl;

import com.github.andyczy.java.excel.ExcelUtils;
import com.zdxr.cc.mgr.sl.common.BeanCopyUtil;
import com.zdxr.cc.mgr.sl.common.BizError;
import com.zdxr.cc.mgr.sl.data.*;
import com.zdxr.cc.mgr.sl.entity.*;
import com.zdxr.cc.mgr.sl.enums.ModelStateEnum;
import com.zdxr.cc.mgr.sl.enums.YNStatusEnum;
import com.zdxr.cc.mgr.sl.mapper.*;
import com.zdxr.cc.mgr.sl.service.ISlDeviceProjectConfigService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 实验项目配置
 */
@Service
public class SlDeviceProjectConfigServiceImpl implements ISlDeviceProjectConfigService {
    @Autowired
    private SlDeviceMapper deviceMapper;
    @Autowired
    private SlDeviceProjectConfigMapper projectConfigMapper;
    @Autowired
    private SlDeviceProjectRecordMapper projectRecordMapper;
    @Autowired
    private SlDeviceDataItemConfigMapper dataItemConfigMapper;
    @Autowired
    private SlDeviceDataItemRecordMapper dataItemRecordMapper;
    @Autowired
    private SlDeviceNoMapper deviceNoMapper;

    /**
     * 试验项目保存
     *
     * @param voData 配置数据
     */
    @Override
    @Transactional
    public void save(SlDeviceProjectConfigSaveVoData voData) {
        SlDevice device = deviceMapper.selectById(voData.getDeviceId());
        if (device == null) {
            throw new BizError("型号批次不存在");
        }
        List<SlDeviceProjectRecord> sendRecordList = projectRecordMapper.selectSend(voData.getDeviceId());
        if (sendRecordList != null && sendRecordList.size() > 0) {
            throw new BizError("已存在下发任务,不能修改配置");
        } else {
            projectRecordMapper.deleteByDeviceId(voData.getDeviceId());
            dataItemConfigMapper.deleteByDeviceId(voData.getDeviceId());
        }
        projectConfigMapper.deleteBydeviceId(voData.getDeviceId());
        List<SlDeviceProjectConfig> list = new ArrayList<>();
        List<SlDeviceDataItemConfig> itemList = new ArrayList<>();
        //装配环节
        if (voData.getZphjList() != null && voData.getZphjList().size() > 0) {
            for (SlDeviceProjectConfigSaveDetailVoData detailVoData : voData.getZphjList()) {
                SlDeviceProjectConfig config = new SlDeviceProjectConfig();
                BeanCopyUtil.copy(detailVoData, config);
                config.setDeviceId(voData.getDeviceId());
                config.setModelName(device.getModelName());
                config.setDeviceName(device.getDeviceName());
                config.setModelState(ModelStateEnum.ZPHJ);
                config.setClassify(device.getClassify());
                projectConfigMapper.insert(config);
                if (config.getData() == YNStatusEnum.Y) {
                    if (detailVoData.getItemList() != null && detailVoData.getItemList().size() > 0) {
                        List<SlDeviceDataItemConfig> itemConfigList = new ArrayList<>();
                        for (SlDeviceDataItemConfigVoData itemConfigVoData : detailVoData.getItemList()) {
                            SlDeviceDataItemConfig itemConfig = new SlDeviceDataItemConfig();
                            BeanCopyUtil.copy(itemConfigVoData, itemConfig);
                            itemConfig.setDeviceId(voData.getDeviceId());
                            itemConfig.setConfigId(config.getId());
                            itemConfigList.add(itemConfig);
                        }
                        config.setItemConfigList(itemConfigList);
                        itemList.addAll(itemConfigList);
                    }
                }
                list.add(config);
            }
        }
        //整机测试
        if (voData.getZjcsList() != null && voData.getZjcsList().size() > 0) {
            for (SlDeviceProjectConfigSaveDetailVoData detailVoData : voData.getZjcsList()) {
                SlDeviceProjectConfig config = new SlDeviceProjectConfig();
                BeanCopyUtil.copy(detailVoData, config);
                config.setDeviceId(voData.getDeviceId());
                config.setModelName(device.getModelName());
                config.setDeviceName(device.getDeviceName());
                config.setModelState(ModelStateEnum.ZJCS);
                config.setClassify(device.getClassify());
                projectConfigMapper.insert(config);
                if (config.getData() == YNStatusEnum.Y) {
                    if (detailVoData.getItemList() != null && detailVoData.getItemList().size() > 0) {
                        List<SlDeviceDataItemConfig> itemConfigList = new ArrayList<>();
                        for (SlDeviceDataItemConfigVoData itemConfigVoData : detailVoData.getItemList()) {
                            SlDeviceDataItemConfig itemConfig = new SlDeviceDataItemConfig();
                            BeanCopyUtil.copy(itemConfigVoData, itemConfig);
                            itemConfig.setDeviceId(voData.getDeviceId());
                            itemConfig.setConfigId(config.getId());
                            itemConfigList.add(itemConfig);
                        }
                        config.setItemConfigList(itemConfigList);
                        itemList.addAll(itemConfigList);
                    }
                }
                list.add(config);
            }
        }
        //验收试验
        if (voData.getYssyList() != null && voData.getYssyList().size() > 0) {
            for (SlDeviceProjectConfigSaveDetailVoData detailVoData : voData.getYssyList()) {
                SlDeviceProjectConfig config = new SlDeviceProjectConfig();
                BeanCopyUtil.copy(detailVoData, config);
                config.setDeviceId(voData.getDeviceId());
                config.setModelName(device.getModelName());
                config.setDeviceName(device.getDeviceName());
                config.setModelState(ModelStateEnum.YSSY);
                config.setClassify(device.getClassify());
                projectConfigMapper.insert(config);
                if (config.getData() == YNStatusEnum.Y) {
                    if (detailVoData.getItemList() != null && detailVoData.getItemList().size() > 0) {
                        List<SlDeviceDataItemConfig> itemConfigList = new ArrayList<>();
                        for (SlDeviceDataItemConfigVoData itemConfigVoData : detailVoData.getItemList()) {
                            SlDeviceDataItemConfig itemConfig = new SlDeviceDataItemConfig();
                            BeanCopyUtil.copy(itemConfigVoData, itemConfig);
                            itemConfig.setDeviceId(voData.getDeviceId());
                            itemConfig.setConfigId(config.getId());
                            itemConfigList.add(itemConfig);
                        }
                        config.setItemConfigList(itemConfigList);
                        itemList.addAll(itemConfigList);
                    }
                }
                list.add(config);
            }
        }
        //例行试验
        if (voData.getLxsyList() != null && voData.getLxsyList().size() > 0) {
            for (SlDeviceProjectConfigSaveDetailVoData detailVoData : voData.getLxsyList()) {
                SlDeviceProjectConfig config = new SlDeviceProjectConfig();
                BeanCopyUtil.copy(detailVoData, config);
                config.setDeviceId(voData.getDeviceId());
                config.setModelName(device.getModelName());
                config.setDeviceName(device.getDeviceName());
                config.setModelState(ModelStateEnum.LXSY);
                config.setClassify(device.getClassify());
                projectConfigMapper.insert(config);
                if (config.getData() == YNStatusEnum.Y) {
                    if (detailVoData.getItemList() != null && detailVoData.getItemList().size() > 0) {
                        List<SlDeviceDataItemConfig> itemConfigList = new ArrayList<>();
                        for (SlDeviceDataItemConfigVoData itemConfigVoData : detailVoData.getItemList()) {
                            SlDeviceDataItemConfig itemConfig = new SlDeviceDataItemConfig();
                            BeanCopyUtil.copy(itemConfigVoData, itemConfig);
                            itemConfig.setDeviceId(voData.getDeviceId());
                            itemConfig.setConfigId(config.getId());
                            itemConfigList.add(itemConfig);
                        }
                        config.setItemConfigList(itemConfigList);
                        itemList.addAll(itemConfigList);
                    }
                }
                list.add(config);
            }
        }
        if (list.size() > 0) {
            if (itemList.size() > 0) {
                dataItemConfigMapper.insertBatch(itemList);
            }
            List<SlDeviceNo> deviceNoList = deviceNoMapper.findByDeviceId(voData.getDeviceId());
            if (deviceNoList.size() == 0 || deviceNoList.size() != device.getDeviceNum()) {
                throw new BizError("未必设置编号信息");
            }
            for (int i = 1; i <= device.getDeviceNum(); i++) {
                for (SlDeviceProjectConfig config : list) {
                    SlDeviceProjectRecord record = new SlDeviceProjectRecord();
                    record.setDeviceId(device.getId());
                    record.setModelName(device.getModelName());
                    record.setModelDesc(device.getModelDesc());
                    record.setClassify(device.getClassify());
                    record.setDeviceCode(device.getDeviceCode());
                    record.setDeviceName(device.getDeviceName());
                    record.setSysName(device.getSysName());
                    record.setSysSubName(device.getSysSubName());
                    record.setDeviceNum(device.getDeviceNum());
                    record.setModelState(config.getModelState());
                    SlDeviceNo deviceNo = deviceNoList.get(i - 1);
                    record.setDeviceNo(deviceNo.getDeviceNo());
                    record.setExpProject(config.getExpProject());
                    record.setExpCondition(config.getExpCondition());
                    record.setTestProject(config.getTestProject());
                    record.setTestMethod(config.getTestMethod());
                    record.setTestJudge(config.getTestJudge());
                    record.setAttach(config.getAttach());
                    record.setData(config.getData());
                    record.setRecord(config.getRecord());
                    record.setMedia(config.getMedia());
                    record.setSendStatus(YNStatusEnum.N);
                    projectRecordMapper.insert(record);
                    if (record.getData() == YNStatusEnum.Y && config.getItemConfigList() != null && config.getItemConfigList().size() > 0) {
                        List<SlDeviceDataItemConfig> itemConfigList = config.getItemConfigList();
                        List<SlDeviceDataItemRecord> itemRecordList = new ArrayList<>();
                        for (SlDeviceDataItemConfig itemConfig : itemConfigList) {
                            SlDeviceDataItemRecord itemRecord = new SlDeviceDataItemRecord();
                            BeanCopyUtil.copy(itemConfig, itemRecord);
                            itemRecord.setRecordId(record.getId());
                            itemRecordList.add(itemRecord);
                        }
                        dataItemRecordMapper.insertBatch(itemRecordList);
                    }
                }
            }
        }

    }

    /**
     * 查询型号批次设置
     *
     * @param deviceId 数据ID
     * @return
     */
    @Override
    public SlDeviceProjectConfigInfoVoData selectList(Integer deviceId) {
        SlDevice device = deviceMapper.selectById(deviceId);
        if (device == null) {
            throw new BizError("未查询到型号批次");
        }
        List<SlDeviceProjectConfig> zphjList = projectConfigMapper.selectByDeviceIdAndState(deviceId, ModelStateEnum.ZPHJ);
        List<SlDeviceProjectConfig> zjcsList = projectConfigMapper.selectByDeviceIdAndState(deviceId, ModelStateEnum.ZJCS);
        List<SlDeviceProjectConfig> yssyList = projectConfigMapper.selectByDeviceIdAndState(deviceId, ModelStateEnum.YSSY);
        List<SlDeviceProjectConfig> lxsyList = projectConfigMapper.selectByDeviceIdAndState(deviceId, ModelStateEnum.LXSY);
        SlDeviceProjectConfigInfoVoData voData = new SlDeviceProjectConfigInfoVoData();
        voData.setDeviceId(deviceId);
        voData.setModelName(device.getModelName());
        voData.setDeviceName(device.getDeviceName());
        List<SlDeviceProjectConfigVoData> zphj = BeanCopyUtil.copyList(zphjList, SlDeviceProjectConfigVoData.class);
        List<SlDeviceProjectConfigVoData> zjcs = BeanCopyUtil.copyList(zjcsList, SlDeviceProjectConfigVoData.class);
        List<SlDeviceProjectConfigVoData> yssy = BeanCopyUtil.copyList(yssyList, SlDeviceProjectConfigVoData.class);
        List<SlDeviceProjectConfigVoData> lxsy = BeanCopyUtil.copyList(lxsyList, SlDeviceProjectConfigVoData.class);
        for (SlDeviceProjectConfigVoData configVoData : zphj) {
            if (configVoData.getData() == YNStatusEnum.Y) {
                List<SlDeviceDataItemConfig> configList = dataItemConfigMapper.findByConfigId(configVoData.getId());
                List<SlDeviceDataItemConfigVoData> configVoList = BeanCopyUtil.copyList(configList, SlDeviceDataItemConfigVoData.class);
                configVoData.setItemList(configVoList);
            }
        }
        for (SlDeviceProjectConfigVoData configVoData : zjcs) {
            if (configVoData.getData() == YNStatusEnum.Y) {
                List<SlDeviceDataItemConfig> configList = dataItemConfigMapper.findByConfigId(configVoData.getId());
                List<SlDeviceDataItemConfigVoData> configVoList = BeanCopyUtil.copyList(configList, SlDeviceDataItemConfigVoData.class);
                configVoData.setItemList(configVoList);
            }
        }
        for (SlDeviceProjectConfigVoData configVoData : yssy) {
            if (configVoData.getData() == YNStatusEnum.Y) {
                List<SlDeviceDataItemConfig> configList = dataItemConfigMapper.findByConfigId(configVoData.getId());
                List<SlDeviceDataItemConfigVoData> configVoList = BeanCopyUtil.copyList(configList, SlDeviceDataItemConfigVoData.class);
                configVoData.setItemList(configVoList);
            }
        }
        for (SlDeviceProjectConfigVoData configVoData : lxsy) {
            if (configVoData.getData() == YNStatusEnum.Y) {
                List<SlDeviceDataItemConfig> configList = dataItemConfigMapper.findByConfigId(configVoData.getId());
                List<SlDeviceDataItemConfigVoData> configVoList = BeanCopyUtil.copyList(configList, SlDeviceDataItemConfigVoData.class);
                configVoData.setItemList(configVoList);
            }
        }
        voData.setZphjList(zphj);
        voData.setZjcsList(zjcs);
        voData.setYssyList(yssy);
        voData.setLxsyList(lxsy);
        return voData;
    }

    /**
     * 导入excel
     *
     * @param deviceId 数据ID
     * @param file     文件
     */
    @Override
    @Transactional
    public void importExcel(Integer deviceId, MultipartFile file) {
        ExcelUtils.initialization();
        String excel2003 = "xls";
        String excel2007 = "xlsx";
        Workbook workbook = null;
        try {
            if (file.getOriginalFilename().endsWith(excel2003)) {
                workbook = new HSSFWorkbook(file.getInputStream());
            } else if (file.getOriginalFilename().endsWith(excel2007)) {
                workbook = new XSSFWorkbook(file.getInputStream());
            }
            String[] sheetNames = {"装配环节", "整机测试", "验收试验", "例行试验"};
            HashMap mapContinueRow = new HashMap();
            mapContinueRow.put(1, new Integer[]{1});
            mapContinueRow.put(2, new Integer[]{1});
            mapContinueRow.put(3, new Integer[]{1});
            mapContinueRow.put(4, new Integer[]{1});
            List<List<LinkedHashMap<String, String>>> excelList = ExcelUtils.importForExcelData(workbook, sheetNames, null, mapContinueRow);
            List<LinkedHashMap<String, String>> zphjexcel = excelList.get(0);
            List<LinkedHashMap<String, String>> zjcsexcel = excelList.get(1);
            List<LinkedHashMap<String, String>> yssyexcel = excelList.get(2);
            List<LinkedHashMap<String, String>> lxsyexcel = excelList.get(3);

            SlDeviceProjectConfigSaveVoData voData = new SlDeviceProjectConfigSaveVoData();
            voData.setDeviceId(deviceId);
            List<SlDeviceProjectConfigSaveDetailVoData> zphjList = new ArrayList<>();
            List<SlDeviceProjectConfigSaveDetailVoData> zjcsList = new ArrayList<>();
            List<SlDeviceProjectConfigSaveDetailVoData> yssyList = new ArrayList<>();
            List<SlDeviceProjectConfigSaveDetailVoData> lxsyList = new ArrayList<>();
            if (zphjexcel != null && zphjexcel.size() > 0) {
                String testProject = "";
                String testMethod = "";
                String testJudge = "";
                YNStatusEnum attach = null;
                YNStatusEnum data = null;
                Map<String, String> itemMap = new HashMap<>();
                YNStatusEnum record = null;
                YNStatusEnum media = null;
                for (int i = 0; i < zphjexcel.size(); i++) {
                    LinkedHashMap<String, String> row = zphjexcel.get(i);
                    if (StringUtils.isNotBlank(row.get("1"))) {
                        if (StringUtils.isNotBlank(testProject)) {
                            SlDeviceProjectConfigSaveDetailVoData detailVoData = new SlDeviceProjectConfigSaveDetailVoData();
                            detailVoData.setTestProject(testProject);
                            detailVoData.setTestMethod(testMethod);
                            detailVoData.setTestJudge(testJudge);
                            detailVoData.setAttach(attach);
                            detailVoData.setData(data);
                            detailVoData.setRecord(record);
                            detailVoData.setMedia(media);
                            if (data == YNStatusEnum.Y) {
                                List<SlDeviceDataItemConfigVoData> itemConfigList = new ArrayList<>();
                                for (String key : itemMap.keySet()) {
                                    SlDeviceDataItemConfigVoData itemConfig = new SlDeviceDataItemConfigVoData();
                                    itemConfig.setDataItem(key);
                                    itemConfig.setDataUnit(itemMap.get(key));
                                    itemConfigList.add(itemConfig);
                                }
                                detailVoData.setItemList(itemConfigList);
                            }
                            zphjList.add(detailVoData);
                            itemMap.clear();
                        }
                        testProject = row.get("1");
                        testMethod = row.get("2");
                        testJudge = row.get("3");
                        attach = convert(row.get("6"));
                        data = convert(row.get("7"));
                        record = convert(row.get("8"));
                        media = convert(row.get("9"));
                    }
                    itemMap.put(row.get("4"), row.get("5"));
                    if (i == zphjexcel.size() - 1) {
                        SlDeviceProjectConfigSaveDetailVoData detailVoData = new SlDeviceProjectConfigSaveDetailVoData();
                        detailVoData.setTestProject(testProject);
                        detailVoData.setTestMethod(testMethod);
                        detailVoData.setTestJudge(testJudge);
                        detailVoData.setAttach(attach);
                        detailVoData.setData(data);
                        detailVoData.setRecord(record);
                        detailVoData.setMedia(media);
                        if (data == YNStatusEnum.Y) {
                            List<SlDeviceDataItemConfigVoData> itemConfigList = new ArrayList<>();
                            for (String key : itemMap.keySet()) {
                                SlDeviceDataItemConfigVoData itemConfig = new SlDeviceDataItemConfigVoData();
                                itemConfig.setDataItem(key);
                                itemConfig.setDataUnit(itemMap.get(key));
                                itemConfigList.add(itemConfig);
                            }
                            detailVoData.setItemList(itemConfigList);
                        }
                        zphjList.add(detailVoData);
                    }
                }
            }
            if (zjcsexcel != null && zjcsexcel.size() > 0) {
                String testProject = "";
                String testMethod = "";
                String testJudge = "";
                YNStatusEnum attach = null;
                YNStatusEnum data = null;
                Map<String, String> itemMap = new HashMap<>();
                YNStatusEnum record = null;
                YNStatusEnum media = null;
                for (int i = 0; i < zjcsexcel.size(); i++) {
                    LinkedHashMap<String, String> row = zjcsexcel.get(i);
                    if (StringUtils.isNotBlank(row.get("1"))) {
                        if (StringUtils.isNotBlank(testProject)) {
                            SlDeviceProjectConfigSaveDetailVoData detailVoData = new SlDeviceProjectConfigSaveDetailVoData();
                            detailVoData.setTestProject(testProject);
                            detailVoData.setTestMethod(testMethod);
                            detailVoData.setTestJudge(testJudge);
                            detailVoData.setAttach(attach);
                            detailVoData.setData(data);
                            detailVoData.setRecord(record);
                            detailVoData.setMedia(media);
                            if (data == YNStatusEnum.Y) {
                                List<SlDeviceDataItemConfigVoData> itemConfigList = new ArrayList<>();
                                for (String key : itemMap.keySet()) {
                                    SlDeviceDataItemConfigVoData itemConfig = new SlDeviceDataItemConfigVoData();
                                    itemConfig.setDataItem(key);
                                    itemConfig.setDataUnit(itemMap.get(key));
                                    itemConfigList.add(itemConfig);
                                }
                                detailVoData.setItemList(itemConfigList);
                            }
                            zjcsList.add(detailVoData);
                            itemMap.clear();
                        }
                        testProject = row.get("1");
                        testMethod = row.get("2");
                        testJudge = row.get("3");
                        attach = convert(row.get("6"));
                        data = convert(row.get("7"));
                        record = convert(row.get("8"));
                        media = convert(row.get("9"));
                    }
                    itemMap.put(row.get("4"), row.get("5"));
                    if (i == zjcsexcel.size() - 1) {
                        SlDeviceProjectConfigSaveDetailVoData detailVoData = new SlDeviceProjectConfigSaveDetailVoData();
                        detailVoData.setTestProject(testProject);
                        detailVoData.setTestMethod(testMethod);
                        detailVoData.setTestJudge(testJudge);
                        detailVoData.setAttach(attach);
                        detailVoData.setData(data);
                        detailVoData.setRecord(record);
                        detailVoData.setMedia(media);
                        if (data == YNStatusEnum.Y) {
                            List<SlDeviceDataItemConfigVoData> itemConfigList = new ArrayList<>();
                            for (String key : itemMap.keySet()) {
                                SlDeviceDataItemConfigVoData itemConfig = new SlDeviceDataItemConfigVoData();
                                itemConfig.setDataItem(key);
                                itemConfig.setDataUnit(itemMap.get(key));
                                itemConfigList.add(itemConfig);
                            }
                            detailVoData.setItemList(itemConfigList);
                        }
                        zjcsList.add(detailVoData);
                    }
                }
            }
            if (yssyexcel != null && yssyexcel.size() > 0) {
                String expProject = "";
                String expCondition = "";

                String testProject = "";
                String testMethod = "";
                String testJudge = "";
                YNStatusEnum attach = null;
                YNStatusEnum data = null;
                Map<String, String> itemMap = new HashMap<>();
                YNStatusEnum record = null;
                YNStatusEnum media = null;
                for (int i = 0; i < yssyexcel.size(); i++) {
                    LinkedHashMap<String, String> row = yssyexcel.get(i);
                    if (StringUtils.isNotBlank(row.get("3"))) {
                        if (StringUtils.isNotBlank(testProject)) {
                            SlDeviceProjectConfigSaveDetailVoData detailVoData = new SlDeviceProjectConfigSaveDetailVoData();
                            detailVoData.setExpProject(expProject);
                            detailVoData.setExpCondition(expCondition);
                            detailVoData.setTestProject(testProject);
                            detailVoData.setTestMethod(testMethod);
                            detailVoData.setTestJudge(testJudge);
                            detailVoData.setAttach(attach);
                            detailVoData.setData(data);
                            detailVoData.setRecord(record);
                            detailVoData.setMedia(media);
                            if (data == YNStatusEnum.Y) {
                                List<SlDeviceDataItemConfigVoData> itemConfigList = new ArrayList<>();
                                for (String key : itemMap.keySet()) {
                                    SlDeviceDataItemConfigVoData itemConfig = new SlDeviceDataItemConfigVoData();
                                    itemConfig.setDataItem(key);
                                    itemConfig.setDataUnit(itemMap.get(key));
                                    itemConfigList.add(itemConfig);
                                }
                                detailVoData.setItemList(itemConfigList);
                            }
                            yssyList.add(detailVoData);
                            itemMap.clear();
                        }
                        testProject = row.get("3");
                        testMethod = row.get("4");
                        testJudge = row.get("5");
                        attach = convert(row.get("8"));
                        data = convert(row.get("9"));
                        record = convert(row.get("10"));
                        media = convert(row.get("11"));
                    }
                    if (StringUtils.isNotBlank(row.get("1"))) {
                        expProject = row.get("1");
                    }
                    if (StringUtils.isNotBlank(row.get("2"))) {
                        expCondition = row.get("2");
                    }
                    itemMap.put(row.get("6"), row.get("7"));
                    if (i == yssyexcel.size() - 1) {
                        SlDeviceProjectConfigSaveDetailVoData detailVoData = new SlDeviceProjectConfigSaveDetailVoData();
                        detailVoData.setExpProject(expProject);
                        detailVoData.setExpCondition(expCondition);
                        detailVoData.setTestProject(testProject);
                        detailVoData.setTestMethod(testMethod);
                        detailVoData.setTestJudge(testJudge);
                        detailVoData.setAttach(attach);
                        detailVoData.setData(data);
                        detailVoData.setRecord(record);
                        detailVoData.setMedia(media);
                        if (data == YNStatusEnum.Y) {
                            List<SlDeviceDataItemConfigVoData> itemConfigList = new ArrayList<>();
                            for (String key : itemMap.keySet()) {
                                SlDeviceDataItemConfigVoData itemConfig = new SlDeviceDataItemConfigVoData();
                                itemConfig.setDataItem(key);
                                itemConfig.setDataUnit(itemMap.get(key));
                                itemConfigList.add(itemConfig);
                            }
                            detailVoData.setItemList(itemConfigList);
                        }
                        yssyList.add(detailVoData);
                    }
                }
            }

            if (lxsyexcel != null && lxsyexcel.size() > 0) {
                String expProject = "";
                String expCondition = "";

                String testProject = "";
                String testMethod = "";
                String testJudge = "";
                YNStatusEnum attach = null;
                YNStatusEnum data = null;
                Map<String, String> itemMap = new HashMap<>();
                YNStatusEnum record = null;
                YNStatusEnum media = null;
                for (int i = 0; i < lxsyexcel.size(); i++) {
                    LinkedHashMap<String, String> row = lxsyexcel.get(i);
                    if (StringUtils.isNotBlank(row.get("3"))) {
                        if (StringUtils.isNotBlank(testProject)) {
                            SlDeviceProjectConfigSaveDetailVoData detailVoData = new SlDeviceProjectConfigSaveDetailVoData();
                            detailVoData.setExpProject(expProject);
                            detailVoData.setExpCondition(expCondition);
                            detailVoData.setTestProject(testProject);
                            detailVoData.setTestMethod(testMethod);
                            detailVoData.setTestJudge(testJudge);
                            detailVoData.setAttach(attach);
                            detailVoData.setData(data);
                            detailVoData.setRecord(record);
                            detailVoData.setMedia(media);
                            if (data == YNStatusEnum.Y) {
                                List<SlDeviceDataItemConfigVoData> itemConfigList = new ArrayList<>();
                                for (String key : itemMap.keySet()) {
                                    SlDeviceDataItemConfigVoData itemConfig = new SlDeviceDataItemConfigVoData();
                                    itemConfig.setDataItem(key);
                                    itemConfig.setDataUnit(itemMap.get(key));
                                    itemConfigList.add(itemConfig);
                                }
                                detailVoData.setItemList(itemConfigList);
                            }
                            lxsyList.add(detailVoData);
                            itemMap.clear();
                        }

                        testProject = row.get("3");
                        testMethod = row.get("4");
                        testJudge = row.get("5");
                        attach = convert(row.get("8"));
                        data = convert(row.get("9"));
                        record = convert(row.get("10"));
                        media = convert(row.get("11"));
                    }
                    if (StringUtils.isNotBlank(row.get("1"))) {
                        expProject = row.get("1");
                    }
                    if (StringUtils.isNotBlank(row.get("2"))) {
                        expCondition = row.get("2");
                    }
                    itemMap.put(row.get("6"), row.get("7"));
                    if (i == lxsyexcel.size() - 1) {
                        SlDeviceProjectConfigSaveDetailVoData detailVoData = new SlDeviceProjectConfigSaveDetailVoData();
                        detailVoData.setExpProject(expProject);
                        detailVoData.setExpCondition(expCondition);
                        detailVoData.setTestProject(testProject);
                        detailVoData.setTestMethod(testMethod);
                        detailVoData.setTestJudge(testJudge);
                        detailVoData.setAttach(attach);
                        detailVoData.setData(data);
                        detailVoData.setRecord(record);
                        detailVoData.setMedia(media);
                        if (data == YNStatusEnum.Y) {
                            List<SlDeviceDataItemConfigVoData> itemConfigList = new ArrayList<>();
                            for (String key : itemMap.keySet()) {
                                SlDeviceDataItemConfigVoData itemConfig = new SlDeviceDataItemConfigVoData();
                                itemConfig.setDataItem(key);
                                itemConfig.setDataUnit(itemMap.get(key));
                                itemConfigList.add(itemConfig);
                            }
                            detailVoData.setItemList(itemConfigList);
                        }
                        lxsyList.add(detailVoData);
                    }
                }
            }
            voData.setZphjList(zphjList);
            voData.setZjcsList(zjcsList);
            voData.setYssyList(yssyList);
            voData.setLxsyList(lxsyList);
            this.save(voData);
        } catch (IOException e) {
            throw new BizError("excel解析异常");
        }
    }

    /**
     * 导出excel
     *
     * @param deviceId
     * @param response
     */
    public void exportExcel(Integer deviceId, HttpServletResponse response) {
        List<SlDeviceProjectConfig> zphjList = projectConfigMapper.selectByDeviceIdAndState(deviceId, ModelStateEnum.ZPHJ);
        List<SlDeviceProjectConfig> zjcsList = projectConfigMapper.selectByDeviceIdAndState(deviceId, ModelStateEnum.ZJCS);
        List<SlDeviceProjectConfig> yssyList = projectConfigMapper.selectByDeviceIdAndState(deviceId, ModelStateEnum.YSSY);
        List<SlDeviceProjectConfig> lxsyList = projectConfigMapper.selectByDeviceIdAndState(deviceId, ModelStateEnum.LXSY);
        List<List<String[]>> dataLists = new ArrayList<>();
        List<String[]> zphjexcel = new ArrayList<>();
        List<String[]> zjcsexcel = new ArrayList<>();
        List<String[]> yssyexcel = new ArrayList<>();
        List<String[]> lxsyexcel = new ArrayList<>();
        HashMap regionMap = new HashMap();
        String[] zphjHead = {"序号", "测试项目", "测试方法", "合格判据", "具体测试", "单位", "上传附件", "数据记录", "记录试验条件", "多媒体记录"};
        zphjexcel.add(zphjHead);
        String[] zjcsHead = {"序号", "测试项目", "测试方法", "合格判据", "具体测试", "单位", "上传附件", "数据记录", "记录试验条件", "多媒体记录"};
        zjcsexcel.add(zjcsHead);
        String[] yssyHead = {"序号", "试验项目", "试验条件", "测试项目", "测试方法", "合格判据", "具体测试", "单位", "上传附件", "数据记录", "记录试验条件", "多媒体记录"};
        yssyexcel.add(yssyHead);
        String[] lxsyHead = {"序号", "试验项目", "试验条件", "测试项目", "测试方法", "合格判据", "具体测试", "单位", "上传附件", "数据记录", "记录试验条件", "多媒体记录"};
        lxsyexcel.add(lxsyHead);

        ArrayList<Integer[]> zphjExcelRegion = new ArrayList<>();
        int zphjStartRow = 1;
        for (int i = 0; i < zphjList.size(); i++) {
            SlDeviceProjectConfig config = zphjList.get(i);
            if (config.getData() == YNStatusEnum.Y) {
                List<SlDeviceDataItemConfig> itemConfigList = dataItemConfigMapper.findByConfigId(config.getId());
                if (itemConfigList != null && itemConfigList.size() > 0) {
                    if (zphjStartRow != (zphjStartRow + itemConfigList.size() - 1)) {
                        zphjExcelRegion.add(new Integer[]{zphjStartRow, zphjStartRow + itemConfigList.size() - 1, 1, 1});
                        zphjExcelRegion.add(new Integer[]{zphjStartRow, zphjStartRow + itemConfigList.size() - 1, 2, 2});
                        zphjExcelRegion.add(new Integer[]{zphjStartRow, zphjStartRow + itemConfigList.size() - 1, 3, 3});
                        zphjExcelRegion.add(new Integer[]{zphjStartRow, zphjStartRow + itemConfigList.size() - 1, 6, 6});
                        zphjExcelRegion.add(new Integer[]{zphjStartRow, zphjStartRow + itemConfigList.size() - 1, 7, 7});
                        zphjExcelRegion.add(new Integer[]{zphjStartRow, zphjStartRow + itemConfigList.size() - 1, 8, 8});
                        zphjExcelRegion.add(new Integer[]{zphjStartRow, zphjStartRow + itemConfigList.size() - 1, 9, 9});
                    }
                    for (SlDeviceDataItemConfig itemConfig : itemConfigList) {
                        String[] valueString = new String[]{(zphjStartRow) + "", config.getTestProject(), config.getTestMethod(), config.getTestJudge(), itemConfig.getDataItem(), itemConfig.getDataUnit(), convert(config.getAttach()), convert(config.getData()), convert(config.getRecord()), convert(config.getMedia())};
                        zphjexcel.add(valueString);
                        zphjStartRow++;
                    }
                } else {
                    String[] valueString = new String[]{(zphjStartRow) + "", config.getTestProject(), config.getTestMethod(), config.getTestJudge(), "", "", convert(config.getAttach()), convert(config.getData()), convert(config.getRecord()), convert(config.getMedia())};
                    zphjexcel.add(valueString);
                    zphjStartRow++;
                }
            } else {
                String[] valueString = new String[]{(zphjStartRow) + "", config.getTestProject(), config.getTestMethod(), config.getTestJudge(), "", "", convert(config.getAttach()), convert(config.getData()), convert(config.getRecord()), convert(config.getMedia())};
                zphjexcel.add(valueString);
                zphjStartRow++;
            }
        }
        regionMap.put(1, zphjExcelRegion);

        ArrayList<Integer[]> zjcsExcelRegion = new ArrayList<>();
        int zjcsStartRow = 1;
        for (int i = 0; i < zjcsList.size(); i++) {
            SlDeviceProjectConfig config = zjcsList.get(i);
            if (config.getData() == YNStatusEnum.Y) {
                List<SlDeviceDataItemConfig> itemConfigList = dataItemConfigMapper.findByConfigId(config.getId());
                if (itemConfigList != null && itemConfigList.size() > 0) {
                    if (zjcsStartRow != (zjcsStartRow + itemConfigList.size() - 1)) {
                        zjcsExcelRegion.add(new Integer[]{zjcsStartRow, zjcsStartRow + itemConfigList.size() - 1, 1, 1});
                        zjcsExcelRegion.add(new Integer[]{zjcsStartRow, zjcsStartRow + itemConfigList.size() - 1, 2, 2});
                        zjcsExcelRegion.add(new Integer[]{zjcsStartRow, zjcsStartRow + itemConfigList.size() - 1, 3, 3});
                        zjcsExcelRegion.add(new Integer[]{zjcsStartRow, zjcsStartRow + itemConfigList.size() - 1, 6, 6});
                        zjcsExcelRegion.add(new Integer[]{zjcsStartRow, zjcsStartRow + itemConfigList.size() - 1, 7, 7});
                        zjcsExcelRegion.add(new Integer[]{zjcsStartRow, zjcsStartRow + itemConfigList.size() - 1, 8, 8});
                        zjcsExcelRegion.add(new Integer[]{zjcsStartRow, zjcsStartRow + itemConfigList.size() - 1, 9, 9});
                    }
                    for (SlDeviceDataItemConfig itemConfig : itemConfigList) {
                        String[] valueString = new String[]{(zjcsStartRow) + "", config.getTestProject(), config.getTestMethod(), config.getTestJudge(), itemConfig.getDataItem(), itemConfig.getDataUnit(), convert(config.getAttach()), convert(config.getData()), convert(config.getRecord()), convert(config.getMedia())};
                        zjcsexcel.add(valueString);
                        zjcsStartRow++;
                    }
                } else {
                    String[] valueString = new String[]{(zjcsStartRow) + "", config.getTestProject(), config.getTestMethod(), config.getTestJudge(), "", "", convert(config.getAttach()), convert(config.getData()), convert(config.getRecord()), convert(config.getMedia())};
                    zjcsexcel.add(valueString);
                    zjcsStartRow++;
                }
            } else {
                String[] valueString = new String[]{(zjcsStartRow) + "", config.getTestProject(), config.getTestMethod(), config.getTestJudge(), "", "", convert(config.getAttach()), convert(config.getData()), convert(config.getRecord()), convert(config.getMedia())};
                zjcsexcel.add(valueString);
                zjcsStartRow++;
            }
        }
        regionMap.put(2, zjcsExcelRegion);
        ArrayList<Integer[]> yssyExcelRegion = new ArrayList<>();
        String yssySyxm = "";
        String yssySytj = "";
        int yssySyxmStartRow = 1;
        int yssySytjStartRow = 1;
        int yssyStartRow = 1;
        for (int i = 0; i < yssyList.size(); i++) {
            SlDeviceProjectConfig config = yssyList.get(i);
            if (!config.getExpProject().equals(yssySyxm)) {
                if (StringUtils.isNotBlank(yssySyxm)) {
                    yssyExcelRegion.add(new Integer[]{yssySyxmStartRow, yssyStartRow - 1, 1, 1});
                }
                yssySyxm = config.getExpProject();
                yssySyxmStartRow = yssyStartRow;
            }
            if (!config.getExpCondition().equals(yssySytj)) {
                if (StringUtils.isNotBlank(yssySytj)) {
                    yssyExcelRegion.add(new Integer[]{yssySytjStartRow, yssyStartRow - 1, 2, 2});
                }
                yssySytj = config.getExpCondition();
                yssySytjStartRow = yssyStartRow;
            }
            //最后一行
            if (i == yssyList.size() - 1) {
                if (yssyStartRow - yssySyxmStartRow > 0) {
                    yssyExcelRegion.add(new Integer[]{yssySyxmStartRow, yssyStartRow, 1, 1});
                }
                if (yssyStartRow - yssySytjStartRow > 0) {
                    yssyExcelRegion.add(new Integer[]{yssySytjStartRow, yssyStartRow, 2, 2});
                }
            }
            if (config.getData() == YNStatusEnum.Y) {
                List<SlDeviceDataItemConfig> itemConfigList = dataItemConfigMapper.findByConfigId(config.getId());
                if (itemConfigList != null && itemConfigList.size() > 0) {
                    if (yssyStartRow != (yssyStartRow + itemConfigList.size() - 1)) {
                        yssyExcelRegion.add(new Integer[]{yssyStartRow, yssyStartRow + itemConfigList.size() - 1, 3, 3});
                        yssyExcelRegion.add(new Integer[]{yssyStartRow, yssyStartRow + itemConfigList.size() - 1, 4, 4});
                        yssyExcelRegion.add(new Integer[]{yssyStartRow, yssyStartRow + itemConfigList.size() - 1, 5, 5});
                        yssyExcelRegion.add(new Integer[]{yssyStartRow, yssyStartRow + itemConfigList.size() - 1, 8, 8});
                        yssyExcelRegion.add(new Integer[]{yssyStartRow, yssyStartRow + itemConfigList.size() - 1, 9, 9});
                        yssyExcelRegion.add(new Integer[]{yssyStartRow, yssyStartRow + itemConfigList.size() - 1, 10, 10});
                        yssyExcelRegion.add(new Integer[]{yssyStartRow, yssyStartRow + itemConfigList.size() - 1, 11, 11});
                    }
                    for (SlDeviceDataItemConfig itemConfig : itemConfigList) {
                        String[] valueString = new String[]{(yssyStartRow) + "", config.getExpProject(), config.getExpCondition(), config.getTestProject(), config.getTestMethod(), config.getTestJudge(), itemConfig.getDataItem(), itemConfig.getDataUnit(), convert(config.getAttach()), convert(config.getData()), convert(config.getRecord()), convert(config.getMedia())};
                        yssyexcel.add(valueString);
                        yssyStartRow++;
                    }
                } else {
                    String[] valueString = new String[]{(yssyStartRow) + "", config.getExpProject(), config.getExpCondition(), config.getTestProject(), config.getTestMethod(), config.getTestJudge(), "", "", convert(config.getAttach()), convert(config.getData()), convert(config.getRecord()), convert(config.getMedia())};
                    yssyexcel.add(valueString);
                    yssyStartRow++;
                }
            } else {
                String[] valueString = new String[]{(yssyStartRow) + "", config.getExpProject(), config.getExpCondition(), config.getTestProject(), config.getTestMethod(), config.getTestJudge(), "", "", convert(config.getAttach()), convert(config.getData()), convert(config.getRecord()), convert(config.getMedia())};
                yssyexcel.add(valueString);
                yssyStartRow++;
            }
        }
        regionMap.put(3, yssyExcelRegion);

        ArrayList<Integer[]> lxsyExcelRegion = new ArrayList<>();
        String lxsySyxm = "";
        String lxsySytj = "";
        int lxsySyxmStartRow = 1;
        int lxsySytjStartRow = 1;
        int lxsyStartRow = 1;
        for (int i = 0; i < lxsyList.size(); i++) {
            SlDeviceProjectConfig config = lxsyList.get(i);
            if (!config.getExpProject().equals(lxsySyxm)) {
                if (StringUtils.isNotBlank(lxsySyxm)) {
                    lxsyExcelRegion.add(new Integer[]{lxsySyxmStartRow, lxsyStartRow - 1, 1, 1});
                }
                lxsySyxm = config.getExpProject();
                lxsySyxmStartRow = lxsyStartRow;
            }
            if (!config.getExpCondition().equals(lxsySytj)) {
                if (StringUtils.isNotBlank(lxsySytj)) {
                    lxsyExcelRegion.add(new Integer[]{lxsySytjStartRow, lxsyStartRow - 1, 2, 2});
                }
                lxsySytj = config.getExpCondition();
                lxsySytjStartRow = lxsyStartRow;
            }
            //最后一行
            if (i == yssyList.size() - 1) {
                if (lxsyStartRow - lxsySyxmStartRow > 0) {
                    lxsyExcelRegion.add(new Integer[]{lxsySyxmStartRow, lxsyStartRow, 1, 1});
                }
                if (lxsyStartRow - lxsySytjStartRow > 0) {
                    lxsyExcelRegion.add(new Integer[]{lxsySytjStartRow, lxsyStartRow, 2, 2});
                }
            }
            if (config.getData() == YNStatusEnum.Y) {
                List<SlDeviceDataItemConfig> itemConfigList = dataItemConfigMapper.findByConfigId(config.getId());
                if (itemConfigList != null && itemConfigList.size() > 0) {
                    if (lxsyStartRow != (lxsyStartRow + itemConfigList.size() - 1)) {
                        lxsyExcelRegion.add(new Integer[]{lxsyStartRow, lxsyStartRow + itemConfigList.size() - 1, 3, 3});
                        lxsyExcelRegion.add(new Integer[]{lxsyStartRow, lxsyStartRow + itemConfigList.size() - 1, 4, 4});
                        lxsyExcelRegion.add(new Integer[]{lxsyStartRow, lxsyStartRow + itemConfigList.size() - 1, 5, 5});
                        lxsyExcelRegion.add(new Integer[]{lxsyStartRow, lxsyStartRow + itemConfigList.size() - 1, 8, 8});
                        lxsyExcelRegion.add(new Integer[]{lxsyStartRow, lxsyStartRow + itemConfigList.size() - 1, 9, 9});
                        lxsyExcelRegion.add(new Integer[]{lxsyStartRow, lxsyStartRow + itemConfigList.size() - 1, 10, 10});
                        lxsyExcelRegion.add(new Integer[]{lxsyStartRow, lxsyStartRow + itemConfigList.size() - 1, 11, 11});
                    }
                    for (SlDeviceDataItemConfig itemConfig : itemConfigList) {
                        String[] valueString = new String[]{(lxsyStartRow) + "", config.getExpProject(), config.getExpCondition(), config.getTestProject(), config.getTestMethod(), config.getTestJudge(), itemConfig.getDataItem(), itemConfig.getDataUnit(), convert(config.getAttach()), convert(config.getData()), convert(config.getRecord()), convert(config.getMedia())};
                        lxsyexcel.add(valueString);
                        lxsyStartRow++;
                    }
                } else {
                    String[] valueString = new String[]{(lxsyStartRow) + "", config.getExpProject(), config.getExpCondition(), config.getTestProject(), config.getTestMethod(), config.getTestJudge(), "", "", convert(config.getAttach()), convert(config.getData()), convert(config.getRecord()), convert(config.getMedia())};
                    lxsyexcel.add(valueString);
                    lxsyStartRow++;
                }
            } else {
                String[] valueString = new String[]{(lxsyStartRow) + "", config.getExpProject(), config.getExpCondition(), config.getTestProject(), config.getTestMethod(), config.getTestJudge(), "", "", convert(config.getAttach()), convert(config.getData()), convert(config.getRecord()), convert(config.getMedia())};
                lxsyexcel.add(valueString);
                lxsyStartRow++;
            }
        }
        regionMap.put(4, lxsyExcelRegion);
        dataLists.add(zphjexcel);
        dataLists.add(zjcsexcel);
        dataLists.add(yssyexcel);
        dataLists.add(lxsyexcel);

        ExcelUtils excelUtils = ExcelUtils.initialization();
        // 必填项--导出数据（参数请看下面的格式）
        excelUtils.setDataLists(dataLists);
        // 必填项--sheet名称（如果是多表格导出、sheetName也要是多个值！）
        String[] sheetNameList = new String[]{"装配环节", "整机测试", "验收试验", "例行试验"};
        excelUtils.setSheetName(sheetNameList);
        // 文件名称(可为空，默认是：sheet 第一个名称)
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        excelUtils.setFileName("试验项目配置" + df.format(new Date()));
        // web项目response响应输出流：必须填 【ExcelUtils 对象】
        excelUtils.setResponse(response);
        excelUtils.setRegionMap(regionMap);
        response.setHeader("Content-Type", "application/x-excel");
        // 执行导出
        excelUtils.exportForExcelsOptimize();
    }

    private YNStatusEnum convert(String str) {
        if ("是".equals(str)) {
            return YNStatusEnum.Y;
        } else {
            return YNStatusEnum.N;
        }
    }

    private String convert(YNStatusEnum yn) {
        if (yn == YNStatusEnum.Y) {
            return "是";
        } else {
            return "否";
        }
    }
}
