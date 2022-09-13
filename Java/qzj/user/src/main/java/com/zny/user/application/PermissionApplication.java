package com.zny.user.application;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zny.common.utils.DateUtils;
import com.zny.user.mapper.PermissionMapper;
import com.zny.user.model.PermissionModel;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author WBS
 * Date:2022/9/2
 */

@Service
public class PermissionApplication extends ServiceImpl<PermissionMapper, PermissionModel> {

    /**
     * 添加权限
     *
     * @param permissionName 权限名
     * @param permissionCode 权限代码
     */
    public SaResult addPermission(String permissionName, String permissionCode, String parentId) {
        QueryWrapper<PermissionModel> wrapper = new QueryWrapper<PermissionModel>();
        wrapper.eq("permission_name", permissionName);
        PermissionModel model = this.getOne(wrapper);
        if (model != null) {
            return SaResult.error("权限名已存在！");
        }
        PermissionModel permissionModel = new PermissionModel();
        permissionModel.setId(UUID.randomUUID().toString());
        permissionModel.setPermission_name(permissionName);
        permissionModel.setParent_id(parentId);
        permissionModel.setCreate_time(DateUtils.dateToStr(LocalDateTime.now()));
        permissionModel.setPermission_code(permissionCode);
        if (save(permissionModel)) {
            return SaResult.ok("添加权限成功！");
        }
        else {
            return SaResult.error("添加权限失败！");
        }
    }

    /**
     * 查询权限列表
     *
     * @param permissionName 权限名
     * @param permissionCode 权限代码
     * @param pageIndex      页码
     * @param pageSize       分页大小
     */
    public Map<String, Object> getPermissionList(
            String permissionId, String permissionName, String permissionCode, Integer pageIndex,
            Integer pageSize) {
        if (pageSize == null) {
            pageSize = 10;
        }
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        QueryWrapper<PermissionModel> wrapper = new QueryWrapper<PermissionModel>();
        wrapper.eq(StringUtils.isNotBlank(permissionId), "id", permissionId);
        wrapper.eq(StringUtils.isNotBlank(permissionName), "permission_name", permissionName);
        wrapper.eq(StringUtils.isNotBlank(permissionCode), "permission_code", permissionCode);
        Page<PermissionModel> page = new Page<>(pageIndex, pageSize);
        Page<PermissionModel> result = this.page(page, wrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("total", result.getTotal());
        map.put("rows", result.getRecords());
        map.put("pages", result.getPages());
        map.put("current", result.getCurrent());
        return map;
    }

    /**
     * 删除权限
     *
     * @param id 用户id
     */
    public SaResult deletePermission(String id) {
        QueryWrapper<PermissionModel> wrapper = new QueryWrapper<PermissionModel>();
        wrapper.eq("id", id);
        PermissionModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResult.error("权限不存在！");
        }
        if (removeById(id)) {
            return SaResult.ok("删除权限成功！");
        }
        else {
            return SaResult.error("删除权限失败！");
        }
    }

    /**
     * 更新权限信息
     *
     * @param id             权限id
     * @param permissionName 权限名
     * @param permissionCode 权限代码
     */
    public SaResult updatePermission(String id, String permissionName, String permissionCode) {
        QueryWrapper<PermissionModel> wrapper = new QueryWrapper<PermissionModel>();
        wrapper.eq("id", id);
        PermissionModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResult.error("权限不存在！");
        }
        model.setPermission_name(permissionName);
        model.setPermission_code(permissionCode);
        if (updateById(model)) {
            return SaResult.ok("更新权限信息成功！");
        }
        else {
            return SaResult.error("删除权限信息失败！");
        }
    }
}
