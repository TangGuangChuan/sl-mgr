package com.zdxr.cc.mgr.sl.mapper;

import com.zdxr.cc.mgr.sl.config.mybatisplus.SlMapper;
import com.zdxr.cc.mgr.sl.entity.SlDeviceNo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SlDeviceNoMapper extends SlMapper<SlDeviceNo> {
    @Select("select * from sl_device_no where device_id = #{deviceId} order by id")
    List<SlDeviceNo> findByDeviceId(@Param("deviceId") Integer deviceId);

    @Delete("delete from sl_device_no where device_id = #{deviceId}")
    int deleteByDeviceId(@Param("deviceId") Integer deviceId);


    @Select("select * from sl_device_no where device_id = #{deviceId} and device_no in (${deviceNoStr}) order by id")
    List<SlDeviceNo> findByDeviceIdAndDeviceNo(@Param("deviceId") Integer deviceId,@Param("deviceNoStr")String deviceNoStr);
}
