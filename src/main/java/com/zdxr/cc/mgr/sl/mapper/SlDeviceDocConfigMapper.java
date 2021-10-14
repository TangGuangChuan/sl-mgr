package com.zdxr.cc.mgr.sl.mapper;

import com.zdxr.cc.mgr.sl.config.mybatisplus.SlMapper;
import com.zdxr.cc.mgr.sl.entity.SlDeviceDocConfig;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SlDeviceDocConfigMapper extends SlMapper<SlDeviceDocConfig> {
    @Delete("delete from sl_device_doc_config where device_id = #{deviceId}")
    int deleteBydeviceId(@Param("deviceId") Integer deviceId);

    @Select("select * from sl_device_doc_config where device_id = #{deviceId}")
    List<SlDeviceDocConfig> selectBydeviceId(@Param("deviceId") Integer deviceId);
}
