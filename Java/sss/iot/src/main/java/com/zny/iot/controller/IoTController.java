package com.zny.iot.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.iot.application.StationApplication;
import com.zny.iot.model.StationBaseSetModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author WBS
 * Date:2022/9/17
 * iot控制器
 */

@RestController
@RequestMapping("/iot")
@Tag(name = "iot", description = "IoT模块")
public class IoTController {

    private final StationApplication stationApplication;

    public IoTController(StationApplication stationApplication) {
        this.stationApplication = stationApplication;
    }

    /**
     * 获取测站信息
     *
     * @param id 测站id
     */
    @RequestMapping(value = "/station/{id}", method = RequestMethod.GET)
    public SaResult getStation(@PathVariable String id) {
        return stationApplication.getStationById(id);
    }

    /**
     * 获取传感器信息
     *
     * @param id 传感器id
     */
    @RequestMapping(value = "/sensor/{id}", method = RequestMethod.GET)
    public SaResult getSensor(@PathVariable String id) {
        return stationApplication.getSensorById(id);
    }

    /**
     * 获取测站列表
     *
     * @param stationId 测站id
     * @param pageSize  分页大小
     * @param pageIndex 页码
     */
    @RequestMapping(value = "/station_list", method = RequestMethod.GET)
    public SaResult stationList(
            @RequestParam(required = false) String stationId, @RequestParam(required = false) Integer pageIndex,
            @RequestParam(required = false) Integer pageSize) {
        return stationApplication.getStationPage(stationId, pageIndex, pageSize);
    }

    /**
     * 获取传感器列表
     *
     * @param sensorId  传感器id
     * @param pageSize  分页大小
     * @param pageIndex 页码
     */
    @RequestMapping(value = "/sensor_list", method = RequestMethod.GET)
    public SaResult sensorList(
            @RequestParam(required = false) String sensorId, @RequestParam(required = false) Integer pageIndex,
            @RequestParam(required = false) Integer pageSize) {
        return stationApplication.getSensorPage(sensorId, pageIndex, pageSize);
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
    @RequestMapping(value = "/real_data", method = RequestMethod.GET)
    public SaResult realData(
            @RequestParam(required = false) Integer stationId, @RequestParam(required = false) Integer pointId,
            @RequestParam(required = false) String start, @RequestParam(required = false) String end,
            @RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
        return stationApplication.getRealData(stationId, pointId, start, end, pageIndex, pageSize);
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
    @RequestMapping(value = "/period_data", method = RequestMethod.GET)
    public SaResult periodData(
            @RequestParam(required = false) Integer stationId, @RequestParam(required = false) Integer pointId,
            @RequestParam(required = false) String start, @RequestParam(required = false) String end,
            @RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
        return stationApplication.getPeriodData(stationId, pointId, start, end, pageIndex, pageSize);
    }

    /**
     * 根据用户获取测站
     *
     * @param userId 用户id
     */
    @RequestMapping(value = "/station/by_user", method = RequestMethod.GET)
    public SaResult getStationBaseSetByUser(String userId) {
        List<StationBaseSetModel> list = stationApplication.getStationBaseSetByUser(userId);
        return SaResult.data(list);
    }

    /**
     * 根据角色获取测站
     *
     * @param roleId 角色id
     */
    @RequestMapping(value = "/station/by_role", method = RequestMethod.GET)
    public SaResult getStationBaseSetByRole(String roleId) {
        List<StationBaseSetModel> list = stationApplication.getStationBaseSetByRole(roleId);
        return SaResult.data(list);
    }

    /**
     * 绑定测站到用户
     *
     * @param userIds           用户id
     * @param stationBaseSetIds 测站id
     */
    @RequestMapping(value = "/station/bind_by_user", method = RequestMethod.POST)
    public SaResult bindStationBaseSetByUser(String[] userIds, String[] stationBaseSetIds) {
        return stationApplication.bindStationBaseSetByUser(userIds, stationBaseSetIds);
    }

    /**
     * 绑定测站到角色
     *
     * @param roleIds           角色id
     * @param stationBaseSetIds 测站id
     */
    @RequestMapping(value = "/station/bind_by_role", method = RequestMethod.POST)
    public SaResult bindStationBaseSetByRole(String[] roleIds, String[] stationBaseSetIds) {
        return stationApplication.bindStationBaseSetByRole(roleIds, stationBaseSetIds);
    }

    /**
     * 解绑测站到用户
     *
     * @param userIds           用户id
     * @param stationBaseSetIds id
     */
    @RequestMapping(value = "/station/unbind_by_user", method = RequestMethod.POST)
    public SaResult unBindStationBaseSetByUser(String[] userIds, String[] stationBaseSetIds) {
        return stationApplication.unBindStationBaseSetByUser(userIds, stationBaseSetIds);
    }

    /**
     * 解绑测站到角色
     *
     * @param roleIds           角色id
     * @param stationBaseSetIds id
     */
    @RequestMapping(value = "/station/unbind_by_role", method = RequestMethod.POST)
    public SaResult unBindStationBaseSetByRole(String[] roleIds, String[] stationBaseSetIds) {
        return stationApplication.unBindStationBaseSetByRole(roleIds, stationBaseSetIds);
    }
}
