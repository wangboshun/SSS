package com.zny.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.zny.common.model.PageResult;
import com.zny.user.application.UserApplication;
import com.zny.user.model.user.UserModel;
import com.zny.user.model.user.UserTreeModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/8/31
 * 用户控制器
 */

@RestController
@RequestMapping("/user")
@Tag(name = "user", description = "用户模块")
public class UserController {

    private final UserApplication userApplication;

    public UserController(UserApplication userApplication) {
        this.userApplication = userApplication;
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     */
    @GetMapping(value = "/login")
    public SaResult login(String username, String password) {
        Map<String, String> tokenInfo = userApplication.login(username, password);
        if (tokenInfo != null) {
            return SaResult.data(tokenInfo);
        }
        return SaResult.get(401, "账号或密码错误！", null);
    }

    /**
     * 注销
     */
    @PostMapping(value = "/logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok("注销成功");
    }

    /**
     * 获取用户列表
     *
     * @param userId    用户id
     * @param userName  用户名
     * @param pageIndex 页码
     * @param pageSize  分页大小
     */
    @GetMapping(value = "/list")
    public SaResult list(
            @RequestParam(required = false) String userId, @RequestParam(required = false) String userName,
            @RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
        PageResult result = userApplication.getUserPage(userId, userName, pageIndex, pageSize);
        return SaResult.data(result);
    }

    /**
     * 获取用户树
     *
     * @param userId 用户id
     */
    @GetMapping(value = "/tree")
    public SaResult tree(@RequestParam(required = false) String userId) {
        List<UserTreeModel> result = userApplication.getUserTree(userId);
        return SaResult.data(result);
    }

    /**
     * 获取用户信息
     *
     * @param id 用户id
     */
    @GetMapping(value = "/{id}")
    public SaResult get(@PathVariable("id") String id) {
        return SaResult.data(userApplication.getUserById(id));
    }

    /**
     * 添加用户
     *
     * @param username 用户名
     * @param password 密码
     * @param userType 用户类型
     * @param parentId 父级id
     */
    @PostMapping(value = "/add")
    public SaResult add(
            String username, String password, @RequestParam(required = false) Integer userType,
            @RequestParam(required = false) String parentId) {
        return userApplication.addUser(username, password, userType, parentId);
    }

    /**
     * 删除用户
     *
     * @param id 用户id
     */
    @DeleteMapping(value = "/{id}")
    public SaResult delete(@PathVariable String id) {
        return userApplication.deleteUser(id);
    }

    /**
     * 更新用户信息
     *
     * @param id       用户id
     * @param username 用户名
     * @param password 密码
     * @param userType 用户类型
     * @param parentId 父级id
     */
    @PatchMapping(value = "/{id}")
    public SaResult update(
            @PathVariable String id, String username, String password, @RequestParam(required = false) Integer userType,
            @RequestParam(required = false) String parentId) {
        return userApplication.updateUser(id, username, password, userType, parentId);
    }

    /**
     * 根据角色获取用户
     *
     * @param roleId 角色id
     */
    @GetMapping(value = "/{roleId}/role")
    public SaResult getUserByRole(@PathVariable String roleId) {
        List<UserModel> list = userApplication.getUserByRole(roleId);
        return SaResult.data(list);
    }

    /**
     * 绑定用户到角色
     *
     * @param roleIds 角色id
     * @param userIds 用户id
     */
    @PatchMapping(value = "/bind_by_role")
    public SaResult bindUserByRole(String[] roleIds, String[] userIds) {
        return userApplication.bindUserByRole(roleIds, userIds);
    }

    /**
     * 解绑用户到角色
     *
     * @param roleIds 角色id
     * @param userIds id
     */
    @PatchMapping(value = "/unbind_by_role")
    public SaResult unBindUserByRole(String[] roleIds, String[] userIds) {
        return userApplication.unBindUserByRole(roleIds, userIds);
    }
}
