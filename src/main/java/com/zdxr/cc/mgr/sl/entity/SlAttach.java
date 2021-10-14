package com.zdxr.cc.mgr.sl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 附件
 * </p>
 *
 * @author denglw
 * @since 2020-12-25
 */
@Getter
@Setter
@TableName("sl_attach")
public class SlAttach implements Serializable {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 附件ID
     */
    private String attachId;

    /**
     * 附件名称
     */
    private String fileName;

    /**
     * 后缀名
     */
    private String suffix;

    /**
     * 全路径地址
     */
    private String fileUrl;

    /**
     * 操作人
     */
    private String operatorName;

    /**
     * 操作时间
     */
    private Date operatorTime;
}
