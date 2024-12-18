package com.zny.user.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.common.model.PageResult;
import com.zny.user.application.RoleApplication;
import com.zny.user.model.role.RoleModel;
import com.zny.user.model.role.RoleTreeModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author WBS
 * Date:2022/9/6
 * 角色控制器
 */

@RestController
@RequestMapping("/user/role")
@Tag(name = "role", description = "角色模块")
public class RoleController {

    private final RoleApplication roleApplication;

    public RoleController(RoleApplication roleApplication) {
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
    @GetMapping(value = "/list")
    public SaResult list(
            @RequestParam(required = false) String roleId, @RequestParam(required = false) String roleName,
            @RequestParam(required = false) String roleCode, @RequestParam(required = false) Integer pageIndex,
            @RequestParam(required = false) Integer pageSize) {
        PageResult result = roleApplication.getRolePage(roleId, roleName, roleCode, pageIndex, pageSize);
        return SaResult.data(result);
    }

    /**
     * 获取角色树
     *
     * @param roleId 角色id
     */
    @GetMapping(value = "/tree")
    public SaResult tree(@RequestParam(required = false) String roleId) {
        List<RoleTreeModel> result = roleApplication.getRoleTree(roleId);
        return SaResult.data(result);
    }

    /**
     * 获取角色信息
     *
     * @param id 角色id
     */
    @GetMapping(value = "/{id}")
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
    @PostMapping(value = "/add")
    public SaResult add(String roleName, String roleCode, @RequestParam(required = false) String parentId) {
        return roleApplication.addRole(roleName, roleCode, parentId);
    }

    /**
     * 删除角色
     *
     * @param id 角色id
     */
    @DeleteMapping(value = "/{id}")
    public SaResult delete(@PathVariable String id) {
        return roleApplication.deleteRole(id);
    }

    /**
     * 更新角色信息
     *
     * @param id       角色id
     * @param roleName 角色名
     * @param roleCode 角色代码
     * @param parentId 父级id
     */
    @PatchMapping(value = "/{id}")
    public SaResult update(
            @PathVariable String id, String roleName, String roleCode,
            @RequestParam(required = false) String parentId) {
        return roleApplication.updateRole(id, roleName, roleCode, parentId);
    }
}
