package com.zdxr.cc.mgr.sl.service;

import com.zdxr.cc.mgr.sl.data.SlDeviceClientVoData;

/**
 * 客户端信息
 */
public interface ISlDeviceClientService {
    /**
     * 保存操作端
     * @param voData 客户端信息
     */
    void saveDeviceClient(SlDeviceClientVoData voData);
}
