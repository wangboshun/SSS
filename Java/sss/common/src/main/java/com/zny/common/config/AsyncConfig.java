package com.zny.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author WBS
 * Date:2022/9/5
 * 异步配置类
 */

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean(name = "customExecutor")
    public ThreadPoolTaskExecutor customExecutor() {
        int processNum = Runtime.getRuntime().availableProcessors();
        int corePoolSize = (int) (processNum / (1 - 0.2));
        int maxPoolSize = (int) (processNum / (1 - 0.5));
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程池大小
        executor.setCorePoolSize(corePoolSize);
        //最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //队列容量
        executor.setQueueCapacity(maxPoolSize * 10);
        //活跃时间
        executor.setKeepAliveSeconds(60);
        //线程名字前缀
        executor.setThreadNamePrefix("线程---");
        /*
          拒绝处理策略
          CallerRunsPolicy()：交由调用方线程运行，比如 main 线程。
          AbortPolicy()：直接抛出异常。
          DiscardPolicy()：直接丢弃。
          DiscardOldestPolicy()：丢弃队列中最老的任务。
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 可在这初始化，也可以不初始化，在调用的时候再初始化
        executor.initialize();
        return executor;
    }
}
