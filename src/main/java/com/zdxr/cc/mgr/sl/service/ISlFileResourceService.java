package com.zdxr.cc.mgr.sl.service;

import com.zdxr.cc.mgr.sl.data.SlAttachUpLoadVoData;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 文件资源
 */
public interface ISlFileResourceService {
    /**
     * 文件资源下载
     * @param fileName 文件名
     * @param in 输入流
     */
    void upload(String fileName, MultipartFile file);

    /**
     * 下载附件
     *
     * @param attachId 附件ID
     * @return
     */
    SlAttachUpLoadVoData download(String attachId);
}
