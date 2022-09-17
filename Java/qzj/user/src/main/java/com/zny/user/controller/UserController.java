package com.zny.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.google.common.collect.Table;
import com.zny.user.application.ResourceApplication;
import com.zny.user.application.UserApplication;
import com.zny.user.model.resource.ResourceEnum;
import com.zny.user.model.user.UserModel;
import com.zny.user.model.user.UserTreeModel;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    private final ResourceApplication resourceApplication;

    private final UserApplication userApplication;

    public UserController(ResourceApplication resourceApplication, UserApplication userApplication) {
        this.resourceApplication = resourceApplication;
        this.userApplication = userApplication;
    }

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
     * 获取用户树
     *
     * @param userId 用户id
     */
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public SaResult tree(@RequestParam(required = false) String userId) {
        List<UserTreeModel> result = userApplication.getUserTree(userId);
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
     * @param userType 用户类型
     * @param parentId 父级id
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
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
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public SaResult delete(@PathVariable String id) {
        return userApplication.deleteUser(id);
    }

    /**
     * 删除指定用户下的所有资源
     *
     * @param userId 用户id
     */
    @RequestMapping(value = "/delResource", method = RequestMethod.DELETE)
    public SaResult delResource(String userId) {
        return resourceApplication.deleteForMain(userId, ResourceEnum.USER);
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
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public SaResult update(
            @PathVariable String id, String username, String password, @RequestParam(required = false) Integer userType,
            @RequestParam(required = false) String parentId) {
        return userApplication.updateUser(id, username, password, userType, parentId);
    }

    /**
     * 根据用户获取角色信息
     *
     * @param userId 用户id
     */
    @RequestMapping(value = "/getRole", method = RequestMethod.GET)
    public SaResult getRole(String userId) {
        Table<String, String, String> table = resourceApplication.getRoleByUser(userId);
        List<Map<String, String>> list = resourceApplication.tableConvertList(table);
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
        List<Map<String, String>> list = resourceApplication.tableConvertList(table);
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
        List<Map<String, String>> list = resourceApplication.tableConvertList(table);
        return SaResult.data(list);
    }

    /**
     * 根据用户获取Api
     *
     * @param userId 用户id
     */
    @RequestMapping(value = "/getApi", method = RequestMethod.GET)
    public SaResult getApi(String userId) {
        Table<String, String, String> table = resourceApplication.getApiByUser(userId);
        List<Map<String, String>> list = resourceApplication.tableConvertList(table);
        return SaResult.data(list);
    }

}
