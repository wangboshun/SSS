package com.zny.user.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.zny.common.aop.api.ApiLog;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author WBS
 * Date:2022/8/31
 */

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "user", description = "用户模块")
public class UserController {

    @Value("${use_username}")
    private String name;

    @Value("${user_password}")
    private String password;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String Test() {
        return "user";
    }

    @ApiLog
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public SaResult login(String username, String password) {
        log.info("login info");
        log.error("login error");

        StpUtil.login(username);
        Object loginId = StpUtil.getLoginId();
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        List<String> permissionList = StpUtil.getPermissionList();
        List<String> roleList = StpUtil.getRoleList();
        boolean permission = StpUtil.hasPermission("user-add");
        boolean role = StpUtil.hasRole("admin");
        return SaResult.data(tokenInfo.tokenValue);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok("注销成功");
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @SaCheckLogin
    public SaResult info() {
        SaSession session = StpUtil.getSession();
        SaSession tokenSession = StpUtil.getTokenSession();
        return SaResult.data(StpUtil.getTokenInfo());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @SaCheckPermission(value = "user-add", orRole = "admin")
    public SaResult add(String username, String password) {
        return SaResult.ok("添加成功");
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @SaCheckPermission(value = "user-delete", orRole = "admin")
    public SaResult delete(String username) {
        return SaResult.ok("删除成功");
    }
}
