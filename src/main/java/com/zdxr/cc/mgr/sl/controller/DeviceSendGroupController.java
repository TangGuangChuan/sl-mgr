package com.zdxr.cc.mgr.sl.controller;

import com.google.gson.Gson;
import com.zdxr.cc.mgr.sl.common.BizError;
import com.zdxr.cc.mgr.sl.common.BpmsHttpResult;
import com.zdxr.cc.mgr.sl.data.*;
import com.zdxr.cc.mgr.sl.service.ISlDeviceSendGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/dlapi/device_send_group")
@Api(tags = "任务下发相关")
public class DeviceSendGroupController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ISlDeviceSendGroupService service;

    @PostMapping("insert")
    @ApiOperation(value = "新增")
    public BpmsHttpResult insert(@RequestBody SlDeviceSendGroupInsertVoData data) {
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
    public BpmsHttpResult update(@RequestBody SlDeviceSendGroupUpdateVoData data) {
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
    public BpmsHttpResult deleteById(@RequestParam @ApiParam(value = "主键ID", name = "id") Integer id) {
        try {
            service.deleteById(id);
            return BpmsHttpResult.success();
        } catch (BizError error) {
            log.error("系统异常",error);
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
        }
    }

    @PostMapping("deleteSendDataById")
    @ApiOperation(value = "删除下发数据", notes = "返回data数据为是否提示确认删除")
    public BpmsHttpResult<Boolean> deleteSendDataById(@RequestBody SendGroupDeleteVoData voData) {
        try {
            Boolean deleteConfirm = service.deleteSendDataById(voData);
            return BpmsHttpResult.success(deleteConfirm);
        } catch (Exception e) {
            log.error("系统异常", e);
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, e.getMessage());
        }
    }

    @GetMapping("selectList")
    @ApiOperation("查询下发分组情况")
    public BpmsHttpResult<SlDeviceSendGroupInfoVoData> selectList(@RequestParam @ApiParam(value = "设备ID") Integer deviceId) {
        try {
            SlDeviceSendGroupInfoVoData list = service.selectList(deviceId);
            return BpmsHttpResult.success(list);
        } catch (BizError error) {
            log.error("系统异常",error);
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
        }
    }

    @GetMapping("sendGroup")
    @ApiOperation("下发")
    public BpmsHttpResult sendGroup(@RequestParam @ApiParam(value = "分组ID集合 逗号分割") String groupIds) {
        try {
            service.sendGroup(groupIds);
            return BpmsHttpResult.success();
        } catch (BizError error) {
            log.error("系统异常",error);
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
        }
    }

    @PostMapping("clientUpload")
    @ApiOperation("操作端数据上传")
    public BpmsHttpResult clientUpload(@RequestParam(value = "files", required = false) MultipartFile[] files, @RequestParam("json") String json) {
        synchronized (json.intern()) {
            log.info("client上传发送过来的数据:" + json);
            if (files != null) {
                log.info("client上传发送过来的数据:" + files.length);
            }
            try {
                if (StringUtils.isBlank(json)) {
                    return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, "上传数据不能为空");
                } else {
                    Gson gson = new Gson();
                    SlClientUploadVoData voData = gson.fromJson(json, SlClientUploadVoData.class);
                    if (voData == null) {
                        return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, "上传数据格式不正确");
                    } else {
                        service.clientUpload(files, voData);
                    }
                }
                return BpmsHttpResult.success();
            } catch (BizError error) {
                log.error("系统异常",error);
                return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
            }
        }
    }

    @GetMapping("selectOperatorClient")
    @ApiOperation("查看当前设备连接")
    public BpmsHttpResult<List<String>> selectOperatorClient() {
        try {
            List<String> list = service.selectOperatorClient();
            return BpmsHttpResult.success(list);
        } catch (BizError error) {
            log.error("系统异常",error);
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
        }
    }
}
