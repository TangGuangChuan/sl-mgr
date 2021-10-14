package com.zdxr.cc.mgr.sl.tcp.util;

import java.util.concurrent.atomic.AtomicInteger;


public class SeqNoUtil {
	private static AtomicInteger no = new AtomicInteger();
	
	public static int getSeqNo() {
		return no.getAndAdd(1);
	}
}
