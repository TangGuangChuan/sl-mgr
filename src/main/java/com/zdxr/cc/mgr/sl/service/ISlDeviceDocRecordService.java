package com.zdxr.cc.mgr.sl.service;

import com.zdxr.cc.mgr.sl.data.SlDeviceDocRecordVoData;

import java.util.List;

public interface ISlDeviceDocRecordService {
    /**
     * 文档记录列表查询
     * @param deviceId
     * @return
     */
    List<SlDeviceDocRecordVoData> selectList(Integer deviceId);
}
