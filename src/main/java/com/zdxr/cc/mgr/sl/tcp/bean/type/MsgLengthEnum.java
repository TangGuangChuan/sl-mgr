package com.zdxr.cc.mgr.sl.tcp.bean.type;

public enum MsgLengthEnum {
	TYPE_LENGTH(2),//消息类型长度
	SEQ_LENGTH(4),//消息序号长度
	MSG_LENGTH(4);//消息内容长度
	
	private int length;
	
	public int getLength() {
		return length;
	}

	private MsgLengthEnum(int length) {
		this.length = length;
	}
}
