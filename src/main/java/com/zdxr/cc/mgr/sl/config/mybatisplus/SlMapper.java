package com.zdxr.cc.mgr.sl.config.mybatisplus;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 基础 mapper add by denglw
 * @param <T>
 */
public interface SlMapper<T> extends BaseMapper<T> {
    void insertBatch(@Param("list") List<T> list);
}
