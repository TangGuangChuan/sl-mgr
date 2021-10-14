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
@TableName("sl_device_project_config")
public class SlDeviceProjectConfig implements Serializable {

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
     * 项目阶段(ZPHJ, 装配环节),(ZJCS, 整机测试),(YSSY, 验收试验),(LXSY, 例行试验)
     */
    private ModelStateEnum modelState;

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
     * 数据记录:(Y,是),(N,否)
     */
    private YNStatusEnum data;

    /**
     * 记录试验条件:(Y,是),(N,否)
     */
    private YNStatusEnum record;

    /**
     * 多媒体记录:(Y,是),(N,否)
     */
    private YNStatusEnum media;


    /**
     * 数据记录项目
     */
    @TableField(exist = false)
    private List<SlDeviceDataItemConfig> itemConfigList;
}
