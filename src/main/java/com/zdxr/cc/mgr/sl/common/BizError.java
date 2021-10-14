package com.zdxr.cc.mgr.sl.common;

public class BizError extends RuntimeException {
    private String code;

    public BizError() {
        super();
    }

    /**
     * 构建一个<code>BizError.java</code>
     * msg为自定义信息
     * 此方法适用于 直接抛出自己定义的信息
     *
     * @param msg
     */
    public BizError(String msg) {
        super(msg);
    }

    public BizError(String message, String code) {
        super(message);
        this.code = code;
    }

    public BizError(String message, Throwable cause) {
        super(message, cause);
    }

    public BizError(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
