package com.zdxr.cc.mgr.sl.common;

import java.util.HashMap;
import java.util.Map;

public class BpmsException extends Exception {

    public static final int DB_CREATE_FAIL = -1;
    public static final int DB_INSERT_FAIL = -2;
    public static final int DB_SELECT_FAIL = -3;
    public static final int DB_UPDATE_FAIL = -4;
    public static final int DB_DELETE_FAIL = -5;

    public static final int TASK_NAME_DUPLICATED = -101;
    public static final int TASK_NAME_NOT_EXIST = -102;
    public static final int PAGE_TYPE_ERROR = -103;

    private static Map<Integer, String> table = new HashMap<>();
    static{
        table.put(DB_CREATE_FAIL, "创建表失败");
        table.put(DB_INSERT_FAIL, "插入数据失败");
        table.put(DB_SELECT_FAIL, "查询数据失败");
        table.put(DB_UPDATE_FAIL, "更新数据失败");
        table.put(DB_DELETE_FAIL, "删除数据失败");
        table.put(TASK_NAME_DUPLICATED, "任务名已经重复");
        table.put(TASK_NAME_NOT_EXIST, "任务不存在");
        table.put(PAGE_TYPE_ERROR, "页面类型错误");
    }

    private int errCode;

    public BpmsException(int errCode) {
        this.errCode = errCode;
    }

    public String toString() {
        if(table.get(errCode) == null) {
            return "异常错误";
        }
        return table.get(errCode);
    }
}
