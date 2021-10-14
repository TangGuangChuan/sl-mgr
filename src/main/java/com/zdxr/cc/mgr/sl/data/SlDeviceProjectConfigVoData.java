package com.zdxr.cc.mgr.sl.data;

import com.zdxr.cc.mgr.sl.enums.ModelStateEnum;
import com.zdxr.cc.mgr.sl.enums.YNStatusEnum;
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
public class SlDeviceProjectConfigVoData implements Serializable {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "设备ID")
    private Integer deviceId;

    @ApiModelProperty("型号批次名称")
    private String modelName;

    @ApiModelProperty("分类")
    private String classify;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty(value = "项目阶段(ZPHJ, 装配环节),(ZJCS, 整机测试),(YSSY, 验收试验),(LXSY, 例行试验)")
    private ModelStateEnum modelState;

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

    @ApiModelProperty(value = "数据记录:(Y,是),(N,否)")
    private YNStatusEnum data;

    @ApiModelProperty(value = "记录试验条件:(Y,是),(N,否)")
    private YNStatusEnum record;

    @ApiModelProperty(value = "多媒体记录:(Y,是),(N,否)")
    private YNStatusEnum media;

    @ApiModelProperty("数据记录事项")
    private List<SlDeviceDataItemConfigVoData> itemList;
}
