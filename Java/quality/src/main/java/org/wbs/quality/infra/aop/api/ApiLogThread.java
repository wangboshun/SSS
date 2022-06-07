package org.wbs.quality.infra.aop.api;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
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
public class ApiLogThread {

    final Logger logger = LoggerFactory.getLogger("ApiLog");

    Queue<ApiLogModel> logQueue = new LinkedBlockingQueue<>();

    /**
     * 延时5秒消费日志
     */
    @Async
    @Scheduled(fixedDelay = 5000)
    public void remove() {
        if (logQueue.isEmpty()) {
            System.out.println("没有Api日志任务");
        }
        while (!logQueue.isEmpty()) {
            ApiLogModel model = logQueue.poll();
            logger.info(new Gson().toJson(model));
        }
    }

    /**
     * 添加日志到消息队列
     *
     * @param pjd     pjd
     * @param request request
     * @param result  result
     * @param spend   spend
     */

    public void addLog(ProceedingJoinPoint pjd, HttpServletRequest request, Object result, Long spend) {
        ApiLogModel model = new ApiLogModel();
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
        model.setClient(request.getRemoteHost() + ":" + request.getRemotePort());
        model.setMethod(request.getMethod());
        model.setUrl(request.getRequestURL().toString());
        logQueue.offer(model);
    }
}
