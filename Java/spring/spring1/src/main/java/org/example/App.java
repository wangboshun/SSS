package org.example;

import org.application.DbApplication;
import org.application.UserInfoService;
import org.model.UserInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        Test2();
    }

    /**
     * XML配置方式
     */
    public static void Test1() {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans2.xml");
        DbApplication application = (DbApplication) context.getBean("dbApplication");
        application.Test1();
    }

    /**
     * 配置类方法
     */
    public static void Test2() {
        ApplicationContext context = new AnnotationConfigApplicationContext(UserInfoService.class);
        UserInfo u = (UserInfo) context.getBean("getUserInfo");
        System.out.println(u.getName());
    }
}
