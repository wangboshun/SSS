package com.zny.user.application;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zny.common.enums.ResourceEnum;
import com.zny.common.model.PageResult;
import com.zny.common.resource.ResourceApplication;
import com.zny.common.result.MessageCodeEnum;
import com.zny.common.result.SaResultEx;
import com.zny.common.utils.DateUtils;
import com.zny.common.utils.PageUtils;
import com.zny.user.mapper.PermissionMapper;
import com.zny.user.model.permission.PermissionModel;
import com.zny.user.model.permission.PermissionTreeModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author WBS
 * Date:2022/9/2
 * 权限服务类
 */

@Service
@DS("main")
public class PermissionApplication extends ServiceImpl<PermissionMapper, PermissionModel> {

    private final ResourceApplication resourceApplication;

    public PermissionApplication(ResourceApplication resourceApplication) {
        this.resourceApplication = resourceApplication;
    }

    /**
     * 根据id获取权限信息
     *
     * @param id id
     */
    public SaResult getPermissionById(String id) {
        if (resourceApplication.haveResource(id, ResourceEnum.PERMISSION)) {
            PermissionModel model = this.getById(id);
            if (model == null) {
                return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "权限不存在！");
            }
            return SaResult.data(model);
        } else {
            return SaResultEx.error(MessageCodeEnum.AUTH_INVALID);
        }
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
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "权限已存在！");
        }
        model = new PermissionModel();
        model.setId(UUID.randomUUID().toString());
        model.setPermission_name(permissionName);
        model.setParent_id(parentId);
        model.setCreate_time(DateUtils.dateToStr(LocalDateTime.now()));
        model.setPermission_code(permissionCode);
        if (save(model)) {
            return SaResult.ok("添加权限成功！");
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "添加权限失败！");
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
        } else {
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
    public PageResult getPermissionPage(
            String permissionId, String permissionName, String permissionCode, Integer pageIndex, Integer pageSize) {
        pageSize = PageUtils.getPageSize(pageSize);
        pageIndex = PageUtils.getPageIndex(pageIndex);
        QueryWrapper<PermissionModel> wrapper = new QueryWrapper<PermissionModel>();
        if (!resourceApplication.haveResource(wrapper, permissionId, "id", ResourceEnum.PERMISSION)) {
            return null;
        }
        wrapper.eq(StringUtils.isNotBlank(permissionName), "permission_name", permissionName);
        wrapper.eq(StringUtils.isNotBlank(permissionCode), "permission_code", permissionCode);
        wrapper.orderByDesc("create_time");
        Page<PermissionModel> page = new Page<>(pageIndex, pageSize);
        Page<PermissionModel> result = this.page(page, wrapper);
        PageResult pageResult = new PageResult();
        pageResult.setPages(result.getPages());
        pageResult.setRows(result.getRecords());
        pageResult.setTotal(result.getTotal());
        pageResult.setCurrent(result.getCurrent());
        return pageResult;
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
            return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "权限不存在！");
        }
        if (removeById(id)) {
            return SaResult.ok("删除权限成功！");
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "删除权限失败！");
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
            return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "权限不存在！");
        }
        if (StringUtils.isNotBlank(parentId)) {
            model.setParent_id(parentId);
        }
        if (StringUtils.isNotBlank(permissionName)) {
            model.setPermission_name(permissionName);
        }
        if (StringUtils.isNotBlank(permissionCode)) {
            model.setPermission_code(permissionCode);
        }

        if (updateById(model)) {
            return SaResult.ok("更新权限信息成功！");
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "删除权限信息失败！");
        }
    }

    /**
     * 根据用户获取权限
     *
     * @param userId 用户id
     */
    public List<PermissionModel> getPermissionByUser(String userId) {
        Set<String> ids = resourceApplication.getIdsByUser(userId, ResourceEnum.PERMISSION);
        List<PermissionModel> permissionList = new ArrayList<PermissionModel>(getPermissionByIds(ids));

        //获取所有角色
        Set<String> roleList = resourceApplication.getRoleByUser(userId);

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
        Set<String> ids = resourceApplication.getIdsByRole(roleId, ResourceEnum.PERMISSION);
        return getPermissionByIds(ids);
    }

    /**
     * 根据资源映射获取权限
     *
     * @param ids 资源id
     */
    private List<PermissionModel> getPermissionByIds(Set<String> ids) {
        List<PermissionModel> list = new ArrayList<PermissionModel>();
        if (ids == null || ids.isEmpty()) {
            return list;
        }
        for (String id : ids) {
            PermissionModel model = this.getById(id);
            list.add(model);
        }
        return list;
    }

    /**
     * 绑定菜单到用户
     *
     * @param userIds       用户id
     * @param permissionIds 权限id
     */
    public SaResult bindPermissionByUser(String[] userIds, String[] permissionIds) {
        if (permissionIds == null || permissionIds.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.addResource(userIds, ResourceEnum.USER.getIndex(), permissionIds, ResourceEnum.PERMISSION.getIndex());
    }

    /**
     * 绑定菜单到角色
     *
     * @param roleIds       角色id
     * @param permissionIds 权限id
     */
    public SaResult bindPermissionByRole(String[] roleIds, String[] permissionIds) {
        if (permissionIds == null || permissionIds.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.addResource(roleIds, ResourceEnum.ROLE.getIndex(), permissionIds, ResourceEnum.PERMISSION.getIndex());
    }

    /**
     * 解绑权限到用户
     *
     * @param userId       用户id
     * @param permissionId id
     */
    public SaResult unBindPermissionByUser(String[] userId, String[] permissionId) {
        if (permissionId == null || permissionId.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.deleteResource(null, userId, ResourceEnum.USER.getIndex(), permissionId, ResourceEnum.PERMISSION.getIndex());
    }

    /**
     * 解绑权限到角色
     *
     * @param roleId       角色id
     * @param permissionId id
     */
    public SaResult unBindPermissionByRole(String[] roleId, String[] permissionId) {
        if (permissionId == null || permissionId.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.deleteResource(null, roleId, ResourceEnum.ROLE.getIndex(), permissionId, ResourceEnum.PERMISSION.getIndex());
    }
}
