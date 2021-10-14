package com.zdxr.cc.mgr.sl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author tangguangchuan
 * @version 1.0
 * @date 2021-10-14 22:12
 * 支撑材料
 */
@Getter
@Setter
@TableName("sl_support_material")
public class SlSupportMaterial {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联文件资源表的文件ID(一对多)
     */
    private String fileId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件后缀名
     */
    private String suffix;

    /**
     * 文件全路径地址
     */
    private String fileUrl;

    /**
     * 上传时间
     */
    private Date uploadTime;
}
