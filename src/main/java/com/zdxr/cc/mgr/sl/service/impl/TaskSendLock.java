package com.zdxr.cc.mgr.sl.service.impl;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskSendLock {
    private static final Map<String, String> taskStatusMap = new ConcurrentHashMap<>();

    public static int isOver(String groupId, int timeOut){
        long begin = new Date().getTime();

        while(true){
           if("OVER".equals(taskStatusMap.get(groupId))){
               taskStatusMap.remove(groupId);
               return 1;
           }
           try {
               Thread.sleep(500);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           long current = new Date().getTime();
           if(current - begin >= timeOut){
               return -1;
           }
        }
    }

    public static void over(String groupId){
        try{
            taskStatusMap.put(groupId, "OVER");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
