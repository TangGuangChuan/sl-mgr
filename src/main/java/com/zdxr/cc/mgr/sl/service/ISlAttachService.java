package com.zdxr.cc.mgr.sl.service;

import com.zdxr.cc.mgr.sl.data.SlAttachUpLoadVoData;

import java.io.InputStream;

/**
 *附件
 */
public interface ISlAttachService {
    /**
     * 上传附件
     *
     * @param fileName 文件名
     * @param in 文件
     * @return
     */
    SlAttachUpLoadVoData upload(String fileName, String operatorName, InputStream in);

    /**
     * 下载附件
     *
     * @param id 附件ID
     * @return
     */
    SlAttachUpLoadVoData download(String attachId);
}
