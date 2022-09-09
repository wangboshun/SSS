package com.zny.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.google.common.collect.Table;
import com.zny.user.application.ResourceApplication;
import com.zny.user.application.UserApplication;
import com.zny.user.model.UserModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/8/31
 */

@RestController
@RequestMapping("/user")
@Tag(name = "user", description = "用户模块")
public class UserController {

    @Autowired
    private ResourceApplication resourceApplication;

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
        Map<String, String> tokenInfo = userApplication.login(username, password);
        if (tokenInfo != null) {
            return SaResult.data(tokenInfo);
        }
        return SaResult.get(401, "账号或密码错误！", null);
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
     * 获取用户列表
     *
     * @param userId    用户id
     * @param userName  用户名
     * @param pageIndex 页码
     * @param pageSize  分页大小
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public SaResult list(
            @RequestParam(required = false) String userId, @RequestParam(required = false) String userName,
            @RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
        Map<String, Object> result = userApplication.getUserList(userId, userName, pageIndex, pageSize);
        return SaResult.data(result);
    }

    /**
     * 获取用户信息
     *
     * @param id 用户id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public SaResult get(@PathVariable("id") String id) {
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
    public SaResult add(String username, String password) {
        return userApplication.addUser(username, password);
    }

    /**
     * 删除用户
     *
     * @param id 用户id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public SaResult delete(@PathVariable String id) {
        return userApplication.deleteUser(id);
    }

    /**
     * 更新用户信息
     *
     * @param id       用户id
     * @param username 用户名
     * @param password 密码
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public SaResult update(@PathVariable String id, String username, String password) {
        return userApplication.updateUser(id, username, password);
    }

    /**
     * 根据用户获取角色信息
     *
     * @param userId 用户id
     */
    @RequestMapping(value = "/getRole", method = RequestMethod.GET)
    public SaResult getRole(String userId) {
        Table<String, String, String> table = resourceApplication.getRoleByUser(userId);
        List<Map<String, String>> list = resourceApplication.TableConvertList(table);
        return SaResult.data(list);
    }

    /**
     * 根据用户获取菜单信息
     *
     * @param userId 用户id
     */
    @RequestMapping(value = "/getMenu", method = RequestMethod.GET)
    public SaResult getMenu(String userId) {
        Table<String, String, String> table = resourceApplication.getMenuByUser(userId);
        List<Map<String, String>> list = resourceApplication.TableConvertList(table);
        return SaResult.data(list);
    }

    /**
     * 根据用户获取权限信息
     *
     * @param userId 用户id
     */
    @RequestMapping(value = "/getPermission", method = RequestMethod.GET)
    public SaResult getPermission(String userId) {
        Table<String, String, String> table = resourceApplication.getPermissionByUser(userId);
        List<Map<String, String>> list = resourceApplication.TableConvertList(table);
        return SaResult.data(list);
    }

}
