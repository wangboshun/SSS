package org.wbs.quality.aop;

import com.google.gson.Gson;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/6/2
 */

@Aspect
@Component
public class ControllerAop {

    @Pointcut("execution(public * org.wbs.quality.controller.*.*(..))")
    public void ControllerAop() {
    }

    @Before("ControllerAop()")
    public void doBefore(JoinPoint joinPoint) {
//        System.out.println("2---doBefore");
    }

    @After("ControllerAop()")
    public void doAfter(JoinPoint joinPoint) {
//        System.out.println("4---doAfter");
    }

    @AfterReturning("ControllerAop()")
    public void doAfterReturning(JoinPoint joinPoint) {
//        System.out.println("3---doAfterReturning");
    }

    @AfterThrowing("ControllerAop()")
    public void deAfterThrowing(JoinPoint joinPoint) {
//        System.out.println("3---deAfterThrowing");
    }

    @Around("ControllerAop()")
    public Object deAround(ProceedingJoinPoint joinPoint) throws Throwable {
//        System.out.println("1---deAround");
        return joinPoint.proceed();
    }

    @Around("ControllerAop()")
    public Object aroundMethod(ProceedingJoinPoint pjd) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
//        System.out.println("0---deAround");
        Object result = null;
        try {
            //前置通知
//            System.out.println("目标方法执行前...");

            //执行目标方法
            result = pjd.proceed();

            //用新的参数值执行目标方法
            //result = pjd.proceed(new Object[]{"newSpring","newAop"});

            //返回通知
//            System.out.println("目标方法返回结果后..." + result);

        } catch (Throwable e) {
            //异常通知
//            System.out.println("执行目标方法异常后...");
            throw new RuntimeException(e);
        }
        //后置通知
//        System.out.println("目标方法执行后...");

        Object[] args = pjd.getArgs();
        String[] argNames = ((MethodSignature) pjd.getSignature()).getParameterNames();
        Map<String, Object> params = new HashMap<>(args.length);
        for (int i = 0; i < argNames.length; i++) {
            params.put(argNames[i], args[i]);
        }

        stopWatch.stop();
        long spend = stopWatch.getTotalTimeMillis();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        Map<String, Object> json = new HashMap<>();
        json.put("params", params);
        json.put("name", pjd.getSignature().getName());
        json.put("time", spend + " ms");
        json.put("result", result);
        json.put("url", request.getRequestURL());
        json.put("method", request.getMethod());

        System.out.println(new Gson().toJson(json));

        return result;
    }
}
