package org.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * @author WBS
 * 注解切面编程
 * Date:2022/8/17
 */

@Aspect
public class AnnotationPointcut {

    @Before("execution(* org.aop.TestImpl.*())")
    public void before() {
        System.out.println("before");
    }

    @Around("execution(* org.aop.TestImpl.*())")
    public Object around(ProceedingJoinPoint jp) throws Throwable//连接点
    {
        System.out.println("环绕前");
        Object o=jp.proceed();
        System.out.println("环绕后");
        return  o;
    }

    @After("execution(* org.aop.TestImpl.*())")
    public void after() {
        System.out.println("after");
    }
}