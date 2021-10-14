package com.zdxr.cc.mgr.sl.data;

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
 * @since 2020-12-25
 */
@Getter
@Setter
@ApiModel
public class SlDeviceSendGroupInsertVoData implements Serializable {

    @ApiModelProperty(value = "单机ID")
    private Integer deviceId;

    @ApiModelProperty(value = "设备编号 逗号分割")
    private String deviceNo;

    @ApiModelProperty(value = "阶段过程 空代表全部,(ZPHJ, 装配环节),(ZJCS, 整机测试),(YSSY, 验收试验),(LXSY, 例行试验)")
    private String modelState;

    @ApiModelProperty(value = "操作终端")
    private String operatorClient;
}
