package com.zdxr.cc.mgr.sl.service;

import com.zdxr.cc.mgr.sl.data.SlDeviceProjectRecordVoData;
import com.zdxr.cc.mgr.sl.enums.ModelStateEnum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 实验项目数据记录
 */
public interface ISlDeviceProjectRecordService {

    /**
     * 查询项目记录
     *
     * @param deviceId 数据id
     * @param deviceNo 编号
     * @return
     */
    List<SlDeviceProjectRecordVoData> selectList(Integer deviceId, String deviceNo, ModelStateEnum modelState);

    /**
     * 导出excel
     *
     * @param deviceId id
     * @param response 编号
     */
    void exportExcel(Integer deviceId,HttpServletRequest request, HttpServletResponse response);

    /**
     * 生成下载报告
     *
     * @param deviceId 数据id
     * @param response
     */
    void downloadReport(Integer deviceId, HttpServletRequest request, HttpServletResponse response);
}
