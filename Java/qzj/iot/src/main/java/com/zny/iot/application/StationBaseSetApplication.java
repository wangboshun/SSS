package com.zny.iot.application;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zny.common.enums.ResourceEnum;
import com.zny.common.model.PageResult;
import com.zny.common.resource.ResourceApplication;
import com.zny.common.result.MessageCodeEnum;
import com.zny.common.result.SaResultEx;
import com.zny.iot.mapper.StationBaseSetMapper;
import com.zny.iot.model.StationBaseSetModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
     * 根据id获取测站信息
     *
     * @param id id
     */
    public SaResult getStationBaseSetById(String id) {
        if (resourceApplication.haveResource(id, "StationID", ResourceEnum.Station)) {
            StationBaseSetModel model = this.getById(id);
            if (model == null) {
                return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "测站不存在！");
            }
            return SaResult.data(model);
        }
        else {
            return SaResultEx.error(MessageCodeEnum.AUTH_INVALID);
        }
    }

    /**
     * 获取测站列表
     *
     * @param stationId 测站id
     * @param pageIndex 页码
     * @param pageSize  分页大小
     */
    public PageResult getStationBaseSetPage(String stationId, Integer pageIndex, Integer pageSize) {
        if (pageSize == null) {
            pageSize = 10;
        }
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }

        QueryWrapper<StationBaseSetModel> wrapper = new QueryWrapper<StationBaseSetModel>();
        if (!resourceApplication.haveResource(wrapper, stationId, "StationID", ResourceEnum.Station)) {
            return null;
        }

        Page<StationBaseSetModel> page = new Page<>(pageIndex, pageSize);
        Page<StationBaseSetModel> result = this.page(page, wrapper);
        PageResult pageResult = new PageResult();
        pageResult.setPages(result.getPages());
        pageResult.setRows(result.getRecords());
        pageResult.setTotal(result.getTotal());
        pageResult.setCurrent(result.getCurrent());
        return pageResult;
    }

    /**
     * 根据用户获取测站
     *
     * @param userId 用户id
     */
    public List<StationBaseSetModel> getStationBaseSetByUser(String userId) {
        Set<String> ids = resourceApplication.getIdsByUser(userId, ResourceEnum.Station);
        List<StationBaseSetModel> stationBaseSetList = new ArrayList<StationBaseSetModel>(getStationBaseSetByIds(ids));

        //获取所有角色
        Set<String> roleList = resourceApplication.getRoleByUser(userId);

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
        Set<String> ids = resourceApplication.getIdsByRole(roleId, ResourceEnum.Station);
        return getStationBaseSetByIds(ids);
    }

    /**
     * 根据资源映射获取菜单
     *
     * @param ids 资源id
     */
    private List<StationBaseSetModel> getStationBaseSetByIds(Set<String> ids) {
        List<StationBaseSetModel> menuList = new ArrayList<StationBaseSetModel>();
        for (String id : ids) {
            StationBaseSetModel model = this.getById(id);
            menuList.add(model);
        }
        return menuList;
    }

    /**
     * 绑定测站到用户
     *
     * @param userId           用户id
     * @param stationBaseSetId 测站id
     */
    public SaResult bindStationBaseSetByUser(String userId, String[] stationBaseSetId) {
        if (stationBaseSetId == null || stationBaseSetId.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.addResource(userId, ResourceEnum.USER.getIndex(), stationBaseSetId, ResourceEnum.Station.getIndex());
    }

    /**
     * 绑定测站到角色
     *
     * @param roleId           角色id
     * @param stationBaseSetId 测站id
     */
    public SaResult bindStationBaseSetByRole(String roleId, String[] stationBaseSetId) {
        if (stationBaseSetId == null || stationBaseSetId.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.addResource(roleId, ResourceEnum.ROLE.getIndex(), stationBaseSetId, ResourceEnum.Station.getIndex());
    }

    /**
     * 解绑测站到用户
     *
     * @param userId           用户id
     * @param stationBaseSetId id
     */
    public SaResult unBindStationBaseSetByUser(String userId, String[] stationBaseSetId) {
        if (stationBaseSetId == null || stationBaseSetId.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.deleteResource(null, userId, ResourceEnum.USER.getIndex(), stationBaseSetId, ResourceEnum.Station.getIndex());
    }

    /**
     * 解绑测站到角色
     *
     * @param roleId           角色id
     * @param stationBaseSetId id
     */
    public SaResult unBindStationBaseSetByRole(String roleId, String[] stationBaseSetId) {
        if (stationBaseSetId == null || stationBaseSetId.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.deleteResource(null, roleId, ResourceEnum.ROLE.getIndex(), stationBaseSetId, ResourceEnum.Station.getIndex());
    }
}
