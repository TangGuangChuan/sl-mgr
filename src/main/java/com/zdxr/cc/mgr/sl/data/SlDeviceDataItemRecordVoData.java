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
 * @since 2021-02-02
 */
@Getter
@Setter
@ApiModel(value = "SlDeviceDataItemRecord对象", description = "")
public class SlDeviceDataItemRecordVoData implements Serializable {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty("单机ID")
    private Integer deviceId;

    @ApiModelProperty(value = "数据记录ID")
    private Integer recordId;

    @ApiModelProperty(value = "记录项")
    private String dataItem;

    @ApiModelProperty(value = "记录单位")
    private String dataUnit;

    @ApiModelProperty(value = "记录值")
    private String dataVal;
}
