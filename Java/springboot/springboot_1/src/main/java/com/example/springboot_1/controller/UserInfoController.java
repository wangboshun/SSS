package com.example.springboot_1.controller;

import com.example.springboot_1.mapper.UserInfoMapper;
import com.example.springboot_1.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author WBS
 * Date:2022/8/27
 */

@RestController
public class UserInfoController {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @GetMapping("/queryAll")
    public List<UserInfo> queryAll() {
        List<UserInfo> userInfos = userInfoMapper.queryAll();
        for (UserInfo userInfo : userInfos) {
            System.out.println(userInfo);
        }
        return userInfos;
    }
}
