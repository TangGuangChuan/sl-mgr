package com.zdxr.cc.mgr.sl.controller;

import com.zdxr.cc.mgr.sl.common.BizError;
import com.zdxr.cc.mgr.sl.common.BpmsHttpResult;
import com.zdxr.cc.mgr.sl.data.ModelInvoVoData;
import com.zdxr.cc.mgr.sl.data.SlDeviceInfoVoData;
import com.zdxr.cc.mgr.sl.data.SlDeviceNoUpdateVoData;
import com.zdxr.cc.mgr.sl.data.SlDeviceNoVoData;
import com.zdxr.cc.mgr.sl.enums.base.ModelStatusEnum;
import com.zdxr.cc.mgr.sl.service.ISlDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "/dlapi/device")
@Api(tags = "型号批次相关")
public class DeviceController {
    @Autowired
    private ISlDeviceService service;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @PostMapping("insert")
    @ApiOperation(value = "新增")
    public BpmsHttpResult insert(@RequestBody SlDeviceInfoVoData data) {
        try {
            service.insert(data);
            return BpmsHttpResult.success();
        } catch (BizError error) {
            log.error("系统异常",error);
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
        }
    }

    @PostMapping("update")
    @ApiOperation(value = "修改")
    public BpmsHttpResult update(@RequestBody SlDeviceInfoVoData data) {
        try {
            service.update(data);
            return BpmsHttpResult.success();
        } catch (BizError error) {
            log.error("系统异常",error);
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
        }
    }

    @GetMapping("deleteById")
    @ApiOperation(value = "删除")
    public BpmsHttpResult deleteById(@ApiParam(value = "主键ID", name = "id") Integer id) {
        try {
            service.deleteById(id);
            return BpmsHttpResult.success();
        } catch (BizError error) {
            log.error("系统异常",error);
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
        }
    }

    @GetMapping("selectDeviceNoList")
    @ApiOperation("查询设备编号")
    public BpmsHttpResult<List<SlDeviceNoVoData>> selectDeviceNoList(@RequestParam @ApiParam(value = "单机ID", name = "deviceId") Integer deviceId) {
        try {
            List<SlDeviceNoVoData> voData = service.selectDeviceNoList(deviceId);
            return BpmsHttpResult.success(voData);
        } catch (BizError error) {
            log.error("系统异常",error);
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
        }
    }

    @PostMapping("updateDeviceNo")
    @ApiOperation("设置设备编号")
    public BpmsHttpResult updateDeviceNo(@RequestBody SlDeviceNoUpdateVoData voData) {
        try {
            service.updateDeviceNo(voData);
            return BpmsHttpResult.success(voData);
        } catch (BizError error) {
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
        }
    }

    @GetMapping("findList")
    @ApiOperation(value = "型号批次总览")
    public BpmsHttpResult<ModelInvoVoData> findList(@RequestParam(required = false) @ApiParam(value = "完成状态", name = "status") ModelStatusEnum status,
                                                    @RequestParam(required = false) @ApiParam(value = "型号名称/设备名称", name = "keyWords") String keyWords,
                                                    @RequestParam @ApiParam(value = "当前页", name = "pageNum") Integer pageNum,
                                                    @RequestParam @ApiParam(value = "一页条数", name = "pageSize") Integer pageSize) {

        try {
            ModelInvoVoData voData = service.selectList(status, keyWords, pageNum, pageSize);
            return BpmsHttpResult.success(voData);
        } catch (BizError error) {
            log.error("系统异常",error);
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
        }
    }

    @GetMapping("reportZip")
    @ApiOperation("下载设备zip")
    public BpmsHttpResult reportZip(@ApiParam(value = "设备id", name = "deviceId") Integer deviceId, HttpServletResponse response) {
        try {
            service.reportZip(deviceId,response);
            return BpmsHttpResult.success();
        } catch (BizError error) {
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
        }
    }
}
