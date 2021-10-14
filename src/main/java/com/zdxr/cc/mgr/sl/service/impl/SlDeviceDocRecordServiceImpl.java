package com.zdxr.cc.mgr.sl.service.impl;

import com.zdxr.cc.mgr.sl.common.BeanCopyUtil;
import com.zdxr.cc.mgr.sl.data.SlDeviceDocRecordVoData;
import com.zdxr.cc.mgr.sl.entity.SlDeviceDocRecord;
import com.zdxr.cc.mgr.sl.mapper.SlDeviceDocRecordMapper;
import com.zdxr.cc.mgr.sl.service.ISlDeviceDocRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlDeviceDocRecordServiceImpl implements ISlDeviceDocRecordService {
    @Autowired
    private SlDeviceDocRecordMapper mapper;


    /**
     * 文档记录列表查询
     * @param deviceId
     * @return
     */
    @Override
    public List<SlDeviceDocRecordVoData> selectList(Integer deviceId) {
        List<SlDeviceDocRecord> list = mapper.selectList(deviceId);
        return BeanCopyUtil.copyList(list, SlDeviceDocRecordVoData.class);
    }
}
