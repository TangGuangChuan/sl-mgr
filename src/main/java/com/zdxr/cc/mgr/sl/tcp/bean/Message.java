package com.zdxr.cc.mgr.sl.tcp.bean;

import com.zdxr.cc.mgr.sl.tcp.bean.type.MsgType;
import com.zdxr.cc.mgr.sl.tcp.util.GsonUtil;

import java.io.Serializable;
import java.util.Arrays;

/**
 * socket消息对象
 *
 */
public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6191383383309387389L;
	private MsgType type;// 类型2字节
	private int seqNo; // 自定义ID 4字节
	private int byteLength = 0;// 消息内容长度 4字节(指的是byteContent的长度，而不是content的长度)
	private String content;// 消息内容
	//private Object cntObject;//解析后的消息内容
	private transient byte[] byteContent;//消息内容字节对象
	private transient byte[] tempContent;

	public MsgType getType() {
		return type;
	}

	public void setType(MsgType type) {
		this.type = type;
	}
	
	public void setTypeInt(int type) {
		this.type = MsgType.convert(type);
	}
	
	public int getTypeInt() {
		return this.type.getValue();
	}
	

	public int getByteLength() {
		return this.byteLength;
	}

	public void setByteLength(int byteLength) {
		this.byteLength = byteLength;
	}

	public byte[] getTempContent() {
		if (tempContent == null) {
			return new byte[0];
		}
		return tempContent;
	}
	
	public void setTempContent(byte[] tempContent) {
		this.tempContent = tempContent;
	}

	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}

	public byte[] getByteContent() {
		return byteContent;
	}
	
	public void setByteContent(byte[] byteContent) {
		this.byteContent = byteContent;
		this.byteLength = this.byteContent.length;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public void setContent(Object content) {
		this.content = GsonUtil.getGosn().toJson(content);
	}

	@Override
	public String toString() {
		return "Message [type=" + type + ", seqNo=" + seqNo + ", byteLength=" + byteLength + ", content=" + content
				+ ", byteContent=" + Arrays.toString(byteContent) + "]";
	}

	/*
	 * public Object getCntObject() { return cntObject; }
	 * 
	 * public void setCntObject(Object cntObject) { this.cntObject = cntObject; }
	 */
}
