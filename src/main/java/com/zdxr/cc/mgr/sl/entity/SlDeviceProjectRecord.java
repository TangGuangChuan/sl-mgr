package com.zdxr.cc.mgr.sl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zdxr.cc.mgr.sl.enums.ModelStateEnum;
import com.zdxr.cc.mgr.sl.enums.YNStatusEnum;
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
@TableName("sl_device_project_record")
public class SlDeviceProjectRecord implements Serializable {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 型号批次ID
     */
    private Integer deviceId;

    /**
     * 型号批次名称
     */
    private String modelName;

    /**
     * 型号批次简介
     */
    private String modelDesc;

    /**
     * 分类
     */
    private String classify;

    /**
     * 系统名称
     */
    private String sysName;

    /**
     * 分系统名称
     */
    private String sysSubName;

    /**
     * 设备代号
     */
    private String deviceCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备数量
     */
    private Integer deviceNum;

    /**
     * 项目阶段(ZPHJ, 装配环节),(ZJCS, 整机测试),(YSSY, 验收试验),(LXSY, 例行试验)
     */
    private ModelStateEnum modelState;

    /**
     * 设备编号
     */
    private String deviceNo;

    /**
     * 试验项目
     */
    private String expProject;

    /**
     * 试验条件
     */
    private String expCondition;

    /**
     * 测试项目
     */
    private String testProject;

    /**
     * 测试方法
     */
    private String testMethod;

    /**
     * 合格判断
     */
    private String testJudge;

    /**
     * 上传附件:(Y,是),(N,否)
     */
    private YNStatusEnum attach;

    /**
     * 上传附件ID集合 逗号分割
     */
    private String attachIds;

    /**
     * 数据记录:(Y,是),(N,否)
     */
    private YNStatusEnum data;

    /**
     * 数据记录文本
     */
    private String dataTxt;

    /**
     * 记录试验条件:(Y,是),(N,否)
     */
    private YNStatusEnum record;

    /**
     * 记录试验条件文本
     */
    private String recordTxt;

    /**
     * 多媒体记录:(Y,是),(N,否)
     */
    private YNStatusEnum media;

    /**
     * 多媒体记录附件ID集合 逗号分割
     */
    private String mediaIds;

    /**
     * 是否合格:(Y,是),(N,否)
     */
    private YNStatusEnum qualified;

    /**
     * 签名姓名
     */
    private String signName;

    /**
     * 签名图片附件ID
     */
    private String signId;

    /**
     * 签名时间
     */
    private Date signDate;

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
     * 数据记录项
     */
    @TableField(exist = false)
    private List<SlDeviceDataItemRecord> itemRecordList;
}
