package com.zdxr.cc.mgr.sl.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel
public class BpmsHttpResult<T> implements Serializable {

    public static final int BASE_SUCCESS_CODE = 200;
    public static final String BASE_SUCCESS_INFO = "请求执行成功";
    public static final int BASE_ERROR_CODE = -1;
    public static final String BASE_ERROR_INFO = "请求执行失败";

    @ApiModelProperty("返回编码")
    private Integer code;

    @ApiModelProperty("返回消息")
    private String msg;

    @ApiModelProperty("返回数据")
    private T data;

    public BpmsHttpResult() {
        super();
    }

    public BpmsHttpResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private static String getInfo(int code) {
        switch (code) {
            case BASE_SUCCESS_CODE:
                return BASE_SUCCESS_INFO;
            case BASE_ERROR_CODE:
                return BASE_ERROR_INFO;
            default:
                return null;
        }
    }

    /**
     * 成功且带数据
     **/
    public static <T> BpmsHttpResult success(T t) {
        BpmsHttpResult result = new BpmsHttpResult();
        result.setCode(BASE_SUCCESS_CODE);
        result.setMsg(getInfo(BASE_SUCCESS_CODE));
        result.setData(t);
        return result;
    }

    /**
     * 成功但不带数据
     **/
    public static BpmsHttpResult success() {

        return success(null);
    }

    /**
     * 失败
     **/
    public static BpmsHttpResult error(Integer code, String message) {
        BpmsHttpResult result = new BpmsHttpResult();
        result.setCode(code);
        result.setMsg(message);
        return result;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

}
