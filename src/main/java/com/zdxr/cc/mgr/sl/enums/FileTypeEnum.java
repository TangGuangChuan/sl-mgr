package com.zdxr.cc.mgr.sl.enums;


import com.zdxr.cc.mgr.sl.enums.base.BaseEnum;
import lombok.Getter;

/**
 * 支撑材料类型
 */
@Getter
public enum FileTypeEnum implements BaseEnum {
    UMT("userManualTemplate", "用户手册"),
    GM("GraphicModel", "三维图");

    /**
     * 枚举值码
     */
    private final String code;

    /**
     * 枚举描述
     */
    private final String message;

    /**
     * @param code    枚举值码。
     * @param message 枚举描述。
     */
    private FileTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 得到枚举值码。
     *
     * @return 枚举值码。
     */
    public String code() {
        return code;
    }

    /**
     * 得到枚举描述。
     *
     * @return 枚举描述。
     */
    public String message() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
