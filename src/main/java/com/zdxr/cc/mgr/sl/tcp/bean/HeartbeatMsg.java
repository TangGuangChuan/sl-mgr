package com.zdxr.cc.mgr.sl.tcp.bean;

public class HeartbeatMsg extends Base {
	private byte b = 0x01;

	public byte getB() {
		return b;
	}

	public void setB(byte b) {
		this.b = b;
	}
	
}
