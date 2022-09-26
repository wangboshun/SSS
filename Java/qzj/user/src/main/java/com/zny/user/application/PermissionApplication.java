package com.zny.user.application;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zny.common.enums.ResourceEnum;
import com.zny.common.resource.ResourceApplication;
import com.zny.common.resource.ResourceModel;
import com.zny.common.utils.DateUtils;
import com.zny.user.mapper.PermissionMapper;
import com.zny.user.model.permission.PermissionModel;
import com.zny.user.model.permission.PermissionTreeModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author WBS
 * Date:2022/9/2
 */

@Service
public class PermissionApplication extends ServiceImpl<PermissionMapper, PermissionModel> {

    private final ResourceApplication resourceApplication;

    public PermissionApplication(ResourceApplication resourceApplication) {
        this.resourceApplication = resourceApplication;
    }

    /**
     * 添加权限
     *
     * @param permissionName 权限名
     * @param permissionCode 权限代码
     * @param parentId       父级id
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
     * 查询权限树
     *
     * @param permissionId 权限id
     */
    public List<PermissionTreeModel> getPermissionTree(String permissionId) {
        List<PermissionTreeModel> list = new ArrayList<>();

        //查找根目录
        if (permissionId == null) {
            QueryWrapper<PermissionModel> wrapper = new QueryWrapper<PermissionModel>();

            //查找没有父级id的权限
            wrapper.isNull("parent_id");
            List<PermissionModel> permissionList = this.list(wrapper);
            for (PermissionModel permission : permissionList) {
                list.add(getChildren(permission.getId(), permission.getPermission_name(), 1));
            }
        }
        else {
            QueryWrapper<PermissionModel> wrapper = new QueryWrapper<PermissionModel>();
            wrapper.eq(StringUtils.isNotBlank(permissionId), "id", permissionId);
            PermissionModel permission = this.getOne(wrapper);
            list.add(getChildren(permission.getId(), permission.getPermission_name(), 1));
        }
        return list;
    }

    /**
     * 获取子级
     *
     * @param permissionId 权限id
     * @param level        树形等级
     */
    private PermissionTreeModel getChildren(String permissionId, String permissionName, Integer level) {
        PermissionTreeModel tree = new PermissionTreeModel();
        QueryWrapper<PermissionModel> wrapper = new QueryWrapper<PermissionModel>();
        wrapper.eq(StringUtils.isNotBlank(permissionId), "parent_id", permissionId);
        List<PermissionModel> children = this.list(wrapper);
        tree.setId(permissionId);
        tree.setLelvel(level);
        tree.setPermission_name(permissionName);
        if (children.size() > 0) {
            for (PermissionModel permission : children) {
                tree.setChildren(getChildren(permission.getId(), permission.getPermission_name(), level + 1));
            }
        }
        return tree;
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
            String permissionId, String permissionName, String permissionCode, Integer pageIndex, Integer pageSize) {
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
        Map<String, Object> map = new HashMap<>(4);
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
     * @param parentId       父级id
     */
    public SaResult updatePermission(String id, String permissionName, String permissionCode, String parentId) {
        QueryWrapper<PermissionModel> wrapper = new QueryWrapper<PermissionModel>();
        wrapper.eq("id", id);
        PermissionModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResult.error("权限不存在！");
        }
        if (StringUtils.isNotBlank(parentId)) {
            model.setParent_id(parentId);
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

    /**
     * 根据用户获取权限
     *
     * @param userId 用户id
     */
    public List<PermissionModel> getPermissionByUser(String userId) {
        List<ResourceModel> resourceList = resourceApplication.getResourceList(userId, ResourceEnum.USER.getIndex(), ResourceEnum.PERMISSION.getIndex());
        List<PermissionModel> permissionList = new ArrayList<PermissionModel>(getPermissionByResourceModel(resourceList));

        //获取所有角色
        List<String> roleList = resourceApplication.getRoleByUser(userId);

        //遍历角色id，获取资源
        for (String roleId : roleList) {
            permissionList.addAll(getPermissionByRole(roleId));
        }

        return permissionList;
    }


    /**
     * 根据角色获取权限
     *
     * @param roleId 角色id
     */
    public List<PermissionModel> getPermissionByRole(String roleId) {
        List<ResourceModel> resourceList = resourceApplication.getResourceList(roleId, ResourceEnum.ROLE.getIndex(), ResourceEnum.PERMISSION.getIndex());
        return new ArrayList<PermissionModel>(getPermissionByResourceModel(resourceList));
    }

    /**
     * 根据资源映射获取权限
     *
     * @param list 资源列表
     */
    private List<PermissionModel> getPermissionByResourceModel(List<ResourceModel> list) {
        List<PermissionModel> permissionList = new ArrayList<PermissionModel>();
        for (ResourceModel resourceModel : list) {
            PermissionModel permissionModel = this.getById(resourceModel.getSlave_id());
            permissionList.add(permissionModel);
        }
        return permissionList;
    }

    /**
     * 绑定菜单到用户
     *
     * @param userId       用户id
     * @param permissionId 权限id
     */
    public SaResult bindPermissionByUser(String userId, String[] permissionId) {
        return resourceApplication.addResource(userId, ResourceEnum.USER.getIndex(), permissionId, ResourceEnum.PERMISSION.getIndex());
    }

    /**
     * 绑定菜单到角色
     *
     * @param roleId       角色id
     * @param permissionId 权限id
     */
    public SaResult bindPermissionByRole(String roleId, String[] permissionId) {
        return resourceApplication.addResource(roleId, ResourceEnum.ROLE.getIndex(), permissionId, ResourceEnum.PERMISSION.getIndex());
    }

    /**
     * 解绑权限到用户
     *
     * @param userId       用户id
     * @param permissionId id
     */
    public SaResult unBindPermissionByUser(String userId, String[] permissionId) {
        return resourceApplication.deleteResource(null, userId, ResourceEnum.USER.getIndex(), permissionId, ResourceEnum.PERMISSION.getIndex());
    }

    /**
     * 解绑权限到角色
     *
     * @param roleId       角色id
     * @param permissionId id
     */
    public SaResult unBindPermissionByRole(String roleId, String[] permissionId) {
        return resourceApplication.deleteResource(null, roleId, ResourceEnum.ROLE.getIndex(), permissionId, ResourceEnum.PERMISSION.getIndex());
    }
}
