package com.zdxr.cc.mgr.sl.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel
public class SendGroupDeleteVoData implements Serializable {
    /**
     * 任务分组ID
     */
    @ApiModelProperty("任务分组ID")
    private Integer groupId;
    /**
     * 是否确认删除
     */
    @ApiModelProperty("是否确认删除")
    private Boolean isConfirm;
}
