package com.zdxr.cc.mgr.sl.controller;

import com.zdxr.cc.mgr.sl.common.BizError;
import com.zdxr.cc.mgr.sl.common.BpmsHttpResult;
import com.zdxr.cc.mgr.sl.data.SlDeviceProjectRecordVoData;
import com.zdxr.cc.mgr.sl.enums.ModelStateEnum;
import com.zdxr.cc.mgr.sl.service.ISlDeviceProjectRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "/dlapi/device_project_record")
@Api(tags = "试验结果-试验项目记录")
public class DeviceProjectRecordController {
    @Autowired
    private ISlDeviceProjectRecordService service;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @GetMapping("selectList")
    @ApiOperation("试验项目记录")
    public BpmsHttpResult<List<SlDeviceProjectRecordVoData>> selectList(@RequestParam @ApiParam(value = "单机ID", name = "deviceId") Integer deviceId,
                                                                        @RequestParam @ApiParam(value = "设备编号", name = "deviceNo") String deviceNo,
                                                                        @RequestParam @ApiParam(value = "阶段过程", name = "modelState") ModelStateEnum modelState) {
        try {
            List<SlDeviceProjectRecordVoData> voData = service.selectList(deviceId, deviceNo, modelState);
            return BpmsHttpResult.success(voData);
        } catch (BizError error) {
            log.error("系统异常",error);
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
        }
    }

    @GetMapping("exportExcel")
    @ApiOperation("数据导出")
    public BpmsHttpResult exportExcel(@RequestParam @ApiParam(value = "单机ID", name = "deviceId") Integer deviceId, HttpServletRequest request, HttpServletResponse response) {
        try {
            service.exportExcel(deviceId, request, response);
            return BpmsHttpResult.success();
        } catch (BizError error) {
            log.error("系统异常",error);
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
        }
    }

    @GetMapping("downloadReport")
    @ApiOperation("下载报告")
    public BpmsHttpResult downloadReport(@RequestParam @ApiParam(value = "单机ID", name = "deviceId") Integer deviceId, HttpServletRequest request, HttpServletResponse response) {
        try {
            service.downloadReport(deviceId, request, response);
            return BpmsHttpResult.success();
        } catch (BizError error) {
            log.error("系统异常",error);
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
        }
    }
}
