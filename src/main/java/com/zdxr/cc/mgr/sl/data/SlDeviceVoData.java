package com.zdxr.cc.mgr.sl.data;

import com.zdxr.cc.mgr.sl.enums.YNStatusEnum;
import com.zdxr.cc.mgr.sl.enums.base.ModelStatusEnum;
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
 * @since 2020-12-24
 */
@Getter
@Setter
@ApiModel
public class SlDeviceVoData implements Serializable {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "型号名称")
    private String modelName;

    @ApiModelProperty("型号简介")
    private String modelDesc;

    @ApiModelProperty("分类")
    private String classify;

    @ApiModelProperty("设备代号")
    private String deviceCode;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "设备数量")
    private Integer deviceNum;

    @ApiModelProperty(value = "系统名称")
    private String sysName;

    @ApiModelProperty(value = "分系统名称")
    private String sysSubName;

    @ApiModelProperty("下发状态:(Y,是),(N,否)")
    private YNStatusEnum sendStatus;

    @ApiModelProperty(value = "状态:(DOING,进行中),(COMPLETE,已完成)")
    private ModelStatusEnum status;

    @ApiModelProperty("试验结果是否上传:(Y,是),(N,否)")
    private YNStatusEnum compUpload;
}
