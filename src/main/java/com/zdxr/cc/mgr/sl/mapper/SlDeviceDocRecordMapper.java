package com.zdxr.cc.mgr.sl.mapper;

import com.zdxr.cc.mgr.sl.config.mybatisplus.SlMapper;
import com.zdxr.cc.mgr.sl.entity.SlDeviceDocRecord;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

public interface SlDeviceDocRecordMapper extends SlMapper<SlDeviceDocRecord> {
    @Select("select * from sl_device_doc_record where device_id = #{device_id}")
    List<SlDeviceDocRecord> selectList(Integer deviceId);

    @Select("select * from sl_device_doc_record where device_id = #{deviceId} and send_status = 'N'")
    List<SlDeviceDocRecord> selectNoSendList(@Param("deviceId") Integer deviceId);

    @Select("select count(1) from sl_device_doc_record where device_id = #{device_id} and client_code = #{clientCode} and send_status = 'Y'")
    int selectDataCountByDeviceId(@Param("deviceId") Integer deviceId, @Param("clientCode") String clientCode);

    @Update("update sl_device_doc_record set send_date = null,send_status = 'N',sign_date = null,sign_id = null,sign_name = null,qualified = null,media_ids = null,attach_ids = null where device_id = #{deviceId} and client_code = #{clientCode} and send_status = 'Y'")
    int clearData(@Param("deviceId") Integer deviceId, @Param("clientCode") String clientCode);

    @Update("update sl_device_doc_record set client_code = #{clientCode}, operator_client = #{operatorClient},send_status='Y',send_date = #{sendDate} where device_id = #{deviceId}")
    int updateSend(@Param("clientCode") String clientCode, @Param("operatorClient") String operatorClient, @Param("sendDate") Date sendDate, @Param("deviceId") Integer deviceId);

    @Select("select count(1) from sl_device_doc_record where device_id = #{deviceId} and (sign_id is null or sign_name is null) ")
    int selectNotComp(@Param("deviceId") Integer deviceId);

    @Select("select count(1) from sl_device_doc_record where device_id = #{deviceId} and sign_id is not null and sign_name is not null ")
    int selectComp(@Param("deviceId") Integer deviceId);

    @Delete("delete from sl_device_doc_record where device_id = #{deviceId}")
    int deleteByDeviceId(@Param("deviceId") Integer deviceId);


    @Select("select count(1) from sl_device_doc_record where device_id = #{deviceId} and (sign_id is null or sign_name is null)")
    int selectNotGroup(@Param("deviceId") Integer deviceId);

    @Update("update sl_device_doc_record set operator_client = #{clientName} where client_code = #{clientCode}")
    int updateClient(@Param("clientCode") String clientCode, @Param("clientName") String clientName);

    @Select("select * from sl_device_doc_record where device_id = #{deviceId}")
    List<SlDeviceDocRecord> findByDeviceId(@Param("deviceId") Integer deviceId);
}
