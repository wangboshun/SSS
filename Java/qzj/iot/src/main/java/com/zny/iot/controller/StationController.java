package com.zny.iot.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.iot.application.StationApplication;
import com.zny.iot.model.StationBaseSetModel;
import com.zny.iot.model.input.StationInputDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author WBS
 * Date:2022/9/17
 */

@RestController
@RequestMapping("/iot/station")
@Tag(name = "station", description = "前置机模块")
public class StationController {

    private final StationApplication stationBaseSetApplication;

    public StationController(StationApplication stationBaseSetApplication) {
        this.stationBaseSetApplication = stationBaseSetApplication;
    }

    /**
     * 获取测站信息
     *
     * @param id 测站id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public SaResult get(@PathVariable String id) {
        return stationBaseSetApplication.getStationBaseSetById(id);
    }

    /**
     * 获取测站列表
     *
     * @param stationId 测站id
     * @param pageSize  分页大小
     * @param pageIndex 页码
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public SaResult list(
            @RequestParam(required = false) String stationId, @RequestParam(required = false) Integer pageIndex,
            @RequestParam(required = false) Integer pageSize) {
        return stationBaseSetApplication.getStationBaseSetPage(stationId, pageIndex, pageSize);
    }

    /**
     * 添加测站
     *
     * @param input dto
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public SaResult add(@RequestBody StationInputDto input) {
        return stationBaseSetApplication.addStationBaseSet(input);
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
        return stationBaseSetApplication.getRealData(stationId, pointId, start, end, pageIndex, pageSize);
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
        return stationBaseSetApplication.getPeriodData(stationId, pointId, start, end, pageIndex, pageSize);
    }

    /**
     * 根据用户获取测站
     *
     * @param userId 用户id
     */
    @RequestMapping(value = "/by_user", method = RequestMethod.GET)
    public SaResult getStationBaseSetByUser(String userId) {
        List<StationBaseSetModel> list = stationBaseSetApplication.getStationBaseSetByUser(userId);
        return SaResult.data(list);
    }

    /**
     * 根据角色获取测站
     *
     * @param roleId 角色id
     */
    @RequestMapping(value = "/by_role", method = RequestMethod.GET)
    public SaResult getStationBaseSetByRole(String roleId) {
        List<StationBaseSetModel> list = stationBaseSetApplication.getStationBaseSetByRole(roleId);
        return SaResult.data(list);
    }

    /**
     * 绑定测站到用户
     *
     * @param userIds           用户id
     * @param stationBaseSetIds 测站id
     */
    @RequestMapping(value = "/bind_by_user", method = RequestMethod.POST)
    public SaResult bindStationBaseSetByUser(String[] userIds, String[] stationBaseSetIds) {
        return stationBaseSetApplication.bindStationBaseSetByUser(userIds, stationBaseSetIds);
    }

    /**
     * 绑定测站到角色
     *
     * @param roleIds           角色id
     * @param stationBaseSetIds 测站id
     */
    @RequestMapping(value = "/bind_by_role", method = RequestMethod.POST)
    public SaResult bindStationBaseSetByRole(String[] roleIds, String[] stationBaseSetIds) {
        return stationBaseSetApplication.bindStationBaseSetByRole(roleIds, stationBaseSetIds);
    }

    /**
     * 解绑测站到用户
     *
     * @param userIds           用户id
     * @param stationBaseSetIds id
     */
    @RequestMapping(value = "/unbind_by_user", method = RequestMethod.POST)
    public SaResult unBindStationBaseSetByUser(String[] userIds, String[] stationBaseSetIds) {
        return stationBaseSetApplication.unBindStationBaseSetByUser(userIds, stationBaseSetIds);
    }

    /**
     * 解绑测站到角色
     *
     * @param roleIds           角色id
     * @param stationBaseSetIds id
     */
    @RequestMapping(value = "/unbind_by_role", method = RequestMethod.POST)
    public SaResult unBindStationBaseSetByRole(String[] roleIds, String[] stationBaseSetIds) {
        return stationBaseSetApplication.unBindStationBaseSetByRole(roleIds, stationBaseSetIds);
    }
}
