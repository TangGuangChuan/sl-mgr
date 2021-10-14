package com.zdxr.cc.mgr.sl.mapper;

import com.zdxr.cc.mgr.sl.config.mybatisplus.SlMapper;
import com.zdxr.cc.mgr.sl.entity.SlDeviceProjectConfig;
import com.zdxr.cc.mgr.sl.enums.ModelStateEnum;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SlDeviceProjectConfigMapper extends SlMapper<SlDeviceProjectConfig> {
    @Delete("delete from sl_device_project_config where device_id = #{deviceId}")
    int deleteBydeviceId(@Param("deviceId") Integer deviceId);

    @Select("select * from sl_device_project_config where device_id = #{deviceId}")
    List<SlDeviceProjectConfig> selectByDeviceId(@Param("deviceId") Integer deviceId);

    @Select("select * from sl_device_project_config where device_id = #{deviceId} and model_state = #{modelState}")
    List<SlDeviceProjectConfig> selectByDeviceIdAndState(@Param("deviceId") Integer deviceId,@Param("modelState") ModelStateEnum modelState);
}
