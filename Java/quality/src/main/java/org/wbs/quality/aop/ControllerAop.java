package org.wbs.quality.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;

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

        Object[] args = joinPoint.getArgs(); // 参数值
        String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames(); // 参数名
        System.out.println("目标方法名为:" + joinPoint.getSignature().getName());
        System.out.println("目标方法所属类的简单类名:" + joinPoint.getSignature().getDeclaringType().getSimpleName());
        System.out.println("目标方法所属类的类名:" + joinPoint.getSignature().getDeclaringTypeName());
        System.out.println("目标方法声明类型:" + Modifier.toString(joinPoint.getSignature().getModifiers()));

        System.out.println("2---doBefore");
    }

    @After("ControllerAop()")
    public void doAfter(JoinPoint joinPoint) {
        System.out.println("4---doAfter");
    }

    @AfterReturning("ControllerAop()")
    public void doAfterReturning(JoinPoint joinPoint) {
        System.out.println("3---doAfterReturning");
    }

    @AfterThrowing("ControllerAop()")
    public void deAfterThrowing(JoinPoint joinPoint) {
        System.out.println("3---deAfterThrowing");
    }

    @Around("ControllerAop()")
    public Object deAround(ProceedingJoinPoint joinPoint) throws Throwable {

        System.out.println("1---deAround");
        return joinPoint.proceed();
    }

    @Around("ControllerAop()")
    public Object aroundMethod(ProceedingJoinPoint pjd) {
        System.out.println("0---deAround");
        Object result = null;
        try {
            //前置通知
            System.out.println("目标方法执行前...");

            //执行目标方法
            result = pjd.proceed();

            //用新的参数值执行目标方法
            //result = pjd.proceed(new Object[]{"newSpring","newAop"});

            //返回通知
            System.out.println("目标方法返回结果后..." + result);

        } catch (Throwable e) {
            //异常通知
            System.out.println("执行目标方法异常后...");
            throw new RuntimeException(e);
        }
        //后置通知
        System.out.println("目标方法执行后...");

        return result;
    }
}
