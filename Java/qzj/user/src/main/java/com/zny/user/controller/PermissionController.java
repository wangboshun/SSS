package com.zny.user.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.common.resource.ResourceApplication;
import com.zny.user.application.PermissionApplication;
import com.zny.user.model.permission.PermissionModel;
import com.zny.user.model.permission.PermissionTreeModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/9/6
 */

@RestController
@RequestMapping("/user/permission")
@Tag(name = "permission", description = "权限模块")
public class PermissionController {

    private final ResourceApplication resourceApplication;
    private final PermissionApplication permissionApplication;

    public PermissionController(ResourceApplication resourceApplication, PermissionApplication permissionApplication) {
        this.resourceApplication = resourceApplication;
        this.permissionApplication = permissionApplication;
    }

    /**
     * 获取权限列表
     *
     * @param permissionId   权限id
     * @param permissionName 权限名
     * @param permissionCode 权限代码
     * @param pageSize       分页大小
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public SaResult list(
            @RequestParam(required = false) String permissionId, @RequestParam(required = false) String permissionName,
            @RequestParam(required = false) String permissionCode, @RequestParam(required = false) Integer pageIndex,
            @RequestParam(required = false) Integer pageSize) {
        Map<String, Object> result = permissionApplication.getPermissionList(permissionId, permissionName, permissionCode, pageIndex, pageSize);
        return SaResult.data(result);
    }

    /**
     * 获取权限树
     *
     * @param permissionId 权限id
     */
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public SaResult tree(@RequestParam(required = false) String permissionId) {
        List<PermissionTreeModel> result = permissionApplication.getPermissionTree(permissionId);
        return SaResult.data(result);
    }

    /**
     * 获取权限信息
     *
     * @param id 权限id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public SaResult get(@PathVariable String id) {
        PermissionModel model = permissionApplication.getById(id);
        return SaResult.data(model);
    }

    /**
     * 添加权限
     *
     * @param permissionName 权限名
     * @param permissionCode 权限代码
     * @param parentId       父级id
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public SaResult add(String permissionName, String permissionCode, @RequestParam(required = false) String parentId) {
        return permissionApplication.addPermission(permissionName, permissionCode, parentId);
    }


    /**
     * 删除权限
     *
     * @param id 权限id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public SaResult delete(@PathVariable String id) {
        return permissionApplication.deletePermission(id);
    }

    /**
     * 更新权限信息
     *
     * @param id             权限id
     * @param permissionName 权限名
     * @param permissionCode 权限代码
     * @param parentId       父级id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public SaResult update(
            @PathVariable String id, String permissionName, String permissionCode,
            @RequestParam(required = false) String parentId) {
        return permissionApplication.updatePermission(id, permissionName, permissionCode, parentId);
    }

    /**
     * 根据用户获取权限
     *
     * @param userId 用户id
     */
    @RequestMapping(value = "/by_user", method = RequestMethod.GET)
    public SaResult getPermissionByUser(String userId) {
        List<PermissionModel> list = permissionApplication.getPermissionByUser(userId);
        return SaResult.data(list);
    }

    /**
     * 根据角色获取权限
     *
     * @param roleId 角色id
     */
    @RequestMapping(value = "/by_role", method = RequestMethod.GET)
    public SaResult getPermissionByRole(String roleId) {
        List<PermissionModel> list = permissionApplication.getPermissionByRole(roleId);
        return SaResult.data(list);
    }

    /**
     * 绑定权限到用户
     *
     * @param userId 用户id
     * @param menuId 权限id
     */
    @RequestMapping(value = "/bind_by_user", method = RequestMethod.POST)
    public SaResult bindPermissionByUser(String userId, String[] menuId) {
        return permissionApplication.bindPermissionByUser(userId, menuId);
    }

    /**
     * 绑定权限到角色
     *
     * @param roleId 角色id
     * @param menuId 权限id
     */
    @RequestMapping(value = "/bind_by_role", method = RequestMethod.POST)
    public SaResult bindPermissionByRole(String roleId, String[] menuId) {
        return permissionApplication.bindPermissionByRole(roleId, menuId);
    }

    /**
     * 解绑权限到用户
     *
     * @param userId 用户id
     * @param permissionId  id
     */
    @RequestMapping(value = "/unbind_by_user", method = RequestMethod.POST)
    public SaResult unBindPermissionByUser(String userId, String[] permissionId) {
        return permissionApplication.unBindPermissionByUser(userId, permissionId);
    }

    /**
     * 解绑权限到角色
     *
     * @param roleId 角色id
     * @param permissionId  id
     */
    @RequestMapping(value = "/unbind_by_role", method = RequestMethod.POST)
    public SaResult unBindPermissionByRole(String roleId, String[] permissionId) {
        return permissionApplication.unBindPermissionByRole(roleId, permissionId);
    }
}
