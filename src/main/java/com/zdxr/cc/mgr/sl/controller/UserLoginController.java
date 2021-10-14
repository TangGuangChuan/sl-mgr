package com.zdxr.cc.mgr.sl.controller;

import com.zdxr.cc.mgr.sl.common.BizError;
import com.zdxr.cc.mgr.sl.common.BpmsHttpResult;
import com.zdxr.cc.mgr.sl.data.SlUserLoginVoData;
import com.zdxr.cc.mgr.sl.data.SlUserModifyVoData;
import com.zdxr.cc.mgr.sl.data.SlUserRegisterVoData;
import com.zdxr.cc.mgr.sl.entity.SlUser;
import com.zdxr.cc.mgr.sl.service.ISlUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/dlapi/user")
@Api(tags = "用户相关")
public class UserLoginController {
    @Autowired
    private ISlUserService service;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @PostMapping("register")
    @ApiOperation(value = "注册")
    public BpmsHttpResult register(@RequestBody SlUserRegisterVoData data) {
        try {
            service.registerUser(data);
            return BpmsHttpResult.success();
        } catch (BizError error) {
            log.error("系统异常",error);
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
        }
    }

    @PostMapping("login")
    @ApiOperation(value = "登录", notes = "返回data是用户名")
    public BpmsHttpResult login(@RequestBody SlUserLoginVoData data, HttpServletRequest request) {
        try {
            SlUser slUser = service.login(data);
            request.getSession().setAttribute("user", slUser);
            return BpmsHttpResult.success(slUser.getUserName());
        } catch (BizError error) {
            log.error("系统异常",error);
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
        }
    }

    @PostMapping("modifyPassword")
    @ApiOperation(value = "修改密码")
    public BpmsHttpResult modifyPassword(@RequestBody SlUserModifyVoData data, HttpServletRequest request) {
        try {
            SlUser user = (SlUser) request.getSession().getAttribute("user");
            if (user == null) {
                return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, "未登录");
            }
            SlUser slUser = service.modify(data, user.getId());
            request.getSession().setAttribute("user", slUser);
            return BpmsHttpResult.success();
        } catch (BizError error) {
            log.error("系统异常",error);
            return BpmsHttpResult.error(BpmsHttpResult.BASE_ERROR_CODE, error.getMessage());
        }
    }
}
