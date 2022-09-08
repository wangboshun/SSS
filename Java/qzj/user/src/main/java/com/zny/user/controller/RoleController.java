package com.zny.user.controller;

import cn.dev33.satoken.util.SaResult;
import com.google.common.collect.Table;
import com.zny.user.application.ResourceApplication;
import com.zny.user.application.RoleApplication;
import com.zny.user.model.RoleModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/9/6
 */

@RestController
@RequestMapping("/user/role")
@Tag(name = "role", description = "角色模块")
public class RoleController {

    @Autowired
    private ResourceApplication resourceApplication;

    @Autowired
    private RoleApplication roleApplication;

    /**
     * 获取角色列表
     *
     * @param roleId   角色id
     * @param roleName 角色名
     * @param roleCode 角色代码
     * @param pageSize 分页大小
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public SaResult list(
            @RequestParam(required = false) String roleId, @RequestParam(required = false) String roleName,
            @RequestParam(required = false) String roleCode, @RequestParam(required = false) Integer pageIndex,
            @RequestParam(required = false) Integer pageSize) {
        Map<String, Object> result = roleApplication.getRoleList(roleId, roleName, roleCode, pageIndex, pageSize);
        return SaResult.data(result);
    }

    /**
     * 获取角色信息
     *
     * @param id 角色id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public SaResult get(@PathVariable String id) {
        RoleModel model = roleApplication.getById(id);
        return SaResult.data(model);
    }

    /**
     * 添加角色
     *
     * @param roleName 角色名
     * @param roleCode 角色代码
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public SaResult add(String roleName, String roleCode) {
        return roleApplication.addRole(roleName, roleCode);
    }


    /**
     * 删除角色
     *
     * @param id 角色id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public SaResult delete(@PathVariable String id) {
        return roleApplication.deleteRole(id);
    }

    /**
     * 更新角色信息
     *
     * @param id       角色id
     * @param roleName 角色名
     * @param roleCode 角色代码
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public SaResult update(@PathVariable String id, String roleName, String roleCode) {
        return roleApplication.updateRole(id, roleName, roleCode);
    }

    /**
     * 根据角色获取用户
     *
     * @param roleId 角色id
     */
    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    public SaResult getUser(String roleId) {
        Table<String, String, String> table = resourceApplication.getUserByRole(roleId);
        List<Map<String, String>> list = resourceApplication.TableConvertList(table);
        return SaResult.data(list);
    }

    /**
     * 根据角色获取菜单
     *
     * @param roleId 角色id
     */
    @RequestMapping(value = "/getMenu", method = RequestMethod.GET)
    public SaResult getMenu(String roleId) {
        Table<String, String, String> table = resourceApplication.getMenuByRole(roleId);
        List<Map<String, String>> list = resourceApplication.TableConvertList(table);
        return SaResult.data(list);
    }

    /**
     * 根据角色获取权限
     *
     * @param roleId 角色id
     */
    @RequestMapping(value = "/getPermission", method = RequestMethod.GET)
    public SaResult getPermission(String roleId) {
        Table<String, String, String> table = resourceApplication.getPermissionByRole(roleId);
        List<Map<String, String>> list = resourceApplication.TableConvertList(table);
        return SaResult.data(list);
    }
}
