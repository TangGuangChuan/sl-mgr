package com.zdxr.cc.mgr.sl.tcp.util;


import com.zdxr.cc.mgr.sl.tcp.bean.Message;

public class MsgBuilder {
	public static Message buildResponseMsg(Message message) {
		Message repMessage = new Message();
		repMessage.setTypeInt(message.getTypeInt() + 1);
		repMessage.setSeqNo(message.getSeqNo());
		repMessage.setByteLength(0);
		return repMessage;
	}
}
