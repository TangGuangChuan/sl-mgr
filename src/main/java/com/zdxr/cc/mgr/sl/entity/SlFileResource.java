package com.zdxr.cc.mgr.sl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 文件资源管理
 * @author tangguangchuan
 * @date 2021/10/14 9:56
 */
@Getter
@Setter
@TableName("sl_file_resource")
public class SlFileResource {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文件ID
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
