package com.zny.iot.application;

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
import com.zny.iot.mapper.*;
import com.zny.iot.model.PeriodAppDataModel;
import com.zny.iot.model.RealAppDataModel;
import com.zny.iot.model.SensorSetModel;
import com.zny.iot.model.StationBaseSetModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author WBS
 * Date:2022/9/26
 */

@Service
@DS("iot")
public class StationApplication extends ServiceImpl<StationBaseSetMapper, StationBaseSetModel> {

    private final ResourceApplication resourceApplication;
    private final StationBaseSetMapper stationBaseSetMapper;
    private final BXMapper bxMapper;
    private final EquipmentMapper equipmentMapper;
    private final RealAppDataMapper realAppDataMapper;
    private final PeriodAppDataMapper periodAppDataMapper;
    private final SensorSetMapper sensorSetMapper;

    public StationApplication(
            ResourceApplication resourceApplication, StationBaseSetMapper stationBaseSetMapper, BXMapper bxMapper,
            EquipmentMapper equipmentMapper, RealAppDataMapper realAppDataMapper,
            PeriodAppDataMapper periodAppDataMapper, SensorSetMapper sensorSetMapper) {
        this.resourceApplication = resourceApplication;
        this.stationBaseSetMapper = stationBaseSetMapper;
        this.bxMapper = bxMapper;
        this.equipmentMapper = equipmentMapper;
        this.realAppDataMapper = realAppDataMapper;
        this.periodAppDataMapper = periodAppDataMapper;
        this.sensorSetMapper = sensorSetMapper;
    }

    /**
     * 根据id获取测站信息
     *
     * @param id id
     */
    public SaResult getStationById(String id) {
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
     * 根据id获取传感器信息
     *
     * @param id id
     */
    public SaResult getSensorById(String id) {
        QueryWrapper<StationBaseSetModel> wrapper = new QueryWrapper<StationBaseSetModel>();
        if (!resourceApplication.haveResource(wrapper, null, "StationID", ResourceEnum.Station)) {
            return SaResultEx.error(MessageCodeEnum.AUTH_INVALID);
        }
        List<StationBaseSetModel> stationList = this.list(wrapper);
        List<Integer> stationIds = new ArrayList<>();
        for (StationBaseSetModel station : stationList) {
            stationIds.add(station.getStationId());
        }

        if (stationIds.size() < 1) {
            return SaResultEx.error(MessageCodeEnum.AUTH_INVALID);
        }

        QueryWrapper<SensorSetModel> sensorWrapper = new QueryWrapper<SensorSetModel>();
        sensorWrapper.in("StationID", stationIds);
        sensorWrapper.eq("sensorID", id);
        SensorSetModel model = sensorSetMapper.selectOne(sensorWrapper);
        if (model == null) {
            return SaResultEx.error(MessageCodeEnum.NOT_FOUND);
        }
        else {
            return SaResult.data(model);
        }
    }

    /**
     * 获取测站列表
     *
     * @param stationId 测站id
     * @param pageIndex 页码
     * @param pageSize  分页大小
     */
    public SaResult getStationPage(String stationId, Integer pageIndex, Integer pageSize) {
        pageSize = PageUtils.getPageSize(pageSize);
        pageIndex = PageUtils.getPageIndex(pageIndex);
        QueryWrapper<StationBaseSetModel> wrapper = new QueryWrapper<StationBaseSetModel>();
        if (!resourceApplication.haveResource(wrapper, stationId, "StationID", ResourceEnum.Station)) {
            return SaResultEx.error(MessageCodeEnum.AUTH_INVALID);
        }

        wrapper.orderByAsc("StationID");
        Page<StationBaseSetModel> page = new Page<>(pageIndex, pageSize);
        Page<StationBaseSetModel> result = this.page(page, wrapper);
        PageResult pageResult = new PageResult();
        pageResult.setPages(result.getPages());
        pageResult.setRows(result.getRecords());
        pageResult.setTotal(result.getTotal());
        pageResult.setCurrent(result.getCurrent());
        return SaResult.data(pageResult);
    }

    /**
     * 获取传感器列表
     *
     * @param sensorId  传感器id
     * @param pageIndex 页码
     * @param pageSize  分页大小
     */
    public SaResult getSensorPage(String sensorId, Integer pageIndex, Integer pageSize) {
        pageSize = PageUtils.getPageSize(pageSize);
        pageIndex = PageUtils.getPageIndex(pageIndex);
        QueryWrapper<StationBaseSetModel> wrapper = new QueryWrapper<StationBaseSetModel>();
        if (!resourceApplication.haveResource(wrapper, null, "StationID", ResourceEnum.Station)) {
            return SaResultEx.error(MessageCodeEnum.AUTH_INVALID);
        }
        List<StationBaseSetModel> stationList = this.list(wrapper);
        List<Integer> stationIds = new ArrayList<>();
        for (StationBaseSetModel station : stationList) {
            stationIds.add(station.getStationId());
        }

        if (stationIds.size() < 1) {
            return SaResultEx.error(MessageCodeEnum.AUTH_INVALID);
        }

        QueryWrapper<SensorSetModel> sensorWrapper = new QueryWrapper<SensorSetModel>();
        sensorWrapper.in("StationID", stationIds);
        sensorWrapper.eq(StringUtils.isNotBlank(sensorId), "sensorID", sensorId);
        Page<SensorSetModel> page = new Page<>(pageIndex, pageSize);
        Page<SensorSetModel> result = sensorSetMapper.selectPage(page, sensorWrapper);
        PageResult pageResult = new PageResult();
        pageResult.setPages(result.getPages());
        pageResult.setRows(result.getRecords());
        pageResult.setTotal(result.getTotal());
        pageResult.setCurrent(result.getCurrent());
        return SaResult.data(pageResult);
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
     * 根据资源映射获取测站
     *
     * @param ids 资源id
     */
    private List<StationBaseSetModel> getStationBaseSetByIds(Set<String> ids) {
        List<StationBaseSetModel> list = new ArrayList<StationBaseSetModel>();
        if (ids == null || ids.isEmpty()) {
            return list;
        }
        for (String id : ids) {
            StationBaseSetModel model = this.getById(id);
            list.add(model);
        }
        return list;
    }

    /**
     * 绑定测站到用户
     *
     * @param userIds           用户id
     * @param stationBaseSetIds 测站id
     */
    public SaResult bindStationBaseSetByUser(String[] userIds, String[] stationBaseSetIds) {
        if (stationBaseSetIds == null || stationBaseSetIds.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.addResource(userIds, ResourceEnum.USER.getIndex(), stationBaseSetIds, ResourceEnum.Station.getIndex());
    }

    /**
     * 绑定测站到角色
     *
     * @param roleIds           角色id
     * @param stationBaseSetIds 测站id
     */
    public SaResult bindStationBaseSetByRole(String[] roleIds, String[] stationBaseSetIds) {
        if (stationBaseSetIds == null || stationBaseSetIds.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.addResource(roleIds, ResourceEnum.ROLE.getIndex(), stationBaseSetIds, ResourceEnum.Station.getIndex());
    }

    /**
     * 解绑测站到用户
     *
     * @param userId           用户id
     * @param stationBaseSetId id
     */
    public SaResult unBindStationBaseSetByUser(String[] userId, String[] stationBaseSetId) {
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
    public SaResult unBindStationBaseSetByRole(String[] roleId, String[] stationBaseSetId) {
        if (stationBaseSetId == null || stationBaseSetId.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.deleteResource(null, roleId, ResourceEnum.ROLE.getIndex(), stationBaseSetId, ResourceEnum.Station.getIndex());
    }

    /**
     * 获取实时数据
     *
     * @param stationId 站点id
     * @param pointId   传感器id
     * @param start     开始时间
     * @param end       结束时间
     * @param pageIndex 页码
     * @param pageSize  分页大小
     */
    public SaResult getRealData(
            Integer stationId, Integer pointId, String start, String end, Integer pageIndex, Integer pageSize) {
        pageSize = PageUtils.getPageSize(pageSize);
        pageIndex = PageUtils.getPageIndex(pageIndex);

        QueryWrapper<StationBaseSetModel> stationWrapper = new QueryWrapper<StationBaseSetModel>();
        if (!resourceApplication.haveResource(stationWrapper, stationId, "StationID", ResourceEnum.Station)) {
            return SaResultEx.error(MessageCodeEnum.AUTH_INVALID);
        }
        List<StationBaseSetModel> stationList = this.list(stationWrapper);
        Set<Integer> stationIds = new HashSet<>();
        for (StationBaseSetModel item : stationList) {
            stationIds.add(item.getStationId());
        }
        //如果没有关联测站id
        if (stationIds.size() < 1) {
            return SaResultEx.error(MessageCodeEnum.AUTH_INVALID);
        }

        QueryWrapper<SensorSetModel> sensorWrapper = new QueryWrapper<>();
        sensorWrapper.in("StationID", stationIds);
        List<SensorSetModel> sensorList = sensorSetMapper.selectList(sensorWrapper);
        Set<Integer> pointIds = new HashSet<>();
        for (SensorSetModel sensor : sensorList) {
            pointIds.add(sensor.getSensorId());
        }
        //如果没有关联传感器id
        if (pointIds.size() < 1) {
            return SaResultEx.error(MessageCodeEnum.AUTH_INVALID);
        }

        //判断传入传感器id是否在资源内
        if (pointId != null && pointIds.stream().noneMatch(x -> x.equals(pointId))) {
            return SaResultEx.error(MessageCodeEnum.AUTH_INVALID);
        }

        //如果开始时间为空，从七天前开始
        if (StringUtils.isBlank(start)) {
            start = DateUtils.dateToStr(LocalDateTime.now().plusDays(-7));
        }

        //如果结束时间为空，设置为当前时间
        if (StringUtils.isBlank(end)) {
            end = DateUtils.dateToStr(LocalDateTime.now());
        }

        QueryWrapper<RealAppDataModel> realWrapper = new QueryWrapper<>();

        //如果传入了传感器id，条件筛选
        if (pointId != null) {
            realWrapper.eq("PointID", pointId);
        }
        //如果没有传入传感器id，包含筛选
        else {
            realWrapper.in("PointID", pointIds);
        }

        realWrapper.ge("DataTime", start);
        realWrapper.le("DataTime", end);
        realWrapper.orderByDesc("DataTime");
        Page<RealAppDataModel> page = new Page<>(pageIndex, pageSize);
        Page<RealAppDataModel> result = realAppDataMapper.selectPage(page, realWrapper);
        PageResult pageResult = new PageResult();
        pageResult.setPages(result.getPages());
        pageResult.setRows(result.getRecords());
        pageResult.setTotal(result.getTotal());
        pageResult.setCurrent(result.getCurrent());
        return SaResult.data(pageResult);
    }

    /**
     * 获取时段数据
     *
     * @param stationId 站点id
     * @param pointId   传感器id
     * @param start     开始时间
     * @param end       结束时间
     * @param pageIndex 页码
     * @param pageSize  分页大小
     */
    public SaResult getPeriodData(
            Integer stationId, Integer pointId, String start, String end, Integer pageIndex, Integer pageSize) {
        pageSize = PageUtils.getPageSize(pageSize);
        pageIndex = PageUtils.getPageIndex(pageIndex);
        QueryWrapper<StationBaseSetModel> stationWrapper = new QueryWrapper<StationBaseSetModel>();
        if (!resourceApplication.haveResource(stationWrapper, stationId, "StationID", ResourceEnum.Station)) {
            return SaResultEx.error(MessageCodeEnum.AUTH_INVALID);
        }
        List<StationBaseSetModel> stationList = this.list(stationWrapper);
        Set<Integer> stationIds = new HashSet<>();
        for (StationBaseSetModel item : stationList) {
            stationIds.add(item.getStationId());
        }
        //如果没有关联测站id
        if (stationIds.size() < 1) {
            return SaResultEx.error(MessageCodeEnum.AUTH_INVALID);
        }

        QueryWrapper<SensorSetModel> sensorWrapper = new QueryWrapper<>();
        sensorWrapper.in("StationID", stationIds);
        List<SensorSetModel> sensorList = sensorSetMapper.selectList(sensorWrapper);
        Set<Integer> pointIds = new HashSet<>();
        for (SensorSetModel sensor : sensorList) {
            pointIds.add(sensor.getSensorId());
        }
        //如果没有关联传感器id
        if (pointIds.size() < 1) {
            return SaResultEx.error(MessageCodeEnum.AUTH_INVALID);
        }

        //判断传入传感器id是否在资源内
        if (pointId != null && pointIds.stream().noneMatch(x -> x.equals(pointId))) {
            return SaResultEx.error(MessageCodeEnum.AUTH_INVALID);
        }

        //如果开始时间为空，从七天前开始
        if (StringUtils.isBlank(start)) {
            start = DateUtils.dateToStr(LocalDateTime.now().plusDays(-7));
        }

        //如果结束时间为空，设置为当前时间
        if (StringUtils.isBlank(end)) {
            end = DateUtils.dateToStr(LocalDateTime.now());
        }

        QueryWrapper<PeriodAppDataModel> realWrapper = new QueryWrapper<>();

        //如果传入了传感器id，条件筛选
        if (pointId != null) {
            realWrapper.eq("PointID", pointId);
        }
        //如果没有传入传感器id，包含筛选
        else {
            realWrapper.in("PointID", pointIds);
        }

        realWrapper.ge("DataTime", start);
        realWrapper.le("DataTime", end);
        realWrapper.orderByDesc("DataTime");
        Page<PeriodAppDataModel> page = new Page<>(pageIndex, pageSize);
        Page<PeriodAppDataModel> result = periodAppDataMapper.selectPage(page, realWrapper);
        PageResult pageResult = new PageResult();
        pageResult.setPages(result.getPages());
        pageResult.setRows(result.getRecords());
        pageResult.setTotal(result.getTotal());
        pageResult.setCurrent(result.getCurrent());
        return SaResult.data(pageResult);
    }


}
