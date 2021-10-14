package com.zdxr.cc.mgr.sl.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel
public class SlAttachUpLoadVoData implements Serializable {
    @ApiModelProperty("附件ID")
    private String attachId;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("文件地址")
    private String fileUrl;
}
