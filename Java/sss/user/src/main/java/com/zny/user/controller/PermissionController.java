package com.zny.user.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.common.model.PageResult;
import com.zny.user.application.PermissionApplication;
import com.zny.user.model.permission.PermissionModel;
import com.zny.user.model.permission.PermissionTreeModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author WBS
 * Date:2022/9/6
 * 权限控制器
 */

@RestController
@RequestMapping("/user/permission")
@Tag(name = "permission", description = "权限模块")
public class PermissionController {

    private final PermissionApplication permissionApplication;

    public PermissionController(PermissionApplication permissionApplication) {
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
    @GetMapping(value = "/list")
    public SaResult list(
            @RequestParam(required = false) String permissionId, @RequestParam(required = false) String permissionName,
            @RequestParam(required = false) String permissionCode, @RequestParam(required = false) Integer pageIndex,
            @RequestParam(required = false) Integer pageSize) {
        PageResult result = permissionApplication.getPermissionPage(permissionId, permissionName, permissionCode, pageIndex, pageSize);
        return SaResult.data(result);
    }

    /**
     * 获取权限树
     *
     * @param permissionId 权限id
     */
    @GetMapping(value = "/tree")
    public SaResult tree(@RequestParam(required = false) String permissionId) {
        List<PermissionTreeModel> result = permissionApplication.getPermissionTree(permissionId);
        return SaResult.data(result);
    }

    /**
     * 获取权限信息
     *
     * @param id 权限id
     */
    @GetMapping(value = "/{id}")
    public SaResult get(@PathVariable String id) {
        return SaResult.data(permissionApplication.getPermissionById(id));
    }

    /**
     * 添加权限
     *
     * @param permissionName 权限名
     * @param permissionCode 权限代码
     * @param parentId       父级id
     */
    @PostMapping(value = "/add")
    public SaResult add(String permissionName, String permissionCode, @RequestParam(required = false) String parentId) {
        return permissionApplication.addPermission(permissionName, permissionCode, parentId);
    }


    /**
     * 删除权限
     *
     * @param id 权限id
     */
    @DeleteMapping(value = "/{id}")
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
    @PatchMapping(value = "/{id}")
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
    @GetMapping(value = "/by_user")
    public SaResult getPermissionByUser(String userId) {
        List<PermissionModel> list = permissionApplication.getPermissionByUser(userId);
        return SaResult.data(list);
    }

    /**
     * 根据角色获取权限
     *
     * @param roleId 角色id
     */
    @GetMapping(value = "/by_role")
    public SaResult getPermissionByRole(String roleId) {
        List<PermissionModel> list = permissionApplication.getPermissionByRole(roleId);
        return SaResult.data(list);
    }

    /**
     * 绑定权限到用户
     *
     * @param userIds 用户id
     * @param menuIds 权限id
     */
    @PatchMapping(value = "/bind_by_user")
    public SaResult bindPermissionByUser(String[] userIds, String[] menuIds) {
        return permissionApplication.bindPermissionByUser(userIds, menuIds);
    }

    /**
     * 绑定权限到角色
     *
     * @param roleIds 角色id
     * @param menuIds 权限id
     */
    @PatchMapping(value = "/bind_by_role")
    public SaResult bindPermissionByRole(String[] roleIds, String[] menuIds) {
        return permissionApplication.bindPermissionByRole(roleIds, menuIds);
    }

    /**
     * 解绑权限到用户
     *
     * @param userIds       用户id
     * @param permissionIds id
     */
    @PatchMapping(value = "/unbind_by_user")
    public SaResult unBindPermissionByUser(String[] userIds, String[] permissionIds) {
        return permissionApplication.unBindPermissionByUser(userIds, permissionIds);
    }

    /**
     * 解绑权限到角色
     *
     * @param roleIds       角色id
     * @param permissionIds id
     */
    @PatchMapping(value = "/unbind_by_role")
    public SaResult unBindPermissionByRole(String[] roleIds, String[] permissionIds) {
        return permissionApplication.unBindPermissionByRole(roleIds, permissionIds);
    }
}
