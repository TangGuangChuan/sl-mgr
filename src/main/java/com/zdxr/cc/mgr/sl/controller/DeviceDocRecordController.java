package com.zdxr.cc.mgr.sl.controller;

import com.zdxr.cc.mgr.sl.common.BizError;
import com.zdxr.cc.mgr.sl.common.BpmsHttpResult;
import com.zdxr.cc.mgr.sl.data.SlDeviceDocRecordVoData;
import com.zdxr.cc.mgr.sl.service.ISlDeviceDocRecordService;
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

import java.util.List;

@RestController
@RequestMapping(value = "/dlapi/device_doc_record")
@Api(tags = "试验结果-文档记录")
public class DeviceDocRecordController {
    @Autowired
    private ISlDeviceDocRecordService service;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @GetMapping("selectList")
    @ApiOperation("文档记录")
    public BpmsHttpResult<List<SlDeviceDocRecordVoData>> selectList(@RequestParam @ApiParam(value = "单机ID", name = "deviceId") Integer deviceId) {
        try {
            List<SlDeviceDocRecordVoData> voData = service.selectList(deviceId);
            return BpmsHttpResult.success(voData);
        } catch (BizError error) {
            log.error("系统异常",error);
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
        }
    }
}
