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
@ApiModel(value = "SlDeviceDataItemConfig对象", description = "")
public class SlDeviceDataItemConfigVoData implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty("单机ID")
    private Integer deviceId;

    @ApiModelProperty(value = "配置id")
    private Integer configId;

    @ApiModelProperty(value = "数据记录项")
    private String dataItem;

    @ApiModelProperty(value = "数据记录单位")
    private String dataUnit;
}
