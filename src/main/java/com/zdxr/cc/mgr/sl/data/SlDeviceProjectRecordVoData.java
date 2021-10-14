package com.zdxr.cc.mgr.sl.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zdxr.cc.mgr.sl.enums.ModelStateEnum;
import com.zdxr.cc.mgr.sl.enums.YNStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 试验项目记录
 * </p>
 *
 * @author denglw
 * @since 2020-12-25
 */
@Getter
@Setter
@ApiModel
public class SlDeviceProjectRecordVoData implements Serializable {

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

    @ApiModelProperty(value = "系统名称")
    private String sysName;

    @ApiModelProperty(value = "分系统名称")
    private String sysSubName;

    @ApiModelProperty("设备代号")
    private String deviceCode;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "设备数量")
    private Integer deviceNum;

    @ApiModelProperty(value = "项目阶段(ZPHJ, 装配环节),(ZJCS, 整机测试),(YSSY, 验收试验),(LXSY, 例行试验)")
    private ModelStateEnum modelState;

    @ApiModelProperty(value = "设备编号")
    private String deviceNo;

    @ApiModelProperty(value = "试验项目")
    private String expProject;

    @ApiModelProperty(value = "试验条件")
    private String expCondition;

    @ApiModelProperty(value = "测试项目")
    private String testProject;

    @ApiModelProperty(value = "测试方法")
    private String testMethod;

    @ApiModelProperty(value = "合格判断")
    private String testJudge;

    @ApiModelProperty(value = "上传附件:(Y,是),(N,否)")
    private YNStatusEnum attach;

    @ApiModelProperty(value = "上传附件ID集合 逗号分割")
    private String attachIds;

    @ApiModelProperty(value = "数据记录:(Y,是),(N,否)")
    private YNStatusEnum data;

    @ApiModelProperty(value = "数据记录文本")
    private String dataTxt;

    @ApiModelProperty(value = "记录试验条件:(Y,是),(N,否)")
    private YNStatusEnum record;

    @ApiModelProperty(value = "记录试验条件文本")
    private String recordTxt;

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

    @ApiModelProperty(value = "操作终端")
    private String operatorClient;

    @ApiModelProperty(value = "下发状态:(Y,是),(N,否)")
    private YNStatusEnum sendStatus;

    @ApiModelProperty(value = "下发时间")
    private Date sendDate;

    @ApiModelProperty("数据记录项")
    private List<SlDeviceDataItemRecordVoData> itemRecordList;
}
