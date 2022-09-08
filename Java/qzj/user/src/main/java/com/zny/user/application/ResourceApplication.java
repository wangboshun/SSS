package com.zny.user.application;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.zny.common.utils.DateUtils;
import com.zny.user.mapper.*;
import com.zny.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author WBS
 * Date:2022/9/2
 */

@Service
public class ResourceApplication extends ServiceImpl<ResourceMapper, ResourceModel> {
    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private MenuMapper menMapper;

    /**
     * 添加资源
     */
    public SaResult addResource(String mainId, int mainType, String slaveId, int slaveType) {
        Map<String, String> resourceInfo = getResourceInfo(mainId, mainType);
        String mainName = resourceInfo.get("name");

        resourceInfo = getResourceInfo(slaveId, slaveType);
        String slaveName = resourceInfo.get("name");
        String slaveCode = resourceInfo.get("code");

        if (StringUtils.isBlank(mainName)) {
            return SaResult.error("资源不存在！");
        }
        if (StringUtils.isBlank(slaveName)) {
            return SaResult.error("资源不存在！");
        }

        QueryWrapper<ResourceModel> wrapper = new QueryWrapper<ResourceModel>();
        wrapper.eq("main_id", mainId);
        wrapper.eq("main_type", mainType);

        wrapper.eq("slave_id", slaveId);
        wrapper.eq("slave_type", slaveType);
        ResourceModel model = this.getOne(wrapper);
        if (model != null) {
            return SaResult.error("资源已存在！");
        }
        ResourceModel resourceModel = new ResourceModel();
        resourceModel.setId(UUID.randomUUID().toString());
        resourceModel.setCreate_time(DateUtils.dateToStr(LocalDateTime.now()));

        resourceModel.setMain_id(mainId);
        resourceModel.setMain_type(mainType);
        resourceModel.setMain_name(mainName);

        resourceModel.setSlave_id(slaveId);
        resourceModel.setSlave_type(slaveType);
        resourceModel.setSlave_name(slaveName);
        resourceModel.setSlave_code(slaveCode);

        if (save(resourceModel)) {
            return SaResult.ok("添加资源成功！");
        }
        else {
            return SaResult.error("添加资源失败！");
        }
    }

    /**
     * 获取映射表
     *
     * @param mainId    主id
     * @param mainType  主类型
     * @param slaveType 关联类型
     */
    private Table<String, String, String> getTable(String mainId, Integer mainType, Integer slaveType) {
        Table<String, String, String> table = HashBasedTable.create();
        QueryWrapper<ResourceModel> wrapper = new QueryWrapper<ResourceModel>();
        wrapper.eq("main_id", mainId);
        wrapper.eq("main_type", mainType);
        wrapper.eq("slave_type", slaveType);
        List<ResourceModel> list = this.list(wrapper);
        for (ResourceModel resourceModel : list) {
            table.put(resourceModel.getSlave_id(), resourceModel.getSlave_name(), resourceModel.getSlave_code());
        }
        return table;
    }

    /**
     * 根据用户获取角色
     *
     * @param userId 用户id
     */
    public Table<String, String, String> getRoleByUser(String userId) {
        return getTable(userId, ResourceEnum.USER.getIndex(), ResourceEnum.ROLE.getIndex());
    }

    /**
     * 根据用户获取菜单
     *
     * @param userId 用户id
     */
    public Table<String, String, String> getMenuByUser(String userId) {
        Table<String, String, String> table = HashBasedTable.create();
        Table<String, String, String> roles = getRoleByUser(userId);

        //循环所有roleId
        for (String roleId : roles.rowKeySet()) {
            //根据roleId获取菜单
            Table<String, String, String> tmp = getMenuByRole(roleId);
            if (!tmp.isEmpty()) {
                //table添加角色关联得到菜单
                table.putAll(tmp);
            }
        }

        QueryWrapper<ResourceModel> wrapper = new QueryWrapper<ResourceModel>();
        wrapper.eq("main_id", userId);
        wrapper.eq("main_type", ResourceEnum.USER.getIndex());
        wrapper.eq("slave_type", ResourceEnum.MENU.getIndex());
        List<ResourceModel> list = this.list(wrapper);
        for (ResourceModel resourceModel : list) {
            table.put(resourceModel.getSlave_id(), resourceModel.getSlave_name(), resourceModel.getSlave_code());
        }
        return table;

    }

    /**
     * 根据用户获取权限
     *
     * @param userId 用户id
     */
    public Table<String, String, String> getPermissionByUser(String userId) {
        Table<String, String, String> table = HashBasedTable.create();
        Table<String, String, String> roles = getRoleByUser(userId);

        //循环所有roleId
        for (String roleId : roles.rowKeySet()) {
            //根据roleId获取权限
            Table<String, String, String> tmp = getPermissionByRole(roleId);
            if (!tmp.isEmpty()) {
                //table添加角色关联得到权限
                table.putAll(tmp);
            }
        }

        QueryWrapper<ResourceModel> wrapper = new QueryWrapper<ResourceModel>();
        wrapper.eq("main_id", userId);
        wrapper.eq("main_type", ResourceEnum.USER.getIndex());
        wrapper.eq("slave_type", ResourceEnum.PERMISSION.getIndex());
        List<ResourceModel> list = this.list(wrapper);
        for (ResourceModel resourceModel : list) {
            table.put(resourceModel.getSlave_id(), resourceModel.getSlave_name(), resourceModel.getSlave_code());
        }
        return table;
    }

    /**
     * 根据角色获取用户
     *
     * @param roleId 角色id
     */
    public Table<String, String, String> getUserByRole(String roleId) {
        return getTable(roleId, ResourceEnum.ROLE.getIndex(), ResourceEnum.USER.getIndex());
    }

    /**
     * 根据角色获取菜单
     *
     * @param roleId 角色id
     */
    public Table<String, String, String> getMenuByRole(String roleId) {
        return getTable(roleId, ResourceEnum.ROLE.getIndex(), ResourceEnum.MENU.getIndex());
    }

    /**
     * 根据角色获取权限
     *
     * @param roleId 角色id
     */
    public Table<String, String, String> getPermissionByRole(String roleId) {
        return getTable(roleId, ResourceEnum.ROLE.getIndex(), ResourceEnum.PERMISSION.getIndex());
    }

    /**
     * 查询资源列表
     */
    public Map<String, Object> getResourceList(
            String id, String mainId, Integer mainType, String slaveId, Integer slaveType, Integer pageIndex,
            Integer pageSize) {
        if (pageSize == null) {
            pageSize = 10;
        }
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        QueryWrapper<ResourceModel> wrapper = new QueryWrapper<ResourceModel>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        wrapper.eq(StringUtils.isNotBlank(mainId), "main_id", mainId);
        wrapper.eq(mainType != null, "main_type", mainType);

        wrapper.eq(StringUtils.isNotBlank(slaveId), "slave_id", slaveId);
        wrapper.eq(slaveType != null, "slave_type", slaveType);

        Page<ResourceModel> page = new Page<>(pageIndex, pageSize);
        Page<ResourceModel> result = this.page(page, wrapper);

        Map<String, Object> map = new HashMap<>();
        map.put("total", result.getTotal());
        map.put("rows", result.getRecords());
        map.put("pages", result.getPages());
        map.put("current", result.getCurrent());
        return map;
    }

    /**
     * 删除资源
     */
    public SaResult forMain(String mainId, Integer mainType) {
        QueryWrapper<ResourceModel> wrapper = new QueryWrapper<ResourceModel>();
        wrapper.eq("main_id", mainId);
        wrapper.eq("main_type", mainType);
        if (remove(wrapper)) {
            return SaResult.ok("删除资源成功！");
        }
        else {
            return SaResult.error("删除资源失败！");
        }
    }

    /**
     * 更新资源信息
     */
    public SaResult updateResource(String id, String mainId, Integer mainType, String slaveId, Integer slaveType) {

        QueryWrapper<ResourceModel> wrapper = new QueryWrapper<ResourceModel>();
        wrapper.eq("id", id);
        ResourceModel model = this.getOne(wrapper);
        if (model == null) {
            return SaResult.error("资源不存在！");
        }

        Map<String, String> resourceInfo = getResourceInfo(mainId, mainType);
        String mainName = resourceInfo.get("name");

        resourceInfo = getResourceInfo(slaveId, slaveType);
        String slaveName = resourceInfo.get("name");
        String slaveCode = resourceInfo.get("code");

        if (StringUtils.isBlank(mainName)) {
            return SaResult.error("资源不存在！");
        }
        if (StringUtils.isBlank(slaveName)) {
            return SaResult.error("资源不存在！");
        }

        model.setMain_id(mainId);
        model.setMain_type(mainType);
        model.setMain_name(mainName);

        model.setSlave_id(slaveId);
        model.setSlave_type(slaveType);
        model.setSlave_name(slaveName);
        model.setSlave_code(slaveCode);

        if (updateById(model)) {
            return SaResult.ok("更新资源信息成功！");
        }
        else {
            return SaResult.error("删除资源信息失败！");
        }
    }

    /**
     * 根据id获取资源信息
     */
    private Map<String, String> getResourceInfo(String resourceId, int resourceType) {
        String name = "";
        String code = "";
        ResourceEnum e = ResourceEnum.values()[resourceType];
        switch (e) {
            //判断角色是否存在
            case ROLE:
                RoleModel role = roleMapper.selectById(resourceId);
                if (role == null) {
                    return null;
                }
                name = role.getRole_name();
                code = role.getRole_code();
                break;
            //判断用户是否存在
            case USER:
                UserModel user = userMapper.selectById(resourceId);
                if (user == null) {
                    return null;
                }
                name = user.getUser_name();
                break;
            //判断权限是否存在
            case PERMISSION:
                PermissionModel permission = permissionMapper.selectById(resourceId);
                if (permission == null) {
                    return null;
                }
                name = permission.getPermission_name();
                code = permission.getPermission_code();
                break;
            //判断菜单是否存在
            case MENU:
                MenuModel menu = menMapper.selectById(resourceId);
                if (menu == null) {
                    return null;
                }
                name = menu.getMenu_name();
                code = menu.getMenu_code();
                break;
            default:
                break;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", name);
        map.put("code", code);
        return map;
    }
}
