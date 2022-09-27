package com.zny.iot.application;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zny.common.enums.ResourceEnum;
import com.zny.common.enums.UserTypeEnum;
import com.zny.common.resource.ResourceApplication;
import com.zny.common.resource.ResourceModel;
import com.zny.iot.mapper.StationBaseSetMapper;
import com.zny.iot.model.StationBaseSetModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/9/26
 */

@Service
@DS("iot")
public class StationBaseSetApplication extends ServiceImpl<StationBaseSetMapper, StationBaseSetModel> {

    private final ResourceApplication resourceApplication;

    public StationBaseSetApplication(ResourceApplication resourceApplication) {
        this.resourceApplication = resourceApplication;
    }

    /**
     * 获取测站列表
     *
     * @param stationId 测站id
     * @param pageIndex 页码
     * @param pageSize  分页大小
     */
    public Map<String, Object> getStationList(String stationId, Integer pageIndex, Integer pageSize) {
        if (pageSize == null) {
            pageSize = 10;
        }
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }

        QueryWrapper<StationBaseSetModel> wrapper = new QueryWrapper<StationBaseSetModel>();
        Object userType = StpUtil.getSession().get("userType");
        //如果不是超级管理员
        if (!userType.equals(UserTypeEnum.SUPER.getIndex())) {
            String userId = (String) StpUtil.getSession().get("userId");
            List<String> ids = getIdsByUser(userId);
            if (ids.size() < 1) {
                return null;
            }
            else {
                //如果有前端where条件
                if (stationId != null) {
                    //判断在资源范围内
                    if (ids.stream().anyMatch(x -> x.equals(stationId))) {
                        wrapper.eq("StationID", stationId);
                    }
                    else {
                        return null;
                    }
                }
                //如果没有前端where条件
                else {
                    wrapper.in("StationID", ids);
                }
            }
        }
        else {
            wrapper.eq(stationId != null, "StationID", stationId);
        }

        Page<StationBaseSetModel> page = new Page<>(pageIndex, pageSize);
        Page<StationBaseSetModel> result = this.page(page, wrapper);
        Map<String, Object> map = new HashMap<>(4);
        map.put("total", result.getTotal());
        map.put("rows", result.getRecords());
        map.put("pages", result.getPages());
        map.put("current", result.getCurrent());
        return map;
    }

    /**
     * 根据用户获取测站
     *
     * @param userId 用户id
     */
    public List<StationBaseSetModel> getStationBaseSetByUser(String userId) {
        List<ResourceModel> resourceList = resourceApplication.getResourceList(userId, ResourceEnum.USER.getIndex(), ResourceEnum.Station.getIndex());
        List<StationBaseSetModel> stationBaseSetList = new ArrayList<StationBaseSetModel>(getStationBaseSetByResourceModel(resourceList));

        //获取所有角色
        List<String> roleList = resourceApplication.getRoleByUser(userId);

        //遍历角色id，获取资源
        for (String roleId : roleList) {
            stationBaseSetList.addAll(getStationBaseSetByRole(roleId));
        }

        return stationBaseSetList;
    }

    /**
     * 根据角色获取测站
     *
     * @param roleId 角色id
     */
    public List<StationBaseSetModel> getStationBaseSetByRole(String roleId) {
        List<ResourceModel> resourceList = resourceApplication.getResourceList(roleId, ResourceEnum.ROLE.getIndex(), ResourceEnum.Station.getIndex());
        return getStationBaseSetByResourceModel(resourceList);
    }

    /**
     * 根据资源映射获取菜单
     *
     * @param list 资源列表
     */
    private List<StationBaseSetModel> getStationBaseSetByResourceModel(List<ResourceModel> list) {
        List<StationBaseSetModel> menuList = new ArrayList<StationBaseSetModel>();
        for (ResourceModel resourceModel : list) {
            StationBaseSetModel model = this.getById(resourceModel.getSlave_id());
            menuList.add(model);
        }
        return menuList;
    }


    /**
     * 根据用户获取测站id
     *
     * @param userId 用户id
     */
    public List<String> getIdsByUser(String userId) {
        List<ResourceModel> resourceList = resourceApplication.getResourceList(userId, ResourceEnum.USER.getIndex(), ResourceEnum.Station.getIndex());
        List<String> ids = new ArrayList<>();

        for (ResourceModel resourceModel : resourceList) {
            ids.add(resourceModel.getSlave_id());
        }

        //获取所有角色
        List<String> roleList = resourceApplication.getRoleByUser(userId);

        //遍历角色id，获取资源
        for (String roleId : roleList) {
            ids.addAll(getIdsByRole(roleId));
        }

        return ids;
    }

    /**
     * 根据角色获取测站id
     *
     * @param roleId 角色id
     */
    public List<String> getIdsByRole(String roleId) {
        //根据角色获取所有资源
        List<ResourceModel> resourceList = resourceApplication.getResourceList(roleId, ResourceEnum.ROLE.getIndex(), ResourceEnum.Station.getIndex());

        List<String> ids = new ArrayList<>();
        for (ResourceModel resourceModel : resourceList) {
            ids.add(resourceModel.getSlave_id());
        }
        return ids;
    }


    /**
     * 绑定测站到用户
     *
     * @param userId           用户id
     * @param stationBaseSetId 测站id
     */
    public SaResult bindStationBaseSetByUser(String userId, String[] stationBaseSetId) {
        return resourceApplication.addResource(userId, ResourceEnum.USER.getIndex(), stationBaseSetId, ResourceEnum.Station.getIndex());
    }

    /**
     * 绑定测站到角色
     *
     * @param roleId           角色id
     * @param stationBaseSetId 测站id
     */
    public SaResult bindStationBaseSetByRole(String roleId, String[] stationBaseSetId) {
        return resourceApplication.addResource(roleId, ResourceEnum.ROLE.getIndex(), stationBaseSetId, ResourceEnum.Station.getIndex());
    }

    /**
     * 解绑测站到用户
     *
     * @param userId           用户id
     * @param stationBaseSetId id
     */
    public SaResult unBindStationBaseSetByUser(String userId, String[] stationBaseSetId) {
        return resourceApplication.deleteResource(null, userId, ResourceEnum.USER.getIndex(), stationBaseSetId, ResourceEnum.Station.getIndex());
    }

    /**
     * 解绑测站到角色
     *
     * @param roleId           角色id
     * @param stationBaseSetId id
     */
    public SaResult unBindStationBaseSetByRole(String roleId, String[] stationBaseSetId) {
        return resourceApplication.deleteResource(null, roleId, ResourceEnum.ROLE.getIndex(), stationBaseSetId, ResourceEnum.Station.getIndex());
    }
}
