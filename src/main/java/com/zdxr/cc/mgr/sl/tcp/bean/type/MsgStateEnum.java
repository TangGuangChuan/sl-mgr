package com.zdxr.cc.mgr.sl.tcp.bean.type;

//记录解码状态的状态机枚举类
public enum MsgStateEnum {
	STEP_TYPE(1),//解析类型
	STEP_SEQ(2),//解析序号
	STEP_LENGTH(3),//解析消息长度
	STEP_CONTENT(4);//解析消息内容
	
	private int code;
	
	private  MsgStateEnum(int code) {
		this.code=code;
	}
	
	public int getValue() {
		return this.code;
	}
}
