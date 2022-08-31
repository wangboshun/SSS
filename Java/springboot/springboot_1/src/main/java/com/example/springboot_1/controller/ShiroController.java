package com.example.springboot_1.controller;

import com.example.springboot_1.shiro.MyRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author WBS
 * Date:2022/8/29
 */

@RestController
public class ShiroController {

    @GetMapping("/shiro1")
    public String Test1() {
        MyRealm realm = new MyRealm();
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(realm);

        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("admin", "123456");
        subject.login(token);

        System.out.println("认证状态：" + subject.isAuthenticated());

        System.out.println("admin角色？" + subject.hasRole("admin"));

        System.out.println("user：add权限？" + subject.isPermitted("user:add"));

        return "ok";
    }

}
