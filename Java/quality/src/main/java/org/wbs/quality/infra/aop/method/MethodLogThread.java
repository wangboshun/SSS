package org.wbs.quality.infra.aop.method;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author WBS
 * Api日志线程
 * Date:2022/6/2
 */

@Slf4j
@Component
@EnableScheduling
@EnableAsync
public class MethodLogThread {

    Queue<MethodLogModel> logQueue = new LinkedBlockingQueue<>();

    /**
     * 延时5秒消费日志
     */
    @Async
    @Scheduled(fixedDelay = 5000)
    public void remove() {
        if (logQueue.isEmpty()) {
            System.out.println("没有Method日志任务");
        }
        while (!logQueue.isEmpty()) {
            MethodLogModel model = logQueue.poll();
            log.info(new Gson().toJson(model));
        }
    }

    /**
     * 添加日志到消息队列
     *
     * @param pjd    pjd
     * @param result result
     * @param spend  spend
     */

    @Async
    public void addLog(ProceedingJoinPoint pjd, Object result, Long spend) {
        MethodLogModel model = new MethodLogModel();
        MethodSignature signature = (MethodSignature) pjd.getSignature();
        Method method = signature.getMethod();
        MethodLogAttribute attribute = method.getAnnotation(MethodLogAttribute.class);
        if (attribute != null) {
            model.setAttribute(attribute.value());
        }

        Object[] args = pjd.getArgs();
        String[] argNames = ((MethodSignature) pjd.getSignature()).getParameterNames();
        Map<String, Object> params = new HashMap<>(args.length);
        for (int i = 0; i < argNames.length; i++) {
            params.put(argNames[i], args[i]);
        }
        model.setParameters(params);
        model.setName(pjd.getSignature().getName());
        model.setTime(spend);
        model.setResult(result);
        logQueue.offer(model);
    }
}
