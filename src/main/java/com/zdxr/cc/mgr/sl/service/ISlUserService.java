package com.zdxr.cc.mgr.sl.service;

import com.zdxr.cc.mgr.sl.data.SlUserLoginVoData;
import com.zdxr.cc.mgr.sl.data.SlUserModifyVoData;
import com.zdxr.cc.mgr.sl.data.SlUserRegisterVoData;
import com.zdxr.cc.mgr.sl.entity.SlUser;

public interface ISlUserService {
    /**
     * 注册用户
     *
     * @param voData
     * @return
     */
    SlUser registerUser(SlUserRegisterVoData voData);

    /**
     * 登录
     *
     * @param voData
     * @return
     */
    SlUser login(SlUserLoginVoData voData);

    /**
     * 修改密码
     *
     * @param voData
     * @return
     */
    SlUser modify(SlUserModifyVoData voData, Integer userId);
}
