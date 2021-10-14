package com.zdxr.cc.mgr.sl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdxr.cc.mgr.sl.enums.YNStatusEnum;
import com.zdxr.cc.mgr.sl.enums.base.ModelStatusEnum;
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
@TableName("sl_device")
public class SlDevice implements Serializable {


    /**
     * 主键
     */
    private Integer id;

    /**
     * 型号名称
     */
    private String modelName;

    /**
     * 型号简介
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
     * 系统名称
     */
    private String sysName;

    /**
     * 分系统名称
     */
    private String sysSubName;

    /**
     * 下发状态:(Y,是),(N,否)
     */
    private YNStatusEnum sendStatus;

    /**
     * 状态:(DOING,进行中),(COMPLETE,已完成)
     */
    private ModelStatusEnum status;

    /**
     * 试验结果是否上传:(Y,是),(N,否)
     */
    private YNStatusEnum compUpload;
}
