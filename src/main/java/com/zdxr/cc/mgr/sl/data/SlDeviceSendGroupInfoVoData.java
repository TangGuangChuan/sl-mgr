package com.zdxr.cc.mgr.sl.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ApiModel
public class SlDeviceSendGroupInfoVoData implements Serializable {
    @ApiModelProperty("设备ID")
    private Integer deviceId;
    @ApiModelProperty("型号批次名称")
    private String modelName;
    @ApiModelProperty("设备名称")
    private String deviceName;
    @ApiModelProperty("设备数量")
    private Integer deviceNum;
    @ApiModelProperty("分组数量")
    private Integer count;
    @ApiModelProperty("分组情况")
    private List<SlDeviceSendGroupVoData> list;
}
