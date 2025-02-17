package org.wbs.quality.infra.aop.api;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author WBS
 * Date:2022/6/2
 */

@Aspect
@Component
public class ApiLogAop {

    public final ApiLogThread apiLogThread;

    public ApiLogAop(ApiLogThread apiLogThread) {
        this.apiLogThread = apiLogThread;
    }

    /**
     * 控制器Aop
     */
    @Pointcut("execution(public * org.wbs.quality.controller.*.*(..))")
    public void aop() {
    }

    @Around("aop()")
    public Object aroundMethod(ProceedingJoinPoint pjd) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result;
        try {
            result = pjd.proceed();
            stopWatch.stop();
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request;
            if (attributes != null) {
                request = attributes.getRequest();
                long spend = stopWatch.getTotalTimeMillis();
                apiLogThread.addLog(pjd, request, result, spend);
            }

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
