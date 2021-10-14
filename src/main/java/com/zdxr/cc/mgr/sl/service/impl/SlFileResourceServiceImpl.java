package com.zdxr.cc.mgr.sl.service.impl;

import com.zdxr.cc.mgr.sl.common.BizError;
import com.zdxr.cc.mgr.sl.data.SlAttachUpLoadVoData;
import com.zdxr.cc.mgr.sl.entity.SlFileResource;
import com.zdxr.cc.mgr.sl.entity.SlSupportMaterial;
import com.zdxr.cc.mgr.sl.mapper.SlFileResourceMapper;
import com.zdxr.cc.mgr.sl.mapper.SlSupportMaterialMapper;
import com.zdxr.cc.mgr.sl.service.ISlFileResourceService;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 文件资源
 */
@Service
public class SlFileResourceServiceImpl implements ISlFileResourceService {
    private final String FILE_RESOURCE_PATH = File.separator + "sl" + File.separator + "file_resource" + File.separator;

    private final String OUT_FILE_PATH = File.separator + "sl" + File.separator + "out_file" + File.separator;

    private static final Logger log = LoggerFactory.getLogger(SlFileResourceServiceImpl.class);
    @Resource
    private SlFileResourceMapper fileResourceMapper;
    @Resource
    private SlSupportMaterialMapper supportMaterialMapper;

    @Override
    public void upload(String fileName, MultipartFile file) {
        String dirPath = System.getProperty("user.dir") + FILE_RESOURCE_PATH;
        String outDir = System.getProperty("user.dir") + OUT_FILE_PATH;
        File directory = new File(dirPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new BizError(String.format("创建压缩包目录%s失败", dirPath));
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
            InputStream in = file.getInputStream();
            while ((in.read(buffer)) != -1) {
                fos.write(buffer);
            }
            in.close();
            fos.close();
            //保存压缩包文件信息
            SlFileResource resource = new SlFileResource();
            resource.setFileId(urlFileName);
            resource.setFileUrl(fileUrl);
            resource.setFileName(fileName);
            resource.setUploadTime(new Date());
            resource.setSuffix(suffix);
            fileResourceMapper.insert(resource);
            //解压并保存解压文件
            if ("zip".equalsIgnoreCase(suffix)) {
                ZipFile zipFile = null;
                File unFile = null;
                InputStream unis = null;
                FileOutputStream unfos = null;
                BufferedOutputStream unbos = null;
                int count = -1;
                try {
                    zipFile = new ZipFile(fileUrl, "gbk");
                    Enumeration<?> entries = zipFile.getEntries();
                    while (entries.hasMoreElements()) {
                        byte buf[] = new byte[1024];
                        ZipEntry entry = (ZipEntry) entries.nextElement();
                        String filename = entry.getName();
                        boolean ismkdir = false;
                        //检查此文件是否带有文件夹
                        if(filename.lastIndexOf("/") != -1){
                            ismkdir = true;
                        }
                        filename = outDir + filename;
                        //如果是文件夹先创建
                        if (entry.isDirectory()) {
                            unFile = new File(filename);
                            unFile.mkdirs();
                            continue;
                        }
                        unFile = new File(filename);
                        //如果是目录先创建
                        if(!unFile.exists()){
                            if(ismkdir){
                                //目录先创建
                                new File(filename.substring(0, filename.lastIndexOf("/"))).mkdirs();
                            }
                        }
                        String[] fileArr = fileName.split("\\.");
                        int index = fileArr.length - 1;
                        //解压文件后缀
                        String unFileSuffix = strArray[suffixIndex];
                        //解压的文件原名
                        String unFileName = fileName.substring(filename.lastIndexOf("/"));
                        //解压文件全路径
                        String unFileUrl = filename.substring(0, filename.lastIndexOf("/")) + File.separator + this.getGenFileName() + "." + unFileSuffix;
                        //创建文件
                        unFile.createNewFile();

                        unis = zipFile.getInputStream((ZipArchiveEntry) entry);
                        unfos = new FileOutputStream(unFile);
                        unbos = new BufferedOutputStream(unfos, 1024);
                        while (unis.read(buffer) != -1) {
                            unfos.write(buffer);
                        }
                        unbos.flush();
                        //保存解压的文件信息
                        SlSupportMaterial supportMaterial = new SlSupportMaterial();
                        supportMaterial.setFileName(unFileName);
                        supportMaterial.setFileId(urlFileName);
                        supportMaterial.setFileUrl(unFileUrl);
                        supportMaterial.setSuffix(unFileSuffix);
                        supportMaterialMapper.insert(supportMaterialMapper);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    try{
                        if(unbos != null){
                            unbos.close();
                        }
                        if(unfos != null) {
                            unfos.close();
                        }
                        if(unis != null){
                            unis.close();
                        }
                        if(zipFile != null){
                            zipFile.close();
                        }
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }else if("rar".equalsIgnoreCase(suffix)){
                System.out.println("rar文件");
            }else {
                throw new BizError("文件非zip/rar类型的压缩文件");
            }
        } catch (IOException e) {
            log.error("文件上传失败：{}", e);
            throw new BizError(e.getMessage());
        }
    }

    @Override
    public SlAttachUpLoadVoData download(String attachId) {
        return null;
    }

    private String getGenFileName() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return dateFormat.format(new Date()) + RandomStringUtils.randomAlphanumeric(13);
    }

}
