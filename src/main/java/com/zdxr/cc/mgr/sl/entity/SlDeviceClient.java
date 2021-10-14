package com.zdxr.cc.mgr.sl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author denglw
 * @since 2021-01-13
 */
@Getter
@Setter
@TableName("sl_device_client")
public class SlDeviceClient implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 操作端编号
     */
    private String clientCode;

    /**
     * 操作端名称
     */
    private String clientName;
}
