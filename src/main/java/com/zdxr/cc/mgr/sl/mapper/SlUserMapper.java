package com.zdxr.cc.mgr.sl.mapper;

import com.zdxr.cc.mgr.sl.config.mybatisplus.SlMapper;
import com.zdxr.cc.mgr.sl.entity.SlUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SlUserMapper extends SlMapper<SlUser> {

    @Select("select * from sl_user where user_name = #{userName} and password = #{password}")
    SlUser login(@Param("userName")String userName,@Param("password") String password);
}
