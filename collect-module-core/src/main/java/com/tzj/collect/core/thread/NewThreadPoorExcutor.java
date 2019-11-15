package com.tzj.collect.core.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NewThreadPoorExcutor {
    //核心线程数
    private static int corePoolSize=3;
    //最大线程数
    private static int maximumPoolSize=3;
    //等待时间
    private static long keepAliveTime=5;

    static ThreadPoolExecutor threadPoolExecutor=null;

    public NewThreadPoorExcutor(){

    }
    public static ThreadPoolExecutor getThreadPoor(){
        if (null==threadPoolExecutor){
            ThreadPoolExecutor t;
            synchronized (ThreadPoolExecutor.class){
                t = threadPoolExecutor;
                if (null==t){
                    synchronized (ThreadPoolExecutor.class) {
                        t = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
                    }
                    threadPoolExecutor = t;
                }
            }
        }
        return threadPoolExecutor;
    }
}
