package com.etouse.noviewholder.util;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/9/20.
 */
public class ThreadPoolManager {

    private final ThreadPoolExecutor poolExecutor;

    public ThreadPoolManager() {
        int CPU_COUNT = Runtime.getRuntime().availableProcessors();
        int corePoolSize = CPU_COUNT + 1;
        int maximumPoolSize = 10;
        poolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 5, TimeUnit.HOURS,
                new LinkedBlockingDeque<Runnable>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

    }

    public static final ThreadPoolManager mThreadPoolManager = new ThreadPoolManager();

    public static ThreadPoolManager getInstance() {
        return mThreadPoolManager;
    }

    public void addThreadPool(Runnable runnable) {
        poolExecutor.execute(runnable);
    }
}
