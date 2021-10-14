package com.zdxr.cc.mgr.sl.tcp.util;

import java.util.UUID;

public class UUIDUtil {
	public static String getUUid() {
		String uid = UUID.randomUUID().toString().replace("-", "");
		return uid;
	}
}
