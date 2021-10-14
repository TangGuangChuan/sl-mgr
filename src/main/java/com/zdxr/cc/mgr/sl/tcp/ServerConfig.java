package com.zdxr.cc.mgr.sl.tcp;

public class ServerConfig {
	public static final String CHARSET = "UTF-8";
	
	/**
	 *  允许最大连接数
	 */
	public static final int MAX_CONNECTION = 10000;
	
	/**
	 * 心跳信息检测时间间隔，单位：秒
	 */
	public static final int HEARTBEAT_CHECK_TIME = 120;
	
}
