package com.zny.user.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zny.user.application.user.UserApplication;
import com.zny.user.model.UserModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WBS
 * Date:2022/8/31
 */

@RestController
@RequestMapping("/user")
@Tag(name = "user", description = "用户模块")
public class UserController {

    @Autowired
    private UserApplication userApplication;

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public SaResult login(String username, String password) {
        String token = userApplication.login(username, password);
        if (StringUtils.isNotBlank(token)) {
            return SaResult.data(token);
        }
        return SaResult.get(401, "请登录！", null);
    }

    /**
     * 注销
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok("注销成功");
    }

    /**
     * 获取用户信息
     *
     * @param id 用户id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @SaCheckLogin
    public SaResult info(@PathVariable String id) {
        UserModel model = userApplication.getById(id);
        return SaResult.data(model);
    }

    /**
     * 添加用户
     *
     * @param username 用户名
     * @param password 密码
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @SaCheckPermission(value = "user-add", orRole = "admin")
    public SaResult add(String username, String password) {
        return SaResult.ok("添加成功");
    }


    /**
     * 删除用户
     *
     * @param id 用户id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @SaCheckPermission(value = "user-delete", orRole = "admin")
    public SaResult delete(@PathVariable String id) {
        return SaResult.ok("删除成功");
    }
}
