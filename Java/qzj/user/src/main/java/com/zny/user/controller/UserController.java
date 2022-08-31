package com.zny.user.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WBS
 * Date:2022/8/31
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Value("${use_username}")
    private String name;

    @Value("${user_password}")
    private String password;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String Test() {
        return "user";
    }
}
