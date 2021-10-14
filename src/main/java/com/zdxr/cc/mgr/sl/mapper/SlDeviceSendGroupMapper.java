package com.zdxr.cc.mgr.sl.mapper;

import com.zdxr.cc.mgr.sl.config.mybatisplus.SlMapper;
import com.zdxr.cc.mgr.sl.entity.SlDeviceSendGroup;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface SlDeviceSendGroupMapper extends SlMapper<SlDeviceSendGroup> {
    @Select("select * from sl_device_send_group where device_id = #{deviceId}")
    List<SlDeviceSendGroup> selectList(@Param("deviceId") Integer deviceId);

    @Select("select * from sl_device_send_group where device_id = #{deviceId} and id != #{groupId}")
    List<SlDeviceSendGroup> selectListNotId(@Param("deviceId") Integer deviceId, @Param("groupId") Integer groupId);

    @Select("select * from sl_device_send_group where id in (${groupIds}) and client_code in (${clientStr}) and send_status = 'N'")
    List<SlDeviceSendGroup> selectListByDeviceId(@Param("groupIds") String groupIds, @Param("clientStr") String clientStr);

    @Update("update sl_device_send_group set operator_client = #{clientName} where client_code = #{clientCode}")
    int updateClient(@Param("clientCode") String clientCode, @Param("clientName") String clientName);

    @Update("update sl_device_send_group set status = 'NOT',send_date = null,send_status = 'N' where id = #{id}")
    int updateSendStatus(@Param("id")Integer id);

    @Select("select count(1) from sl_device_send_group where send_status = 'Y' and device_id = #{deviceId}")
    int selectBySendDeviceId(@Param("deviceId")Integer deviceId);
}
