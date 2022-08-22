package org.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author WBS
 * Date:2022/8/17
 */

//aop切面编程
public class TestAop {
    public static void main(String[] args) {
        testProxy();
    }

    public static void testProxy() {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans3.xml");
        ITestClass t = (ITestClass) context.getBean("test");
        t.testProxy();
    }
}
