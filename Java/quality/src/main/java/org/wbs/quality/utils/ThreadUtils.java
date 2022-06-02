package org.wbs.quality.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @author WBS
 * Date:2022/5/30
 */

public class ThreadUtils {


    /**
     * 创建线程池
     *
     * @return 线程池
     */
    public static ThreadPoolExecutor createThreadPool(String name) {
        ThreadFactory factory = builderThreadFactory(name);
        int processNum = Runtime.getRuntime().availableProcessors();
        int corePoolSize = (int) (processNum / (1 - 0.2));
        int maxPoolSize = (int) (processNum / (1 - 0.5));
        long keepAliveTime = 60;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, unit, workQueue, factory);
    }

    /**
     * 创建线程池
     *
     * @return 线程池
     */
    public static ThreadPoolExecutor createThreadPool(String name, int size) {
        ThreadFactory factory = builderThreadFactory(name);
        long keepAliveTime = 60;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        return new ThreadPoolExecutor(size, size, keepAliveTime, unit, workQueue, factory);
    }

    /**
     * 设置线程名称
     *
     * @return 线程工厂
     */
    private static ThreadFactory builderThreadFactory(String name) {
        return new ThreadFactoryBuilder().setNameFormat(name + "-Thread").build();
    }

}
