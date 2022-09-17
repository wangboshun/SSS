package com.zny.user.controller;

import cn.dev33.satoken.util.SaResult;
import com.google.common.collect.Table;
import com.zny.user.application.RoleApplication;
import com.zny.user.application.resource.ResourceApplication;
import com.zny.user.model.resource.ResourceEnum;
import com.zny.user.model.role.RoleModel;
import com.zny.user.model.role.RoleTreeModel;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    private final ResourceApplication resourceApplication;

    private final RoleApplication roleApplication;

    public RoleController(ResourceApplication resourceApplication, RoleApplication roleApplication) {
        this.resourceApplication = resourceApplication;
        this.roleApplication = roleApplication;
    }

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
     * 获取角色树
     *
     * @param roleId 角色id
     */
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public SaResult tree(@RequestParam(required = false) String roleId) {
        List<RoleTreeModel> result = roleApplication.getRoleTree(roleId);
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
     * @param parentId 父级id
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public SaResult add(String roleName, String roleCode, @RequestParam(required = false) String parentId) {
        return roleApplication.addRole(roleName, roleCode, parentId);
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
     * 删除指定角色下的所有资源
     *
     * @param roleId 角色id
     */
    @RequestMapping(value = "/delResource", method = RequestMethod.DELETE)
    public SaResult delResource(String roleId) {
        return resourceApplication.deleteForMain(roleId, ResourceEnum.ROLE);
    }

    /**
     * 更新角色信息
     *
     * @param id       角色id
     * @param roleName 角色名
     * @param roleCode 角色代码
     * @param parentId 父级id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public SaResult update(
            @PathVariable String id, String roleName, String roleCode,
            @RequestParam(required = false) String parentId) {
        return roleApplication.updateRole(id, roleName, roleCode, parentId);
    }

    /**
     * 根据角色获取用户
     *
     * @param roleId 角色id
     */
    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    public SaResult getUser(String roleId) {
        Table<String, String, String> table = resourceApplication.getUserByRole(roleId);
        List<Map<String, String>> list = resourceApplication.tableConvertList(table);
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
        List<Map<String, String>> list = resourceApplication.tableConvertList(table);
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
        List<Map<String, String>> list = resourceApplication.tableConvertList(table);
        return SaResult.data(list);
    }

    /**
     * 根据角色获取api
     *
     * @param roleId 角色id
     */
    @RequestMapping(value = "/getApi", method = RequestMethod.GET)
    public SaResult getApi(String roleId) {
        Table<String, String, String> table = resourceApplication.getApiByRole(roleId);
        List<Map<String, String>> list = resourceApplication.tableConvertList(table);
        return SaResult.data(list);
    }
}
