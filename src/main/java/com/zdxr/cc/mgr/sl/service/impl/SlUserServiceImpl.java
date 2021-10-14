package com.zdxr.cc.mgr.sl.service.impl;

import com.zdxr.cc.mgr.sl.common.BeanCopyUtil;
import com.zdxr.cc.mgr.sl.common.BizError;
import com.zdxr.cc.mgr.sl.data.SlUserLoginVoData;
import com.zdxr.cc.mgr.sl.data.SlUserModifyVoData;
import com.zdxr.cc.mgr.sl.data.SlUserRegisterVoData;
import com.zdxr.cc.mgr.sl.entity.SlUser;
import com.zdxr.cc.mgr.sl.mapper.SlUserMapper;
import com.zdxr.cc.mgr.sl.service.ISlUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SlUserServiceImpl implements ISlUserService {
    @Autowired
    private SlUserMapper mapper;

    @Override
    @Transactional
    public SlUser registerUser(SlUserRegisterVoData voData) {
        if (!voData.getPassword().equals(voData.getConfirmPassword())) {
            throw new BizError("密码不一致,请重新输入");
        }
        try {
            SlUser slUser = new SlUser();
            BeanCopyUtil.copy(voData, slUser);
            mapper.insert(slUser);
            return slUser;
        } catch (Exception e) {
            throw new BizError("用户名重复");
        }
    }

    @Override
    public SlUser login(SlUserLoginVoData voData) {
        SlUser slUser = mapper.login(voData.getUserName(), voData.getPassword());
        if (slUser == null) {
            throw new BizError("用户名或密码错误");
        }
        return slUser;
    }

    @Override
    @Transactional
    public SlUser modify(SlUserModifyVoData voData, Integer userId) {
        SlUser slUser = mapper.selectById(userId);
        if (slUser == null) {
            throw new BizError("当前用户未注册或未登录");
        }
        if (!voData.getNewPassword().equals(voData.getConfirmNewPassword())) {
            throw new BizError("密码不一致,请重新输入");
        }
        SlUser loginUser = mapper.login(slUser.getUserName(), voData.getPassword());
        if (loginUser == null) {
            throw new BizError("旧密码错误");
        }
        slUser.setPassword(voData.getNewPassword());
        mapper.updateById(slUser);
        return slUser;
    }
}
