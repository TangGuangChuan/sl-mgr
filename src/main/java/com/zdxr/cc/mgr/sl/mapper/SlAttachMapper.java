package com.zdxr.cc.mgr.sl.mapper;

import com.zdxr.cc.mgr.sl.config.mybatisplus.SlMapper;
import com.zdxr.cc.mgr.sl.entity.SlAttach;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SlAttachMapper extends SlMapper<SlAttach> {

    @Select("select * from sl_attach where attach_id = #{attachId}")
    SlAttach selectByAttachId(@Param("attachId") String attachId);

    @Select("select * from sl_attach where attach_id in (${attachIds})")
    List<SlAttach> selectByAttachIds(@Param("attachIds") String attachIds);
}
