package org.aop;

/**
 * @author WBS
 * 自定义切入点
 * Date:2022/8/17
 */

public class DiyPointcut {
    public void before() {
        System.out.println("before");
    }

    public void after() {
        System.out.println("after");
    }
}
