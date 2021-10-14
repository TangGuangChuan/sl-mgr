package com.zdxr.cc.mgr.sl.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author denglw
 * @since 2021-03-09
 */
@Getter
@Setter
@ApiModel(value = "SlDeviceNo对象", description = "")
public class SlDeviceNoVoData implements Serializable {


    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "设备ID")
    private Integer deviceId;

    @ApiModelProperty(value = "设备编号")
    private String deviceNo;
}
