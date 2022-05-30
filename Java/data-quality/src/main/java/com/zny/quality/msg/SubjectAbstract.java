package com.zny.quality.msg;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author WBS
 * Date:2022/5/30
 */

public abstract class SubjectAbstract {
    private final List<MsgObserverInterface> list = new ArrayList<>();

    public void addObserver(MsgObserverInterface observer) {
        list.add(observer);
    }

    public void removeObserver(MsgObserverInterface observer) {
        list.remove(observer);
    }

    public void notifyObservers() {
        System.out.println("通知所有发信息中间件");

        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("Thread-pool-").build();
        int processNum = Runtime.getRuntime().availableProcessors();
        int corePoolSize = (int) (processNum / (1 - 0.2));
        int maxPoolSize = (int) (processNum / (1 - 0.5));
        long keepAliveTime = 60;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        for (MsgObserverInterface observer : list) {
            executor.execute(observer);
        }

    }
}
