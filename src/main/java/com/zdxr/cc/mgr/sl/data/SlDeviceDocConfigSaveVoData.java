package com.zdxr.cc.mgr.sl.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author denglw
 * @since 2020-12-24
 */
@Getter
@Setter
@ApiModel
public class SlDeviceDocConfigSaveVoData implements Serializable {
    @ApiModelProperty(value = "设备ID")
    private Integer deviceId;

    @ApiModelProperty("文档检查项")
    private List<SlDeviceDocConfigSaveDetailVoData> list;
}
