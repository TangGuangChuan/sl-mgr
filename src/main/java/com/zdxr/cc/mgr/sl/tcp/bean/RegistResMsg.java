package com.zdxr.cc.mgr.sl.tcp.bean;

public class RegistResMsg extends Base {
	
	public RegistResMsg() {
		
	}
	
	public RegistResMsg(String result) {
		this.result = result;
	}
	private String result;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
