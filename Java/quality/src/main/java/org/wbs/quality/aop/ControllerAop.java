package org.wbs.quality.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.wbs.quality.thread.ApiLogThread;

import javax.servlet.http.HttpServletRequest;

/**
 * @author WBS
 * Date:2022/6/2
 */

@Aspect
@Component
public class ControllerAop {

    @Autowired
    public ApiLogThread apiLogThread;

    /**
     * 控制器Aop
     */
    @Pointcut("execution(public * org.wbs.quality.controller.*.*(..))")
    public void ControllerAop() {
    }

    @Around("ControllerAop()")
    public Object aroundMethod(ProceedingJoinPoint pjd) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result;
        try {
            result = pjd.proceed();
            stopWatch.stop();
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            long spend = stopWatch.getTotalTimeMillis();
            apiLogThread.addLog(pjd, request, result, spend);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
