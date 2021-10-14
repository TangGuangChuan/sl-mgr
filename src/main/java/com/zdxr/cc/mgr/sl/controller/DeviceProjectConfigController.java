package com.zdxr.cc.mgr.sl.controller;

import com.zdxr.cc.mgr.sl.common.BizError;
import com.zdxr.cc.mgr.sl.common.BpmsHttpResult;
import com.zdxr.cc.mgr.sl.data.SlDeviceProjectConfigInfoVoData;
import com.zdxr.cc.mgr.sl.data.SlDeviceProjectConfigSaveVoData;
import com.zdxr.cc.mgr.sl.service.ISlDeviceProjectConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/dlapi/device_project_config")
@Api(tags = "试验项目设置")
public class DeviceProjectConfigController {
    @Autowired
    private ISlDeviceProjectConfigService service;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @PostMapping("save")
    @ApiOperation(value = "保存试验项目设置")
    public BpmsHttpResult save(@RequestBody SlDeviceProjectConfigSaveVoData data) {
        try {
            service.save(data);
            return BpmsHttpResult.success();
        } catch (BizError error) {
            log.error("系统异常",error);
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
        }
    }

    @GetMapping("selectList")
    @ApiOperation("根据单机查询试验项目配置")
    public BpmsHttpResult<SlDeviceProjectConfigInfoVoData> selectList(@RequestParam @ApiParam(value = "单机ID", name = "deviceId") Integer deviceId) {
        try {
            SlDeviceProjectConfigInfoVoData voData = service.selectList(deviceId);
            return BpmsHttpResult.success(voData);
        } catch (BizError error) {
            log.error("系统异常",error);
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
        }
    }

    @PostMapping("importExcel")
    @ApiOperation("导入试验项目配置")
    public BpmsHttpResult importExcel(@RequestParam @ApiParam(value = "设备ID", name = "deviceId") Integer deviceId, @RequestParam("file") MultipartFile file) {
        try {
            service.importExcel(deviceId, file);
            return BpmsHttpResult.success();
        } catch (BizError error) {
            log.error("系统异常",error);
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
        }
    }

    @GetMapping("exportExcel")
    @ApiOperation("导出试验项目配置")
    public BpmsHttpResult exportExcel(@RequestParam @ApiParam(value = "设备ID", name = "deviceId") Integer deviceId, HttpServletResponse response) {
        try {
            service.exportExcel(deviceId, response);
            return BpmsHttpResult.success();
        } catch (BizError error) {
            log.error("系统异常",error);
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
        }
    }

}
