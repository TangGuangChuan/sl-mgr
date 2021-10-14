package com.zdxr.cc.mgr.sl.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ApiModel
public class SlClientUploadVoData implements Serializable {
    @ApiModelProperty("操作端")
    private String clientName;
    @ApiModelProperty("试验项目记录")
    private List<SlDeviceProjectRecordVoData> projectRecords;
    @ApiModelProperty("文档记录")
    private List<SlDeviceDocRecordVoData> docRecords;
    @ApiModelProperty("附件记录")
    private List<SlAttachVoData> attachs;
}
