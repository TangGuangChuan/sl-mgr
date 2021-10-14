package com.zdxr.cc.mgr.sl.data;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel
public class ModelInvoVoData implements Serializable {
    @ApiModelProperty("全部数")
    private Integer allNum;

    @ApiModelProperty("已完成数")
    private Integer compNum;

    @ApiModelProperty("进行中数 ")
    private Integer doingNum;

    @ApiModelProperty("型号批次设备数据")
    private PageInfo<SlDeviceVoData> list;
}
