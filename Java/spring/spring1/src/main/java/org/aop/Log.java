package org.aop;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @author WBS
 * Date:2022/8/17
 */

public class Log implements MethodBeforeAdvice {

    //要执行的目标对象的方法
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println(target.getClass() + "的" + method.getName() + "方法被执行了");
    }
}
