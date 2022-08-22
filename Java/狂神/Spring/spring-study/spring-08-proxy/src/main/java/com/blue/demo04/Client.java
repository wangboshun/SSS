package com.blue.demo04;

import com.blue.demo02.UserService;
import com.blue.demo02.UserServiceImpl;

public class Client {
    public static void main(String[] args) {
        //真实角色
        UserServiceImpl userService = new UserServiceImpl();

        //代理角色：现在没有
        ProxyInvocationHandler pih = new ProxyInvocationHandler();
        pih.setTarget(userService);

        //通过调用程序处理角色 来处理我们要调用的接口对象
        UserService proxy = (UserService) pih.getProxy();

        proxy.delete();

    }

}
