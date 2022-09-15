package com.zny.user.application;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zny.common.utils.DateUtils;
import com.zny.user.mapper.RoleMapper;
import com.zny.user.model.role.RoleModel;
import com.zny.user.model.role.RoleTreeModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author WBS
 * Date:2022/9/2
 */

@Service
public class RoleApplication extends ServiceImpl<RoleMapper, RoleModel> {

    /**
     * 添加角色
     *
     * @param roleName 角色名
     * @param roleCode 角色代码
     */
    public SaResult addRole(String roleName, String roleCode, String parentId) {
        QueryWrapper<RoleModel> wrapper = new QueryWrapper<RoleModel>();
        wrapper.eq("role_name", roleName);
        RoleModel model = this.getOne(wrapper);
        if (model != null) {
            return SaResult.error("角色名已存在！");
        }
        RoleModel roleModel = new RoleModel();
        roleModel.setId(UUID.randomUUID().toString());
        roleModel.setRole_name(roleName);
        roleModel.setParent_id(parentId);
        roleModel.setCreate_time(DateUtils.dateToStr(LocalDateTime.now()));
        roleModel.setRole_code(roleCode);
        if (save(roleModel)) {
            return SaResult.ok("添加角色成功！");
        }
        else {
            return SaResult.error("添加角色失败！");
        }
    }

    /**
     * 查询角色树
     *
     * @param roleId 角色id
     */
    public List<RoleTreeModel> getRoleTree(String roleId) {
        List<RoleTreeModel> list = new ArrayList<>();
        if (roleId == null) {
            QueryWrapper<RoleModel> wrapper = new QueryWrapper<RoleModel>();
            wrapper.isNull("parent_id");
            List<RoleModel> permissionList = this.list(wrapper);
            for (RoleModel role : permissionList) {
                list.add(getChildren(role.getId(), role.getRole_name(), 1));
            }
        }
        else {
            QueryWrapper<RoleModel> wrapper = new QueryWrapper<RoleModel>();
            wrapper.eq(StringUtils.isNotBlank(roleId), "id", roleId);
            RoleModel role = this.getOne(wrapper);
            list.add(getChildren(role.getId(), role.getRole_name(), 1));
        }
        return list;
    }

    /**
     * 获取目录树
     *
     * @param roleId 角色id
     * @param level  树形等级
     */
    private RoleTreeModel getChildren(String roleId, String roleName, Integer level) {
        RoleTreeModel tree = new RoleTreeModel();
        QueryWrapper<RoleModel> wrapper = new QueryWrapper<RoleModel>();
        wrapper.eq(StringUtils.isNotBlank(roleId), "parent_id", roleId);
        List<RoleModel> children = this.list(wrapper);
        tree.setId(roleId);
        tree.setLelvel(level);
        tree.setRole_name(roleName);
        if (children.size() > 0) {
            for (RoleModel role : children) {
                tree.setChildren(getChildren(role.getId(), role.getRole_name(), level + 1));
            }
        }
        return tree;
    }

    /**
     * 查询角色列表
     *
     * @param roleName  角色名
     * @param roleCode  角色代码
     * @param pageIndex 页码
     * @param pageSize  分页大小
     */
    public Map<String, Object> getRoleList(
            String roleId, String roleName, String roleCode, Integer pageIndex, Integer pageSize) {
        if (pageSize == null) {
            pageSize = 10;
        }
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        QueryWrapper<RoleModel> wrapper = new QueryWrapper<RoleModel>();
        wrapper.eq(StringUtils.isNotBlank(roleId), "id", roleId);
        wrapper.eq(StringUtils.isNotBlank(roleName), "role_name", roleName);
        wrapper.eq(StringUtils.isNotBlank(roleCode), "role_code", roleCode);
        Page<RoleModel> page = new Page<>(pageIndex, pageSize);
        Page<RoleModel> result = this.page(page, wrapper);
        Map<String, Object> map = new HashMap<>(4);
        map.put("total", result.getTotal());
        map.put("rows", result.getRecords());
        map.put("pages", result.getPages());
        map.put("current", result.getCurrent());
        return map;
    }

    /**
     * 删除角色
     *
     * @param id 角色id
     */
    public SaResult deleteRole(String id) {
        QueryWrapper<RoleModel> wrapper = new QueryWrapper<RoleModel>();
        wrapper.eq("id", id);
        RoleModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResult.error("角色不存在！");
        }
        if (removeById(id)) {
            return SaResult.ok("删除角色成功！");
        }
        else {
            return SaResult.error("删除角色失败！");
        }
    }

    /**
     * 更新角色信息
     *
     * @param id       角色id
     * @param roleName 角色名
     * @param roleCode 角色代码
     */
    public SaResult updateRole(String id, String roleName, String roleCode) {
        QueryWrapper<RoleModel> wrapper = new QueryWrapper<RoleModel>();
        wrapper.eq("id", id);
        RoleModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResult.error("角色不存在！");
        }
        model.setRole_name(roleName);
        model.setRole_code(roleCode);
        if (updateById(model)) {
            return SaResult.ok("更新角色信息成功！");
        }
        else {
            return SaResult.error("删除角色信息失败！");
        }
    }
}
