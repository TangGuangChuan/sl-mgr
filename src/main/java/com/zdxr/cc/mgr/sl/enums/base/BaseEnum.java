package com.zdxr.cc.mgr.sl.enums.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 父类枚举，序列化以对象形式
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@ApiModel
public interface BaseEnum {

    @ApiModelProperty("编号")
    String code();

    @ApiModelProperty("名称")
    String message();
}
