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
 * @since 2021-02-02
 */
@Getter
@Setter
@TableName("sl_device_data_item_record")
public class SlDeviceDataItemRecord implements Serializable {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 单机ID
     */
    private Integer deviceId;

    /**
     * 数据记录ID
     */
    private Integer recordId;

    /**
     * 记录项
     */
    private String dataItem;

    /**
     * 记录单位
     */
    private String dataUnit;

    /**
     * 记录值
     */
    private String dataVal;
}
