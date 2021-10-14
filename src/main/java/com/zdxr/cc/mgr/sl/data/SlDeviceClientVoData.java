package com.zdxr.cc.mgr.sl.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 客户端信息
 * </p>
 *
 * @author denglw
 * @since 2021-01-13
 */
@Getter
@Setter
@ApiModel
public class SlDeviceClientVoData implements Serializable {

    @ApiModelProperty("操作端编号")
    private String clientCode;

    @ApiModelProperty("操作端名称")
    private String clientName;
}
