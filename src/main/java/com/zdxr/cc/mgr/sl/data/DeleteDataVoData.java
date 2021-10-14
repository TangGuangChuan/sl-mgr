package com.zdxr.cc.mgr.sl.data;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel
public class DeleteDataVoData implements Serializable {
    /**
     * 下发分组ID
     */
    private Integer groupId;
    /**
     * 单机ID
     */
    private Integer deviceId;
    /**
     * 编号 多个逗号分割
     */
    private String deviceNos;
    /**
     * 阶段 多个逗号分割
     */
    private String modelStates;
}
