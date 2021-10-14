package com.zdxr.cc.mgr.sl.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zdxr.cc.mgr.sl.enums.YNStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 文档配置
 * </p>
 *
 * @author denglw
 * @since 2020-12-25
 */
@Getter
@Setter
@ApiModel
public class SlDeviceDocRecordVoData implements Serializable {

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "型号批次ID")
    private Integer deviceId;

    @ApiModelProperty(value = "型号批次名称")
    private String modelName;

    @ApiModelProperty("型号批次简介")
    private String modelDesc;

    @ApiModelProperty(value = "分类")
    private String classify;

    @ApiModelProperty("设备代号")
    private String deviceCode;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "设备数量")
    private Integer deviceNum;

    @ApiModelProperty(value = "检查项目")
    private String checkProject;

    @ApiModelProperty(value = "检查方法")
    private String checkMethod;

    @ApiModelProperty(value = "合格判断")
    private String testJudge;

    @ApiModelProperty(value = "上传附件:(Y,是),(N,否)")
    private YNStatusEnum attach;

    @ApiModelProperty(value = "上传附件ID集合 逗号分割")
    private String attachIds;

    @ApiModelProperty(value = "多媒体记录:(Y,是),(N,否)")
    private YNStatusEnum media;

    @ApiModelProperty(value = "多媒体记录附件ID集合 逗号分割")
    private String mediaIds;

    @ApiModelProperty(value = "是否合格:(Y,是),(N,否)")
    private YNStatusEnum qualified;

    @ApiModelProperty(value = "签名姓名")
    private String signName;

    @ApiModelProperty(value = "签名图片附件ID")
    private String signId;

    @ApiModelProperty(value = "签名时间")
    private Date signDate;

    @ApiModelProperty("操作终端")
    private String operatorClient;

    @ApiModelProperty("下发状态:(Y,是),(N,否)")
    private YNStatusEnum sendStatus;

    @ApiModelProperty("下发时间")
    private Date sendDate;
}
