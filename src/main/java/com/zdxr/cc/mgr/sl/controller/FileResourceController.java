package com.zdxr.cc.mgr.sl.controller;

import com.zdxr.cc.mgr.sl.common.BpmsHttpResult;
import com.zdxr.cc.mgr.sl.service.ISlFileResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/dlapi/file_resource")
@Api(tags = "文件资源管理")
public class FileResourceController {
    private static final Logger log = LoggerFactory.getLogger(FileResourceController.class);
    @Resource
    ISlFileResourceService service;
    @PostMapping("upload")
    @ApiOperation("上传压缩文件")
    public BpmsHttpResult upload(@RequestParam @ApiParam(value = "压缩文件", name = "file") MultipartFile file){
        service.upload(file.getOriginalFilename(),file);
        return BpmsHttpResult.success();
    }
}
