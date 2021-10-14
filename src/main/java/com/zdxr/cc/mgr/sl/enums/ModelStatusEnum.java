package com.zdxr.cc.mgr.sl.enums.base;


import lombok.Getter;

/**
 * 技术状态类型
 */
@Getter
public enum ModelStatusEnum implements BaseEnum {
    DOING("DOING", "进行中"),
    COMPLETE("COMPLETE", "已完成");

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
    private ModelStatusEnum(String code, String message) {
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
