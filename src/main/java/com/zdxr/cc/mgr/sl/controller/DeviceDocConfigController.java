package com.zdxr.cc.mgr.sl.controller;

import com.zdxr.cc.mgr.sl.common.BizError;
import com.zdxr.cc.mgr.sl.common.BpmsHttpResult;
import com.zdxr.cc.mgr.sl.data.SlDeviceDocConfigSaveVoData;
import com.zdxr.cc.mgr.sl.data.SlDeviceDocConfigVoData;
import com.zdxr.cc.mgr.sl.service.ISlDeviceDocConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "/dlapi/device_doc_config")
@Api(tags = "文档检查项设置")
public class DeviceDocConfigController {
    @Autowired
    private ISlDeviceDocConfigService service;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @PostMapping("save")
    @ApiOperation(value = "保存试验项目设置")
    public BpmsHttpResult save(@RequestBody SlDeviceDocConfigSaveVoData data) {
        try {
            service.save(data);
            return BpmsHttpResult.success();
        } catch (BizError error) {
            log.error("系统异常",error);
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
        }
    }

    @GetMapping("selectList")
    @ApiOperation("查询文件检查项配置")
    public BpmsHttpResult<List<SlDeviceDocConfigVoData>> selectList(@RequestParam @ApiParam(value = "设备ID") Integer deviceId) {
        try {
            List<SlDeviceDocConfigVoData> list = service.selectList(deviceId);
            return BpmsHttpResult.success(list);
        } catch (BizError error) {
            log.error("系统异常",error);
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
        }
    }

    @PostMapping("importExcel")
    @ApiOperation("导入文档检查项目配置")
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
    @ApiOperation("导出文档检查项目配置")
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
