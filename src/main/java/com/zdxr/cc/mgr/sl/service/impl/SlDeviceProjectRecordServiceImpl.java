package com.zdxr.cc.mgr.sl.service.impl;

import com.github.andyczy.java.excel.ExcelUtils;
import com.spire.doc.*;
import com.spire.doc.documents.HyperlinkType;
import com.spire.doc.documents.Paragraph;
import com.zdxr.cc.mgr.sl.common.BeanCopyUtil;
import com.zdxr.cc.mgr.sl.common.BizError;
import com.zdxr.cc.mgr.sl.data.SlDeviceDataItemRecordVoData;
import com.zdxr.cc.mgr.sl.data.SlDeviceProjectRecordVoData;
import com.zdxr.cc.mgr.sl.entity.*;
import com.zdxr.cc.mgr.sl.enums.ModelStateEnum;
import com.zdxr.cc.mgr.sl.enums.YNStatusEnum;
import com.zdxr.cc.mgr.sl.mapper.*;
import com.zdxr.cc.mgr.sl.service.ISlDeviceProjectRecordService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 实验项目数据记录
 */
@Service
public class SlDeviceProjectRecordServiceImpl implements ISlDeviceProjectRecordService {
    private static final Logger log = LoggerFactory.getLogger(SlDeviceProjectRecordServiceImpl.class);

    private static String downloadUrl = "";

    @Autowired
    private SlDeviceProjectRecordMapper mapper;
    @Autowired
    private SlDeviceMapper deviceMapper;
    @Autowired
    private SlAttachMapper attachMapper;
    @Autowired
    private SlDeviceDocRecordMapper docRecordMapper;
    @Autowired
    private SlDeviceDataItemRecordMapper itemRecordMapper;

    /**
     * 查询项目记录
     *
     * @param deviceId 数据id
     * @param deviceNo 编号
     * @return
     */
    @Override
    public List<SlDeviceProjectRecordVoData> selectList(Integer deviceId, String deviceNo, ModelStateEnum modelState) {
        List<SlDeviceProjectRecord> list = mapper.selectList(deviceId, deviceNo, modelState);
        List<SlDeviceProjectRecordVoData> voDataList = BeanCopyUtil.copyList(list, SlDeviceProjectRecordVoData.class);
        for (SlDeviceProjectRecordVoData voData : voDataList) {
            if (voData.getData() == YNStatusEnum.Y) {
                List<SlDeviceDataItemRecord> itemRecordList = itemRecordMapper.selectByRecordId(voData.getId());
                if (itemRecordList != null && itemRecordList.size() > 0) {
                    List<SlDeviceDataItemRecordVoData> recordVoDataList = BeanCopyUtil.copyList(itemRecordList, SlDeviceDataItemRecordVoData.class);
                    voData.setItemRecordList(recordVoDataList);
                }
            }
        }
        return voDataList;
    }
    /**
     * 导出excel
     *
     * @param deviceId id
     * @param response 编号
     */
    @Override
    public void exportExcel(Integer deviceId, HttpServletRequest request, HttpServletResponse response) {
        StringBuffer reqUrl = request.getRequestURL();
        String reqUrlStr = reqUrl.substring(0, reqUrl.indexOf("device_project_record"));
        downloadUrl = reqUrlStr + "attach/download?attachId=";

        List<SlDeviceProjectRecord> zphjList = mapper.selectByState(deviceId, ModelStateEnum.ZPHJ);
        List<SlDeviceProjectRecord> zjcsList = mapper.selectByState(deviceId, ModelStateEnum.ZJCS);
        List<SlDeviceProjectRecord> yssyList = mapper.selectByState(deviceId, ModelStateEnum.YSSY);
        List<SlDeviceProjectRecord> lxsyList = mapper.selectByState(deviceId, ModelStateEnum.LXSY);
        List<SlDeviceDocRecord> docList = docRecordMapper.selectList(deviceId);
        List<List<String[]>> dataLists = new ArrayList<>();
        List<String[]> zphjexcel = new ArrayList<>();
        List<String[]> zjcsexcel = new ArrayList<>();
        List<String[]> yssyexcel = new ArrayList<>();
        List<String[]> lxsyexcel = new ArrayList<>();
        List<String[]> docexcel = new ArrayList<>();
        String[] zphjHead = {"序号", "测试项目", "测试方法", "合格判据", "上传附件", "数据记录", "记录实验条件", "多媒体记录", "是否合格", "操作终端", "电子签名"};
        zphjexcel.add(zphjHead);
        String[] zjcsHead = {"序号", "测试项目", "测试方法", "合格判据", "上传附件", "数据记录", "记录实验条件", "多媒体记录", "是否合格", "操作终端", "电子签名"};
        zjcsexcel.add(zjcsHead);
        String[] yssyHead = {"序号", "试验项目", "试验条件", "测试项目", "合格判据", "上传附件", "数据记录", "记录实验条件", "多媒体记录", "是否合格", "操作终端", "电子签名"};
        yssyexcel.add(yssyHead);
        String[] lxsyHead = {"序号", "试验项目", "试验条件", "测试项目", "合格判据", "上传附件", "数据记录", "记录实验条件", "多媒体记录", "是否合格", "操作终端", "电子签名"};
        lxsyexcel.add(lxsyHead);
        String[] docHead = {"序号", "检查项目", "检查方法", "合格判据", "上传附件", "多媒体记录", "是否合格", "操作终端", "电子签名"};
        docexcel.add(docHead);

        for (int i = 0; i < zphjList.size(); i++) {
            SlDeviceProjectRecord config = zphjList.get(i);
            StringBuffer attachSb = new StringBuffer("");
            if (StringUtils.isNotBlank(config.getAttachIds())) {
                String[] attachIds = config.getAttachIds().split(",");
                for (String attachId : attachIds) {
                    attachSb.append(downloadUrl + attachId).append("\r\n");
                }
            }
            StringBuffer mediaSb = new StringBuffer("");
            if (StringUtils.isNotBlank(config.getMediaIds())) {
                String[] attachIds = config.getMediaIds().split(",");
                for (String attachId : attachIds) {
                    mediaSb.append(downloadUrl + attachId).append("\r\n");
                }
            }
            StringBuffer signSb = new StringBuffer("");
            if (StringUtils.isNotBlank(config.getSignId())) {
                signSb.append(downloadUrl + config.getSignId());
            }
            String[] valueString = new String[]{(i + 1) + "", config.getTestProject(), config.getTestMethod(), config.getTestJudge(), attachSb.toString(), config.getDataTxt(), config.getRecordTxt(), mediaSb.toString(), convert(config.getQualified()), config.getOperatorClient(), signSb.toString()};
            zphjexcel.add(valueString);
        }
        for (int i = 0; i < zjcsList.size(); i++) {
            SlDeviceProjectRecord config = zjcsList.get(i);
            StringBuffer attachSb = new StringBuffer("");
            if (StringUtils.isNotBlank(config.getAttachIds())) {
                String[] attachIds = config.getAttachIds().split(",");
                for (String attachId : attachIds) {
                    attachSb.append(downloadUrl + attachId).append("\r\n");
                }
            }
            StringBuffer mediaSb = new StringBuffer("");
            if (StringUtils.isNotBlank(config.getMediaIds())) {
                String[] attachIds = config.getMediaIds().split(",");
                for (String attachId : attachIds) {
                    mediaSb.append(downloadUrl + attachId).append("\r\n");
                }
            }
            StringBuffer signSb = new StringBuffer("");
            if (StringUtils.isNotBlank(config.getSignId())) {
                signSb.append(downloadUrl + config.getSignId());
            }
            String[] valueString = new String[]{(i + 1) + "", config.getTestProject(), config.getTestMethod(), config.getTestJudge(), attachSb.toString(), config.getDataTxt(), config.getRecordTxt(), mediaSb.toString(), convert(config.getQualified()), config.getOperatorClient(), signSb.toString()};
            zjcsexcel.add(valueString);
        }
        for (int i = 0; i < yssyList.size(); i++) {
            SlDeviceProjectRecord config = yssyList.get(i);
            StringBuffer attachSb = new StringBuffer("");
            if (StringUtils.isNotBlank(config.getAttachIds())) {
                String[] attachIds = config.getAttachIds().split(",");
                for (String attachId : attachIds) {
                    attachSb.append(downloadUrl + attachId).append("\r\n");
                }
            }
            StringBuffer mediaSb = new StringBuffer("");
            if (StringUtils.isNotBlank(config.getMediaIds())) {
                String[] attachIds = config.getMediaIds().split(",");
                for (String attachId : attachIds) {
                    mediaSb.append(downloadUrl + attachId).append("\r\n");
                }
            }
            StringBuffer signSb = new StringBuffer("");
            if (StringUtils.isNotBlank(config.getSignId())) {
                signSb.append(downloadUrl + config.getSignId());
            }
            String[] valueString = new String[]{(i + 1) + "", config.getExpProject(), config.getExpCondition(), config.getTestProject(), config.getTestJudge(), attachSb.toString(), config.getDataTxt(), config.getRecordTxt(), mediaSb.toString(), convert(config.getQualified()), config.getOperatorClient(), signSb.toString()};
            yssyexcel.add(valueString);
        }
        for (int i = 0; i < lxsyList.size(); i++) {
            SlDeviceProjectRecord config = lxsyList.get(i);
            StringBuffer attachSb = new StringBuffer("");
            if (StringUtils.isNotBlank(config.getAttachIds())) {
                String[] attachIds = config.getAttachIds().split(",");
                for (String attachId : attachIds) {
                    attachSb.append(downloadUrl + attachId).append("\r\n");
                }
            }
            StringBuffer mediaSb = new StringBuffer("");
            if (StringUtils.isNotBlank(config.getMediaIds())) {
                String[] attachIds = config.getMediaIds().split(",");
                for (String attachId : attachIds) {
                    mediaSb.append(downloadUrl + attachId).append("\r\n");
                }
            }
            StringBuffer signSb = new StringBuffer("");
            if (StringUtils.isNotBlank(config.getSignId())) {
                signSb.append(downloadUrl + config.getSignId());
            }
            String[] valueString = new String[]{(i + 1) + "", config.getExpProject(), config.getExpCondition(), config.getTestProject(), config.getTestJudge(), attachSb.toString(), config.getDataTxt(), config.getRecordTxt(), mediaSb.toString(), convert(config.getQualified()), config.getOperatorClient(), signSb.toString()};
            lxsyexcel.add(valueString);
        }

        for (int i = 0; i < docList.size(); i++) {
            SlDeviceDocRecord config = docList.get(i);
            StringBuffer attachSb = new StringBuffer("");
            if (StringUtils.isNotBlank(config.getAttachIds())) {
                String[] attachIds = config.getAttachIds().split(",");
                for (String attachId : attachIds) {
                    attachSb.append(downloadUrl + attachId).append("\r\n");
                }
            }
            StringBuffer mediaSb = new StringBuffer("");
            if (StringUtils.isNotBlank(config.getMediaIds())) {
                String[] attachIds = config.getMediaIds().split(",");
                for (String attachId : attachIds) {
                    mediaSb.append(downloadUrl + attachId).append("\r\n");
                }
            }
            StringBuffer signSb = new StringBuffer("");
            if (StringUtils.isNotBlank(config.getSignId())) {
                signSb.append(downloadUrl + config.getSignId());
            }
            String[] valueString = new String[]{(i + 1) + "", config.getCheckProject(), config.getCheckMethod(), config.getTestJudge(), attachSb.toString(), mediaSb.toString(), convert(config.getQualified()), config.getOperatorClient(), signSb.toString()};
            docexcel.add(valueString);
        }

        dataLists.add(zphjexcel);
        dataLists.add(zjcsexcel);
        dataLists.add(yssyexcel);
        dataLists.add(lxsyexcel);
        dataLists.add(docexcel);

        ExcelUtils excelUtils = ExcelUtils.initialization();
        // 必填项--导出数据（参数请看下面的格式）
        excelUtils.setDataLists(dataLists);
        // 必填项--sheet名称（如果是多表格导出、sheetName也要是多个值！）
        String[] sheetNameList = new String[]{"装配环节", "整机测试", "验收试验", "例行试验", "文档检查"};
        excelUtils.setSheetName(sheetNameList);
        // 文件名称(可为空，默认是：sheet 第一个名称)
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        excelUtils.setFileName("试验结果" + df.format(new Date()));
        // web项目response响应输出流：必须填 【ExcelUtils 对象】
        excelUtils.setResponse(response);
        // 执行导出
        excelUtils.exportForExcelsOptimize();
    }

    /**
     * 生成下载报告
     *
     * @param deviceId 数据id
     * @param response
     */
    @Override
    public void downloadReport(Integer deviceId, HttpServletRequest request, HttpServletResponse response) {
        StringBuffer reqUrl = request.getRequestURL();
        String reqUrlStr = reqUrl.substring(0, reqUrl.indexOf("device_project_record"));
        downloadUrl = reqUrlStr + "attach/download?attachId=";
        String baseDir = System.getProperty("user.dir") + "/sl/doc/";
        SlDevice device = deviceMapper.selectById(deviceId);
        String path = baseDir + device.getModelName() + "/" + device.getDeviceName();
        File dir = new File(path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new BizError("创建目录失败：" + dir);
            }
        }
        Document templateDoc = new Document();
        Document document = new Document();
        try {
            InputStream in = getClass().getClassLoader().getResourceAsStream("template/template.docx");
            templateDoc.loadFromStream(in, FileFormat.Auto);
            Section templateSec = templateDoc.getSections().get(0);
            if (templateSec == null) {
                throw new BizError("模板不正确");
            }
            templateDoc.cloneDefaultStyleTo(document);
            templateDoc.cloneCompatibilityTo(document);
            templateDoc.cloneThemesTo(document);

            Paragraph modelDeviceTitle = null, proTitle = null, deviceNo = null;
            Paragraph proZphjTitle = null, proZjcsTitle = null, proYssyTitle = null, proLxsyTitle = null;
            Paragraph docCheck = null, docTableTitle = null;
            for (int i = 0; i < templateSec.getParagraphs().getCount(); i++) {
                Paragraph paragraph = templateSec.getParagraphs().get(i);
                log.info(paragraph.getStyleName() + ":" + paragraph.getText());
                if (paragraph.getText().contains("${model_device_name}")) {
                    modelDeviceTitle = paragraph;
                }

                if (paragraph.getText().contains("${pro_title}")) {
                    proTitle = paragraph;
                }

                if (paragraph.getText().contains("${device_no}")) {
                    deviceNo = paragraph;
                }

                if (paragraph.getText().contains("${pro_zphj_title}")) {
                    proZphjTitle = paragraph;
                }
                if (paragraph.getText().contains("${pro_zjcs_title}")) {
                    proZjcsTitle = paragraph;
                }
                if (paragraph.getText().contains("${pro_yssy_title}")) {
                    proYssyTitle = paragraph;
                }
                if (paragraph.getText().contains("${pro_lxsy_title}")) {
                    proLxsyTitle = paragraph;
                }

                if (paragraph.getText().contains("${doc_check}")) {
                    docCheck = paragraph;
                }
                if (paragraph.getText().contains("${doc_table_title}")) {
                    docTableTitle = paragraph;
                }
            }

            Table tableZphj = null, tableZjcs = null, tableYssy = null, tableLxsy = null, tableDoc = null;
            if (templateSec.getTables() == null || templateSec.getTables().getCount() != 5) {
                throw new BizError("模板错误，模板表不正确");
            }
            tableZphj = templateSec.getTables().get(0);
            tableZjcs = templateSec.getTables().get(1);
            tableYssy = templateSec.getTables().get(2);
            tableLxsy = templateSec.getTables().get(3);
            tableDoc = templateSec.getTables().get(4);

            //设置报告名称
            if (modelDeviceTitle == null) {
                throw new BizError("模板错误，找不到报告标题");
            }


            modelDeviceTitle.setText(modelDeviceTitle.getText().replace("${model_device_name}", device.getModelName() + "-" + device.getDeviceName()));
            Section titleSection = document.addSection();
            templateSec.cloneSectionPropertiesTo(titleSection);
            titleSection.getBody().getChildObjects().add(modelDeviceTitle.deepClone());

            proTitle.setText(proTitle.getText().replace("${pro_title}", "实验项目"));
            Section proTitleSection = document.addSection();
            templateSec.cloneSectionPropertiesTo(proTitleSection);
            proTitleSection.getBody().getChildObjects().add(proTitle.deepClone());

            //设置齐套
            for (int i = 1; i <= device.getDeviceNum(); i++) {
                List<SlDeviceProjectRecord> zphj = mapper.selectList(deviceId, i + "", ModelStateEnum.ZPHJ);
                List<SlDeviceProjectRecord> zjcs = mapper.selectList(deviceId, i + "", ModelStateEnum.ZJCS);
                List<SlDeviceProjectRecord> yssy = mapper.selectList(deviceId, i + "", ModelStateEnum.YSSY);
                List<SlDeviceProjectRecord> lxsy = mapper.selectList(deviceId, i + "", ModelStateEnum.LXSY);
                if (deviceNo == null) {
                    throw new BizError("模板错误，找不到实验项目小节标题");
                }
                deviceNo.setText(i + "");
                Section deviceNoSection = document.addSection();
                templateSec.cloneSectionPropertiesTo(deviceNoSection);
                deviceNoSection.getBody().getChildObjects().add(deviceNo.deepClone());

                if (zphj != null && zphj.size() > 0) {
                    proZphjTitle.setText(proZphjTitle.getText().replace("${pro_zphj_title}", "装配环节"));
                    Section proZphjTitleSection = document.addSection();
                    templateSec.cloneSectionPropertiesTo(proZphjTitleSection);
                    proZphjTitleSection.getBody().getChildObjects().add(proZphjTitle.deepClone());
                    proZphjTitleSection.getBody().getChildObjects().add(generateQtSubTable(tableZphj.deepClone(), baseDir, zphj));
                }

                if (zjcs != null && zjcs.size() > 0) {
                    proZjcsTitle.setText(proZjcsTitle.getText().replace("${pro_zjcs_title}", "整机测试"));
                    Section proZjcsTitleSection = document.addSection();
                    templateSec.cloneSectionPropertiesTo(proZjcsTitleSection);
                    proZjcsTitleSection.getBody().getChildObjects().add(proZjcsTitle.deepClone());
                    proZjcsTitleSection.getBody().getChildObjects().add(generateQtSubTable(tableZjcs.deepClone(), baseDir, zjcs));
                }

                if (yssy != null && yssy.size() > 0) {
                    proYssyTitle.setText(proYssyTitle.getText().replace("${pro_yssy_title}", "验收试验"));
                    Section proYssyTitleSection = document.addSection();
                    templateSec.cloneSectionPropertiesTo(proYssyTitleSection);
                    proYssyTitleSection.getBody().getChildObjects().add(proYssyTitle.deepClone());
                    proYssyTitleSection.getBody().getChildObjects().add(generateLtSubTable(tableYssy.deepClone(), baseDir, yssy));
                }

                if (lxsy != null && lxsy.size() > 0) {
                    proLxsyTitle.setText(proLxsyTitle.getText().replace("${pro_lxsy_title}", "例行试验"));
                    Section proLxsyTitleSection = document.addSection();
                    templateSec.cloneSectionPropertiesTo(proLxsyTitleSection);
                    proLxsyTitleSection.getBody().getChildObjects().add(proLxsyTitle.deepClone());
                    proLxsyTitleSection.getBody().getChildObjects().add(generateLtSubTable(tableLxsy.deepClone(), baseDir, lxsy));
                }
                List<SlDeviceDocRecord> docList = docRecordMapper.selectList(deviceId);
                if (docList != null && docList.size() > 0) {
                    docCheck.setText(docCheck.getText().replace("${doc_check}", "文档检查"));
                    Section docCheckSection = document.addSection();
                    templateSec.cloneSectionPropertiesTo(docCheckSection);
                    docCheckSection.getBody().getChildObjects().add(docCheck.deepClone());
                    docCheckSection.getBody().getChildObjects().add(generateDocSubTable(tableDoc.deepClone(), baseDir, docList));
                }
            }

            //保存文件
            log.info("开始保存文件");
            String file = path + String.format("/%s试验报告.docx", device.getModelName() + "-" + device.getDeviceName());
            document.saveToFile(file);
            if (response != null) {
                response.setHeader("Charset", "UTF-8");
                response.setHeader("Content-Type", "application/force-download");
                response.setHeader("Content-Type", "application/vnd.ms-excel");
                response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(device.getModelName() + "-" + device.getDeviceName() + "试验报告", "utf8") + ".docx");
                response.flushBuffer();
                ServletOutputStream outputStream = response.getOutputStream();
                File docFile = new File(file);
                FileInputStream is = new FileInputStream(docFile);
                byte[] b = new byte[100];
                int len;
                while ((len = is.read(b)) > 0) {
                    outputStream.write(b, 0, len);
                }
                is.close();
                docFile.delete();
                outputStream.flush();
                outputStream.close();
            }
            log.info("保存文件完毕");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizError(e.getMessage());
        } finally {
            templateDoc.close();
        }
        log.info("报告生成成功");
    }

    private Table generateQtSubTable(Table table, String baseDir, List<SlDeviceProjectRecord> records) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Integer count = 1;
        for (SlDeviceProjectRecord record : records) {
            TableRow row = table.addRow();
            row.getCells().get(0).addParagraph().setText(count.toString());
            row.getCells().get(1).addParagraph().setText(record.getTestProject());
            row.getCells().get(2).addParagraph().setText(record.getTestMethod());
            row.getCells().get(3).addParagraph().setText(record.getTestJudge());
            row.getCells().get(4).addParagraph().setText(record.getDataTxt());
            row.getCells().get(5).addParagraph().setText(record.getExpCondition());
            if (record.getAttachIds() != null) {
                String[] attachIds = record.getAttachIds().split(",");
                for (String attachId : attachIds) {
                    SlAttach attach = attachMapper.selectByAttachId(attachId);
                    if (attach != null) {
                        File f = new File(attach.getFileUrl());
                        if (f.exists()) {
                            row.getCells().get(6).addParagraph().appendHyperlink(downloadUrl + attach.getAttachId(), attach.getFileName(), HyperlinkType.File_Link);
                        }
                    }
                }
            }
            if (record.getMediaIds() != null) {
                String[] mediaIds = record.getMediaIds().split(",");
                for (String mediaId : mediaIds) {
                    SlAttach attach = attachMapper.selectByAttachId(mediaId);
                    if (attach != null) {
                        File f = new File(attach.getFileUrl());
                        if (f.exists()) {
                            row.getCells().get(7).addParagraph().appendHyperlink(downloadUrl + attach.getAttachId(), attach.getFileName(), HyperlinkType.File_Link);
                        }
                    }
                }
            }
            row.getCells().get(8).addParagraph().setText(record.getQualified() == null ? "不合格" : record.getQualified() == YNStatusEnum.Y ? "合格" : "不合格");
            row.getCells().get(9).addParagraph().setText(record.getSignName());
            if (record.getSignDate() != null) {
                row.getCells().get(10).addParagraph().setText(df.format(record.getSignDate()));
            } else {
                row.getCells().get(10).addParagraph().setText("");
            }
            row.getCells().get(11).addParagraph().setText("");
            count++;
        }
        return table.deepClone();
    }

    private Table generateLtSubTable(Table table, String baseDir, List<SlDeviceProjectRecord> records) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Integer count = 1;
        for (SlDeviceProjectRecord record : records) {
            TableRow row = table.addRow();
            row.getCells().get(0).addParagraph().setText(count.toString());
            row.getCells().get(1).addParagraph().setText(record.getExpProject());
            row.getCells().get(2).addParagraph().setText(record.getExpCondition());
            row.getCells().get(3).addParagraph().setText(record.getTestProject());
            row.getCells().get(4).addParagraph().setText(record.getTestJudge());
            row.getCells().get(5).addParagraph().setText(record.getDataTxt());
            row.getCells().get(6).addParagraph().setText(record.getExpCondition());
            if (record.getAttachIds() != null) {
                String[] attachIds = record.getAttachIds().split(",");
                for (String attachId : attachIds) {
                    SlAttach attach = attachMapper.selectByAttachId(attachId);
                    if (attach != null) {
                        File f = new File(attach.getFileUrl());
                        if (f.exists()) {
                            row.getCells().get(7).addParagraph().appendHyperlink(downloadUrl + attach.getAttachId(), attach.getFileName(), HyperlinkType.File_Link);
                        }
                    }
                }
            }
            if (record.getMediaIds() != null) {
                String[] mediaIds = record.getMediaIds().split(",");
                for (String mediaId : mediaIds) {
                    SlAttach attach = attachMapper.selectByAttachId(mediaId);
                    if (attach != null) {
                        File f = new File(attach.getFileUrl());
                        if (f.exists()) {
                            row.getCells().get(8).addParagraph().appendHyperlink(downloadUrl + attach.getAttachId(), attach.getFileName(), HyperlinkType.File_Link);
                        }
                    }
                }
            }
            row.getCells().get(9).addParagraph().setText(record.getQualified() == null ? "不合格" : record.getQualified() == YNStatusEnum.Y ? "合格" : "不合格");
            row.getCells().get(10).addParagraph().setText(record.getSignName());
            if (record.getSignDate() != null) {
                row.getCells().get(11).addParagraph().setText(df.format(record.getSignDate()));
            } else {
                row.getCells().get(11).addParagraph().setText("");
            }
            row.getCells().get(12).addParagraph().setText("");
            count++;
        }
        return table.deepClone();
    }


    private Table generateDocSubTable(Table table, String baseDir, List<SlDeviceDocRecord> records) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Integer count = 1;
        for (SlDeviceDocRecord record : records) {
            TableRow row = table.addRow();
            row.getCells().get(0).addParagraph().setText(count.toString());
            row.getCells().get(1).addParagraph().setText(record.getCheckProject());
            row.getCells().get(2).addParagraph().setText(record.getCheckMethod());
            row.getCells().get(3).addParagraph().setText(record.getTestJudge());
            if (record.getAttachIds() != null) {
                String[] attachIds = record.getAttachIds().split(",");
                for (String attachId : attachIds) {
                    SlAttach attach = attachMapper.selectByAttachId(attachId);
                    if (attach != null) {
                        File f = new File(attach.getFileUrl());
                        if (f.exists()) {
                            row.getCells().get(4).addParagraph().appendHyperlink(downloadUrl + attach.getAttachId(), attach.getFileName(), HyperlinkType.File_Link);
                        }
                    }
                }
            }
            if (record.getMediaIds() != null) {
                String[] mediaIds = record.getMediaIds().split(",");
                for (String mediaId : mediaIds) {
                    SlAttach attach = attachMapper.selectByAttachId(mediaId);
                    if (attach != null) {
                        File f = new File(attach.getFileUrl());
                        if (f.exists()) {
                            row.getCells().get(5).addParagraph().appendHyperlink(downloadUrl + attach.getAttachId(), attach.getFileName(), HyperlinkType.File_Link);
                        }
                    }
                }
            }
            row.getCells().get(6).addParagraph().setText(record.getQualified() == null ? "不合格" : record.getQualified() == YNStatusEnum.Y ? "合格" : "不合格");
            row.getCells().get(7).addParagraph().setText(record.getSignName());
            if (record.getSignDate() != null) {
                row.getCells().get(8).addParagraph().setText(df.format(record.getSignDate()));
            } else {
                row.getCells().get(8).addParagraph().setText("");
            }
            row.getCells().get(9).addParagraph().setText("");
            count++;
        }
        return table.deepClone();
    }

    private String convert(YNStatusEnum yn) {
        if (yn == null) {
            return "不合格";
        } else {
            if (yn == YNStatusEnum.Y) {
                return "合格";
            } else {
                return "不合格";
            }
        }
    }
}
