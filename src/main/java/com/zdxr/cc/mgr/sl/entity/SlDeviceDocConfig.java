package com.zdxr.cc.mgr.sl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zdxr.cc.mgr.sl.enums.YNStatusEnum;
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
@TableName("sl_device_doc_config")
public class SlDeviceDocConfig implements Serializable {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 设备ID
     */
    private Integer deviceId;

    /**
     * 型号批次名称
     */
    private String modelName;

    /**
     * 分类
     */
    private String classify;

    /**
     * 设备名称
     */
    private String deviceName;

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
     * 多媒体记录:(Y,是),(N,否)
     */
    private YNStatusEnum media;
}
