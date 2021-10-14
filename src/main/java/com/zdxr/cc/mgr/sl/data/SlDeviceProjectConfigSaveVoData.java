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
public class SlDeviceProjectConfigSaveVoData implements Serializable {
    @ApiModelProperty("设备ID")
    private Integer deviceId;
    @ApiModelProperty("装配环节")
    private List<SlDeviceProjectConfigSaveDetailVoData> zphjList;
    @ApiModelProperty("整机测试")
    private List<SlDeviceProjectConfigSaveDetailVoData> zjcsList;
    @ApiModelProperty("验收试验")
    private List<SlDeviceProjectConfigSaveDetailVoData> yssyList;
    @ApiModelProperty("例行试验")
    private List<SlDeviceProjectConfigSaveDetailVoData> lxsyList;
}
