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
public class SlDeviceProjectConfigInfoVoData implements Serializable {
    @ApiModelProperty("设备ID")
    private Integer deviceId;
    @ApiModelProperty("型号批次名称")
    private String modelName;
    @ApiModelProperty("设备名称")
    private String deviceName;
    @ApiModelProperty("装配环节")
    private List<SlDeviceProjectConfigVoData> zphjList;
    @ApiModelProperty("整机测试")
    private List<SlDeviceProjectConfigVoData> zjcsList;
    @ApiModelProperty("验收试验")
    private List<SlDeviceProjectConfigVoData> yssyList;
    @ApiModelProperty("例行试验")
    private List<SlDeviceProjectConfigVoData> lxsyList;
}
