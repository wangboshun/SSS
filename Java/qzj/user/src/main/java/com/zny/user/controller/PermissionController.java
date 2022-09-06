package com.zny.user.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.user.application.user.PermissionApplication;
import com.zny.user.model.PermissionModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author WBS
 * Date:2022/9/6
 */

@RestController
@RequestMapping("/user/permission")
@Tag(name = "permission", description = "权限模块")
public class PermissionController {

    @Autowired
    private PermissionApplication permissionApplication;

    /**
     * 获取权限列表
     *
     * @param permissionId   权限id
     * @param permissionName 权限名
     * @param permissionCode 权限代码
     * @param pageSize       分页大小
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public SaResult list(@RequestParam(required = false) String permissionId, @RequestParam(required = false) String permissionName, @RequestParam(required = false) String permissionCode, @RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
        Map<String, Object> result = permissionApplication.getPermissionList(permissionId, permissionName, permissionCode, pageIndex, pageSize);
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
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public SaResult add(String permissionName, String permissionCode) {
        return permissionApplication.addPermission(permissionName, permissionCode);
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
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public SaResult update(@PathVariable String id, String permissionName, String permissionCode) {
        return permissionApplication.updatePermission(id, permissionName, permissionCode);
    }
}
