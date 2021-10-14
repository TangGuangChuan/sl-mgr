package com.zdxr.cc.mgr.sl.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zdxr.cc.mgr.sl.enums.GroupStatusEnum;
import com.zdxr.cc.mgr.sl.enums.YNStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author denglw
 * @since 2020-12-25
 */
@Getter
@Setter
@ApiModel
public class SlDeviceSendGroupVoData implements Serializable {

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "单机ID")
    private Integer deviceId;

    @ApiModelProperty(value = "设备编号 逗号分割")
    private String deviceNo;

    @ApiModelProperty(value = "阶段过程(ZPHJ, 装配环节),(ZJCS, 整机测试),(YSSY, 验收试验),(LXSY, 例行试验)")
    private String modelState;

    @ApiModelProperty(value = "操作终端")
    private String operatorClient;

    @ApiModelProperty(value = "下发状态:(Y,是),(N,否)")
    private YNStatusEnum sendStatus;

    @ApiModelProperty(value = "下发时间")
    private Date sendDate;

    @ApiModelProperty("执行情况(NOT,未开始),(DOING,进行中),(COMP,已完成)")
    private GroupStatusEnum status;
}
