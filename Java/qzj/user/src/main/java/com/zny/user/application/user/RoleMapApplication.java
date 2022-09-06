package com.zny.user.application.user;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zny.common.utils.DateUtils;
import com.zny.user.mapper.PermissionMapper;
import com.zny.user.mapper.RoleMapMapper;
import com.zny.user.mapper.RoleMapper;
import com.zny.user.model.PermissionModel;
import com.zny.user.model.RoleMapEnum;
import com.zny.user.model.RoleMapModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author WBS
 * Date:2022/9/2
 */

@Service
public class RoleMapApplication extends ServiceImpl<RoleMapMapper, RoleMapModel> {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;


    /**
     * 添加映射
     *
     * @param mapId   id
     * @param mapType 类型
     * @param roleId  角色id
     */
    public SaResult addRoleMap(String roleId, String mapId, Integer mapType) {
        String mapName = getMapName(mapId, mapType);
        if (StringUtils.isBlank(mapName)) {
            return SaResult.error("资源不存在！");
        }

        QueryWrapper<RoleMapModel> wrapper = new QueryWrapper<RoleMapModel>();
        wrapper.eq("map_id", mapId);
        wrapper.eq("role_id", roleId);
        wrapper.eq("map_type", mapType);
        RoleMapModel model = this.getOne(wrapper);
        if (model != null) {
            return SaResult.error("映射已存在！");
        }
        RoleMapModel roleMapModel = new RoleMapModel();
        roleMapModel.setId(UUID.randomUUID().toString());
        roleMapModel.setCreate_time(DateUtils.dateToStr(LocalDateTime.now()));
        roleMapModel.setMap_id(mapId);
        roleMapModel.setRole_id(roleId);
        roleMapModel.setMap_name(mapName);
        roleMapModel.setMap_type(mapType);
        if (save(roleMapModel)) {
            return SaResult.ok("添加映射成功！");
        } else {
            return SaResult.error("添加映射失败！");
        }
    }

    /**
     * 查询映射列表
     *
     * @param mapName   映射名
     * @param mapId     映射代码
     * @param pageIndex 页码
     * @param pageSize  分页大小
     */
    public Map<String, Object> getRoleMapList(String id, String roleId, String mapId, String mapName, Integer mapType, Integer pageIndex, Integer pageSize) {
        if (pageSize == null) {
            pageSize = 10;
        }
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        QueryWrapper<RoleMapModel> wrapper = new QueryWrapper<RoleMapModel>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(StringUtils.isNotBlank(roleId), "role_id", roleId);
        wrapper.eq(StringUtils.isNotBlank(mapName), "map_name", mapName);
        wrapper.eq(StringUtils.isNotBlank(mapId), "map_id", mapId);
        wrapper.eq(mapType != null, "map_type", mapType);
        Page<RoleMapModel> page = new Page<>(pageIndex, pageSize);
        Page<RoleMapModel> result = this.page(page, wrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("total", result.getTotal());
        map.put("rows", result.getRecords());
        map.put("pages", result.getPages());
        map.put("current", result.getCurrent());
        return map;
    }

    /**
     * 删除映射
     *
     * @param roleId 角色id
     */
    public SaResult deleteByRoleId(String roleId) {
        QueryWrapper<RoleMapModel> wrapper = new QueryWrapper<RoleMapModel>();
        wrapper.eq("role_id", roleId);
        if (remove(wrapper)) {
            return SaResult.ok("删除映射成功！");
        } else {
            return SaResult.error("删除映射失败！");
        }
    }

    /**
     * 更新映射信息
     *
     * @param id      映射id
     * @param roleId  角色id
     * @param mapType 类型
     * @param mapId   映射代码
     */
    public SaResult updateRoleMap(String id, String roleId, String mapId, Integer mapType) {

        QueryWrapper<RoleMapModel> wrapper = new QueryWrapper<RoleMapModel>();
        wrapper.eq("id", id);
        RoleMapModel model = this.getOne(wrapper);
        if (model == null) {
            return SaResult.error("映射不存在！");
        }

        String mapName = getMapName(mapId, mapType);
        if (StringUtils.isBlank(mapName)) {
            return SaResult.error("资源不存在！");
        }

        model.setMap_type(mapType);
        model.setRole_id(roleId);
        model.setMap_name(mapName);
        model.setMap_id(mapId);
        if (updateById(model)) {
            return SaResult.ok("更新映射信息成功！");
        } else {
            return SaResult.error("删除映射信息失败！");
        }
    }

    /**
     * 根据id获取资源名称
     *
     * @param mapId   id
     * @param mapType 类型
     */
    private String getMapName(String mapId, int mapType) {
        String mapName = "";
        RoleMapEnum e = RoleMapEnum.values()[mapType];
        switch (e) {
            //判断权限是否存在
            case PERMISSION:
                PermissionModel permission = permissionMapper.selectById(mapId);
                if (permission == null) {
                    return null;
                }
                mapName = permission.getPermission_name();
                break;
            //判断菜单是否存在
            case MENU:
                break;
            default:
                break;
        }
        return mapName;
    }
}
