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
@TableName("sl_device_data_item_config")
public class SlDeviceDataItemConfig implements Serializable {

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
     * 配置id
     */
    private Integer configId;

    /**
     * 数据记录项
     */
    private String dataItem;

    /**
     * 数据记录单位
     */
    private String dataUnit;
}
