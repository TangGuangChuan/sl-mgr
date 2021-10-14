package com.zdxr.cc.mgr.sl.mapper;

import com.zdxr.cc.mgr.sl.config.mybatisplus.SlMapper;
import com.zdxr.cc.mgr.sl.entity.SlAttach;
import com.zdxr.cc.mgr.sl.entity.SlFileResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SlFileResourceMapper extends SlMapper<SlFileResource> {

    @Select("select * from sl_attach where attach_id = #{attachId}")
    SlAttach selectByAttachId(@Param("attachId") String attachId);

    @Select("select * from sl_attach where attach_id in (${attachIds})")
    List<SlAttach> selectByAttachIds(@Param("attachIds") String attachIds);
}
