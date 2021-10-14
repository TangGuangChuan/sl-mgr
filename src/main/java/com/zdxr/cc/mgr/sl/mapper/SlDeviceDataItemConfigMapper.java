package com.zdxr.cc.mgr.sl.mapper;

import com.zdxr.cc.mgr.sl.config.mybatisplus.SlMapper;
import com.zdxr.cc.mgr.sl.entity.SlDeviceDataItemConfig;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SlDeviceDataItemConfigMapper extends SlMapper<SlDeviceDataItemConfig> {
    @Select("select * from sl_device_data_item_config where config_id = #{configId}")
    List<SlDeviceDataItemConfig> findByConfigId(@Param("configId")Integer configId);

    @Delete("delete from sl_device_data_item_config where device_id = #{deviceId}")
    int deleteByDeviceId(@Param("deviceId")Integer deviceId);
}
