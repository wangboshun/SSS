package com.zny.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author WBS
 * Date:2022/9/5
 * 定时器配置类
 */

@Configuration
@EnableScheduling
public class ScheduledConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        int processNum = Runtime.getRuntime().availableProcessors();
        int corePoolSize = (int) (processNum / (1 - 0.2));
        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        executor.setPoolSize(corePoolSize);
        executor.setThreadNamePrefix("定时任务---");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        taskRegistrar.setScheduler(executor);
    }
}
