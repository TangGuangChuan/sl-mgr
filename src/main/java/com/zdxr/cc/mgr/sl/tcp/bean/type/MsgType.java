package com.zdxr.cc.mgr.sl.tcp.bean.type;

public enum MsgType {
	RESERVATION(0x00), REGISTER(0x01), REGISTER_RESP(0x02), HEARTBEAT(0x11), HEARTBEAT_RESP(0x12), OFFLINE(0x13),
	OFFLINE_RESP(0x14), PUSH(0x21), PUSH_RESP(0x22), ROBBINGORDER(0x31), ROBBINGORDER_RESP(0x32), ORDER(0x33),
	ORDER_RESP(0x34), ERROR(0xFF);
	
	private int code;

	private MsgType(int code) {
		this.code = code;
	}

	public int getValue() {
		return this.code;
	}

	public static MsgType convert(int code) {

		if (code < 0 && code > 0xFF) {
			return ERROR;
		}

		MsgType[] values = MsgType.values();
		for (MsgType msgType : values) {
			if (msgType.getValue() == code) {
				return msgType;
			}
		}
		return ERROR;
	}

}
