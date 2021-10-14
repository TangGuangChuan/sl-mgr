package com.zdxr.cc.mgr.sl.mapper;

import com.zdxr.cc.mgr.sl.config.mybatisplus.SlMapper;
import com.zdxr.cc.mgr.sl.entity.SlDeviceClient;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SlDeviceClientMapper extends SlMapper<SlDeviceClient> {
    @Select("select * from sl_device_client where client_code = #{code}")
    SlDeviceClient findByCode(@Param("code") String code);

    @Select("select * from sl_device_client where client_name = #{name}")
    SlDeviceClient findByName(@Param("name") String name);
}
