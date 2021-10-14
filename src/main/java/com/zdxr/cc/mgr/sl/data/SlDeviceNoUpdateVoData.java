package com.zdxr.cc.mgr.sl.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel
public class SlDeviceNoUpdateVoData {
    @ApiModelProperty("更新数据")
    private List<SlDeviceNoVoData> updateList;
}
