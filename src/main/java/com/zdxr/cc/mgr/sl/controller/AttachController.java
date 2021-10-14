package com.zdxr.cc.mgr.sl.controller;

import com.zdxr.cc.mgr.sl.common.BpmsHttpResult;
import com.zdxr.cc.mgr.sl.data.SlAttachUpLoadVoData;
import com.zdxr.cc.mgr.sl.entity.SlUser;
import com.zdxr.cc.mgr.sl.service.ISlAttachService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import static com.zdxr.cc.mgr.sl.common.BpmsHttpResult.BASE_ERROR_CODE;

@RestController
@RequestMapping(value = "/dlapi/attach")
@Api(tags = "附件相关")
public class AttachController {
    private static final Logger log = LoggerFactory.getLogger(AttachController.class);
    @Autowired
    private ISlAttachService service;

    @PostMapping(value = "/upload")
    @ApiOperation(value = "上传文件")
    public BpmsHttpResult upload(@RequestParam @ApiParam(value = "文件", name = "file") MultipartFile file, HttpServletRequest request) {
        String fileName = file.getOriginalFilename();
        try {
            SlUser user = (SlUser) request.getSession().getAttribute("user");
            if (user == null)
                return new BpmsHttpResult(BASE_ERROR_CODE, "未登录", null);
            SlAttachUpLoadVoData voData = service.upload(fileName, user.getUserName(), file.getInputStream());
            return BpmsHttpResult.success(voData);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getStackTrace().toString());
            return new BpmsHttpResult(BASE_ERROR_CODE, e.getMessage(), null);
        }
    }


    @GetMapping(value = "/download")
    @ApiOperation(value = "下载文件")
    public BpmsHttpResult download(@RequestParam @ApiParam(value = "附件ID", name = "attachId") String attachId, HttpServletResponse response) {
        SlAttachUpLoadVoData voData = service.download(attachId);
        if (voData == null) {
            return new BpmsHttpResult(BASE_ERROR_CODE, "附件未上传", null);
        }
        try {
            response.setHeader("Content-Disposition", "attachment;Filename=" + URLEncoder.encode(voData.getFileName(), "UTF-8"));
            OutputStream os = response.getOutputStream();
            byte[] b = new byte[100];
            int len;
            FileInputStream fileInputStream = new FileInputStream(new File(voData.getFileUrl()));
            while ((len = fileInputStream.read(b)) > 0) {
                os.write(b, 0, len);
            }
            fileInputStream.close();
            return BpmsHttpResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getStackTrace().toString());
            return new BpmsHttpResult(BASE_ERROR_CODE, e.getMessage(), null);
        }
    }
}
