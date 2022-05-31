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
    public static ThreadPoolExecutor createThreadPool() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("Thread-pool-").build();
        int processNum = Runtime.getRuntime().availableProcessors();
        int corePoolSize = (int) (processNum / (1 - 0.2));
        int maxPoolSize = (int) (processNum / (1 - 0.5));
        long keepAliveTime = 60;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

}
