package com.zdxr.cc.mgr.sl.service.impl;

import com.github.andyczy.java.excel.ExcelUtils;
import com.zdxr.cc.mgr.sl.common.BeanCopyUtil;
import com.zdxr.cc.mgr.sl.common.BizError;
import com.zdxr.cc.mgr.sl.data.SlDeviceDocConfigSaveDetailVoData;
import com.zdxr.cc.mgr.sl.data.SlDeviceDocConfigSaveVoData;
import com.zdxr.cc.mgr.sl.data.SlDeviceDocConfigVoData;
import com.zdxr.cc.mgr.sl.entity.SlDevice;
import com.zdxr.cc.mgr.sl.entity.SlDeviceDocConfig;
import com.zdxr.cc.mgr.sl.entity.SlDeviceDocRecord;
import com.zdxr.cc.mgr.sl.enums.YNStatusEnum;
import com.zdxr.cc.mgr.sl.mapper.SlDeviceDocConfigMapper;
import com.zdxr.cc.mgr.sl.mapper.SlDeviceDocRecordMapper;
import com.zdxr.cc.mgr.sl.mapper.SlDeviceMapper;
import com.zdxr.cc.mgr.sl.service.ISlDeviceDocConfigService;
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
 * 文档检查项
 */
@Service
public class SlDeviceDocConfigServiceServiceImpl implements ISlDeviceDocConfigService {
    @Autowired
    private SlDeviceMapper modelMapper;
    @Autowired
    private SlDeviceDocConfigMapper docConfigMapper;
    @Autowired
    private SlDeviceDocRecordMapper docRecordMapper;
    /**
     * 保存文档检查项设置
     * @param voData 检查数据
     */
    @Override
    @Transactional
    public void save(SlDeviceDocConfigSaveVoData voData) {
        SlDevice model = modelMapper.selectById(voData.getDeviceId());
        if (model == null) {
            throw new BizError("型号批次不存在");
        }
        docConfigMapper.deleteBydeviceId(voData.getDeviceId());
        List<SlDeviceDocConfig> list = new ArrayList<>();
        List<SlDeviceDocRecord> recordList = new ArrayList<>();
        docRecordMapper.deleteByDeviceId(model.getId());
        //装配环节
        if (voData.getList() != null && voData.getList().size() > 0) {
            for (SlDeviceDocConfigSaveDetailVoData detailVoData : voData.getList()) {
                SlDeviceDocConfig config = new SlDeviceDocConfig();
                BeanCopyUtil.copy(detailVoData, config);
                config.setDeviceId(voData.getDeviceId());
                config.setModelName(model.getModelName());
                config.setDeviceName(model.getDeviceName());
                config.setClassify(model.getClassify());
                list.add(config);

                SlDeviceDocRecord record = new SlDeviceDocRecord();
                record.setDeviceId(model.getId());
                record.setModelName(model.getModelName());
                record.setModelDesc(model.getModelDesc());
                record.setClassify(model.getClassify());
                record.setDeviceCode(model.getDeviceCode());
                record.setDeviceName(model.getDeviceName());
                record.setDeviceNum(model.getDeviceNum());
                record.setCheckProject(detailVoData.getCheckProject());
                record.setCheckMethod(detailVoData.getCheckMethod());
                record.setTestJudge(detailVoData.getTestJudge());
                record.setAttach(detailVoData.getAttach());
                record.setMedia(detailVoData.getMedia());
                record.setSendStatus(YNStatusEnum.N);
                recordList.add(record);
            }
        }
        if (list.size() > 0) {
            docConfigMapper.insertBatch(list);
            docRecordMapper.insertBatch(recordList);
        }
    }
    /**
     * 查询文档检查项目设置
     *
     * @param deviceId 设备id
     * @return
     */
    @Override
    public List<SlDeviceDocConfigVoData> selectList(Integer deviceId) {
        List<SlDeviceDocConfig> docConfigList = docConfigMapper.selectBydeviceId(deviceId);
        return BeanCopyUtil.copyList(docConfigList, SlDeviceDocConfigVoData.class);
    }

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
            String[] sheetNames = {"文档检查项目"};
            HashMap indexMap = new HashMap();
            indexMap.put(1, 2);
            HashMap mapContinueRow = new HashMap();
            mapContinueRow.put(1, new Integer[]{1});
            List<List<LinkedHashMap<String, String>>> excelList = ExcelUtils.importForExcelData(workbook, sheetNames, indexMap, mapContinueRow);
            List<LinkedHashMap<String, String>> docexcel = excelList.get(0);

            SlDeviceDocConfigSaveVoData voData = new SlDeviceDocConfigSaveVoData();
            voData.setDeviceId(deviceId);
            List<SlDeviceDocConfigSaveDetailVoData> docList = new ArrayList<>();
            if (docexcel != null && docexcel.size() > 0) {
                for (LinkedHashMap<String, String> zphj : docexcel) {
                    SlDeviceDocConfigSaveDetailVoData detailVoData = new SlDeviceDocConfigSaveDetailVoData();
                    detailVoData.setCheckProject(zphj.get("1"));
                    detailVoData.setCheckMethod(zphj.get("2"));
                    detailVoData.setTestJudge(zphj.get("3"));
                    detailVoData.setAttach(convert(zphj.get("4")));
                    detailVoData.setMedia(convert(zphj.get("5")));
                    docList.add(detailVoData);
                }
            }
            voData.setList(docList);
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
        List<SlDeviceDocConfig> docList = docConfigMapper.selectBydeviceId(deviceId);
        List<List<String[]>> dataLists = new ArrayList<>();
        List<String[]> docexcel = new ArrayList<>();
        String[] docHead = {"序号", "检查项目", "检查方法", "合格判据", "上传附件", "多媒体记录"};
        docexcel.add(docHead);
        for (int i = 0; i < docList.size(); i++) {
            SlDeviceDocConfig config = docList.get(i);
            String[] valueString = new String[]{(i + 1) + "", config.getCheckProject(), config.getCheckMethod(), config.getTestJudge(), convert(config.getAttach()), convert(config.getMedia())};
            docexcel.add(valueString);
        }
        dataLists.add(docexcel);
        ExcelUtils excelUtils = ExcelUtils.initialization();
        // 必填项--导出数据（参数请看下面的格式）
        excelUtils.setDataLists(dataLists);
        // 必填项--sheet名称（如果是多表格导出、sheetName也要是多个值！）
        String[] sheetNameList = new String[]{"文档检查项目"};
        excelUtils.setSheetName(sheetNameList);
        // 文件名称(可为空，默认是：sheet 第一个名称)
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        excelUtils.setFileName("文档检查项目配置" + df.format(new Date()));
        // web项目response响应输出流：必须填 【ExcelUtils 对象】
        excelUtils.setResponse(response);
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
