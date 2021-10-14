package com.zdxr.cc.mgr.sl.enums;


import com.zdxr.cc.mgr.sl.enums.base.BaseEnum;
import lombok.Getter;

/**
 * 技术状态类型
 */
@Getter
public enum ModelStateEnum implements BaseEnum {
    ZPHJ("ZPHJ", "装配环节"),
    ZJCS("ZJCS", "整机测试"),
    YSSY("YSSY", "验收试验"),
    LXSY("LXSY", "例行试验"),
    DOC("DOC", "文档检查");

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
    private ModelStateEnum(String code, String message) {
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
