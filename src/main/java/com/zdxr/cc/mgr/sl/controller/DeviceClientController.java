package com.zdxr.cc.mgr.sl.controller;

import com.zdxr.cc.mgr.sl.common.BizError;
import com.zdxr.cc.mgr.sl.common.BpmsHttpResult;
import com.zdxr.cc.mgr.sl.data.SlDeviceClientVoData;
import com.zdxr.cc.mgr.sl.service.ISlDeviceClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/dlapi/device_client")
@Api(tags = "操作端管理")
public class DeviceClientController {
    @Autowired
    private ISlDeviceClientService clientService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @PostMapping("saveClient")
    @ApiOperation(value = "保存")
    public BpmsHttpResult saveClient(@RequestBody SlDeviceClientVoData data) {
        try {
            clientService.saveDeviceClient(data);
            return BpmsHttpResult.success();
        } catch (BizError error) {
            log.error("系统异常",error);
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
        }
    }
}
