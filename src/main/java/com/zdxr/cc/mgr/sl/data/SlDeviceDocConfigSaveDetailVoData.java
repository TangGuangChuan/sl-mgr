package com.zdxr.cc.mgr.sl.data;

import com.zdxr.cc.mgr.sl.enums.YNStatusEnum;
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
public class SlDeviceDocConfigSaveDetailVoData implements Serializable {
    @ApiModelProperty(value = "检查项目")
    private String checkProject;

    @ApiModelProperty(value = "检查方法")
    private String checkMethod;

    @ApiModelProperty(value = "合格判断")
    private String testJudge;

    @ApiModelProperty(value = "上传附件:(Y,是),(N,否)")
    private YNStatusEnum attach;

    @ApiModelProperty(value = "多媒体记录:(Y,是),(N,否)")
    private YNStatusEnum media;

}
