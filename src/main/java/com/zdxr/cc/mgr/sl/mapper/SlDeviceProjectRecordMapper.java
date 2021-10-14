package com.zdxr.cc.mgr.sl.mapper;

import com.zdxr.cc.mgr.sl.config.mybatisplus.SlMapper;
import com.zdxr.cc.mgr.sl.entity.SlDeviceProjectRecord;
import com.zdxr.cc.mgr.sl.enums.ModelStateEnum;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

public interface SlDeviceProjectRecordMapper extends SlMapper<SlDeviceProjectRecord> {

    @Select("select * from sl_device_project_record where device_id = #{deviceId} and device_no = #{deviceNo} and model_state = #{modelState}")
    List<SlDeviceProjectRecord> selectList(@Param("deviceId") Integer deviceId, @Param("deviceNo") String deviceNo, @Param("modelState") ModelStateEnum modelState);


    @Select("select * from sl_device_project_record where device_id = #{deviceId}  and model_state = #{modelState}")
    List<SlDeviceProjectRecord> selectByState(@Param("deviceId") Integer deviceId, @Param("modelState") ModelStateEnum modelState);

    @Select("select * from sl_device_project_record where device_id = #{deviceId} and send_status = 'Y'")
    List<SlDeviceProjectRecord> selectSend(@Param("deviceId") Integer deviceId);

    @Delete("delete from sl_device_project_record where device_id = #{deviceId}")
    int deleteByDeviceId(@Param("deviceId") Integer deviceId);

    @Select("select * from sl_device_project_record where device_id = #{deviceId} and model_state = #{modelState} and device_no in (${deviceNoStr}) and send_status = 'N'")
    List<SlDeviceProjectRecord> selectByNoAndState(@Param("deviceId") Integer deviceId, @Param("modelState") String modelState, @Param("deviceNoStr") String deviceNoStr);

    @Select("select * from sl_device_project_record where device_id = #{deviceId} and model_state = #{modelState} and device_no in (${deviceNoStr}) and send_status = 'Y' and client_code = #{clientCode}")
    List<SlDeviceProjectRecord> selectByYAndState(@Param("deviceId") Integer deviceId, @Param("modelState") String modelState, @Param("deviceNoStr") String deviceNoStr,@Param("clientCode") String clientCode);

    @Update("update sl_device_project_record set send_date = null,sign_date=null,sign_id =null,sign_name = null,qualified=null,media_ids=null,record_txt=null,data_txt=null,attach_ids=null where device_id = #{deviceId} and model_state = #{modelState} and device_no in (${deviceNoStr}) and send_status = 'Y' and client_code = #{clientCode}")
    int clearData(@Param("deviceId") Integer deviceId, @Param("modelState") String modelState, @Param("deviceNoStr") String deviceNoStr,@Param("clientCode") String clientCode);

    @Update("update sl_device_project_record set client_code = #{clientCode}, operator_client = #{operatorClient}, send_status = 'Y',send_date = #{sendDate} where device_id = #{deviceId} and model_state = #{modelState} and device_no in (${deviceNoStr}) and send_status = 'N'")
    int updateSend(@Param("clientCode")String clientCode, @Param("operatorClient") String operatorClient, @Param("sendDate") Date sendDate, @Param("deviceId") Integer deviceId, @Param("modelState") String modelState, @Param("deviceNoStr") String deviceNoStr);

    @Select("select count(1) from sl_device_project_record where device_id = #{deviceId} and (sign_id is null or sign_name is null) ")
    int selectNotComp(@Param("deviceId") Integer deviceId);

    @Select("select count(1) from sl_device_project_record where device_id = #{deviceId} and sign_id is not null and sign_name is not null")
    int selectComp(@Param("deviceId") Integer deviceId);

    @Select("select count(1) from sl_device_project_record where device_id = #{deviceId} and device_no in (${deviceNos}) and model_state = #{modelState} and (sign_id is null or sign_name is null)")
    int selectNotGroup(@Param("deviceId") Integer deviceId, @Param("deviceNos") String deviceNos, @Param("modelState") String modelState);

    @Update("update sl_device_project_record set operator_client = #{clientName} where client_code = #{clientCode}")
    int updateClient(@Param("clientCode") String clientCode, @Param("clientName") String clientName);

    @Select("select * from sl_device_project_record where device_id = #{deviceId}")
    List<SlDeviceProjectRecord> findByDeviceId(@Param("deviceId")Integer deviceId);
}
