package com.zdxr.cc.mgr.sl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zdxr.cc.mgr.sl.enums.GroupStatusEnum;
import com.zdxr.cc.mgr.sl.enums.YNStatusEnum;
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
@TableName("sl_device_send_group")
public class SlDeviceSendGroup implements Serializable {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 单机ID
     */
    private Integer deviceId;

    /**
     * 设备编号 逗号分割
     */
    private String deviceNo;

    /**
     * 阶段过程(ZPHJ, 装配环节),(ZJCS, 整机测试),(YSSY, 验收试验),(LXSY, 例行试验)
     */
    private String modelState;

    /**
     * 操作端编码
     */
    private String clientCode;

    /**
     * 操作终端
     */
    private String operatorClient;

    /**
     * 下发状态:(Y,是),(N,否)
     */
    private YNStatusEnum sendStatus;

    /**
     * 下发时间
     */
    private Date sendDate;

    /**
     * 执行情况(NOT,未开始),(DOING,进行中),(COMP,已完成)
     */
    private GroupStatusEnum status;
}
