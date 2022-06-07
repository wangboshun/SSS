package org.wbs.quality.infra.aop.method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * @author WBS
 * Date:2022/6/6
 */

@Aspect
@Component
public class MethodLogAop {

    public final MethodLogThread methodLogThread;

    public MethodLogAop(MethodLogThread methodLogThread) {
        this.methodLogThread = methodLogThread;
    }

    @Pointcut("@annotation(org.wbs.quality.infra.aop.method.MethodLogAttribute)")
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
