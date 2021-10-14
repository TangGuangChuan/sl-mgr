package com.zdxr.cc.mgr.sl.mapper;

import com.zdxr.cc.mgr.sl.config.mybatisplus.SlMapper;
import com.zdxr.cc.mgr.sl.entity.SlDeviceDataItemRecord;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface SlDeviceDataItemRecordMapper extends SlMapper<SlDeviceDataItemRecord> {
    @Select("select * from sl_device_data_item_record where record_id = #{recordId} ")
    List<SlDeviceDataItemRecord> selectByRecordId(@Param("recordId")Integer recordId);

    @Select("select * from sl_device_data_item_record where device_id = #{deviceId}")
    List<SlDeviceDataItemRecord> findByDeviceId(@Param("deviceId")Integer deviceId);

    @Update("update sl_device_data_item_record set data_val = null where record_id = #{recordId} ")
    int clearByRecordId(@Param("recordId")Integer recordId);
}
