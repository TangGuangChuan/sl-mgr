package com.zdxr.cc.mgr.sl.tcp.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
	public static Gson getGosn() {
		return new GsonBuilder().serializeNulls().create();
	}
}
