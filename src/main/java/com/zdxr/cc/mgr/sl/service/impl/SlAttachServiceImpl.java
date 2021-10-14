package com.zdxr.cc.mgr.sl.service.impl;

import com.zdxr.cc.mgr.sl.common.BeanCopyUtil;
import com.zdxr.cc.mgr.sl.common.BizError;
import com.zdxr.cc.mgr.sl.data.SlAttachUpLoadVoData;
import com.zdxr.cc.mgr.sl.entity.SlAttach;
import com.zdxr.cc.mgr.sl.mapper.SlAttachMapper;
import com.zdxr.cc.mgr.sl.service.ISlAttachService;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 附件
 */
@Service
public class SlAttachServiceImpl implements ISlAttachService {
    private final String ATTACH_PATH = File.separator + "sl" + File.separator + "attach" + File.separator;
    private static final Logger log = LoggerFactory.getLogger(SlAttachServiceImpl.class);
    @Autowired
    private SlAttachMapper attachMapper;

    /**
     * 上传附件
     *
     * @param fileName 文件名
     * @param in       文件
     * @return
     */
    @Override
    @Transactional
    public SlAttachUpLoadVoData upload(String fileName, String operatorName, InputStream in) {
        String dirPath = System.getProperty("user.dir") + ATTACH_PATH;
        File directory = new File(dirPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new BizError(String.format("创建目录%s失败", dirPath));
            }
        }
        String urlFileName = this.getGenFileName();
        String[] strArray = fileName.split("\\.");
        int suffixIndex = strArray.length - 1;
        String suffix = strArray[suffixIndex];
        String fileUrl = dirPath + urlFileName + "." + suffix;
        try {
            FileOutputStream fos = new FileOutputStream(fileUrl);
            byte[] buffer = new byte[1024];
            while ((in.read(buffer)) != -1) {
                fos.write(buffer);
            }
            fos.close();
            SlAttach attach = new SlAttach();
            attach.setAttachId(urlFileName);
            attach.setFileName(fileName);
            attach.setFileUrl(fileUrl);
            attach.setSuffix(suffix);
            attach.setOperatorName(operatorName);
            attach.setOperatorTime(new Date());
            attachMapper.insert(attach);
            SlAttachUpLoadVoData voData = new SlAttachUpLoadVoData();
            voData.setAttachId(attach.getAttachId());
            voData.setFileName(fileName);
            return voData;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizError(e.getMessage());
        }
    }

    /**
     * 下载附件
     *
     * @param id 附件ID
     * @return
     */
    @Override
    public SlAttachUpLoadVoData download(String attachId) {
        SlAttach bpmsAttach = attachMapper.selectByAttachId(attachId);
        if (bpmsAttach == null) {
            throw new BizError("附件不存在");
        }
        SlAttachUpLoadVoData voData = new SlAttachUpLoadVoData();
        BeanCopyUtil.copy(bpmsAttach, voData);
        return voData;
    }

    private String getGenFileName() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return dateFormat.format(new Date()) + RandomStringUtils.randomAlphanumeric(13);
    }

}
