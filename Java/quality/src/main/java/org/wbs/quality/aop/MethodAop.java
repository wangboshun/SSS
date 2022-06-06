package org.wbs.quality.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.wbs.quality.thread.MethodLogThread;

/**
 * @author WBS
 * Date:2022/6/6
 */

@Aspect
@Component
public class MethodAop {

    public final MethodLogThread methodLogThread;

    public MethodAop(MethodLogThread methodLogThread) {
        this.methodLogThread = methodLogThread;
    }

    @Pointcut("@annotation(org.wbs.quality.attribute.MethodAopAttribute)")
    public void aop() {
    }

    @Around("aop()")
    public Object around(ProceedingJoinPoint pjd) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result;
        try {
            result = pjd.proceed();
            stopWatch.stop();
            long spend = stopWatch.getTotalTimeMillis();
            methodLogThread.addLog(pjd, result, spend);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
