package com.zdxr.cc.mgr.sl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zdxr.cc.mgr.sl.enums.YNStatusEnum;
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
@TableName("sl_device_doc_record")
public class SlDeviceDocRecord implements Serializable {

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
     * 检查项目
     */
    private String checkProject;

    /**
     * 检查方法
     */
    private String checkMethod;

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
}
