package com.zny.iot.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.common.model.PageResult;
import com.zny.iot.application.StationBaseSetApplication;
import com.zny.iot.model.StationBaseSetModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author WBS
 * Date:2022/9/17
 */

@RestController
@RequestMapping("/iot/station")
@Tag(name = "station", description = "测站模块")
public class StationController {

    private final StationBaseSetApplication stationBaseSetApplication;

    public StationController(StationBaseSetApplication stationBaseSetApplication) {
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
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public SaResult list(
            @RequestParam(required = false) String stationId, @RequestParam(required = false) Integer pageIndex,
            @RequestParam(required = false) Integer pageSize) {
        PageResult result = stationBaseSetApplication.getStationBaseSetPage(stationId, pageIndex, pageSize);
        return SaResult.data(result);
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
     * @param userId           用户id
     * @param stationBaseSetId 测站id
     */
    @RequestMapping(value = "/bind_by_user", method = RequestMethod.POST)
    public SaResult bindStationBaseSetByUser(String userId, String[] stationBaseSetId) {
        return stationBaseSetApplication.bindStationBaseSetByUser(userId, stationBaseSetId);
    }

    /**
     * 绑定测站到角色
     *
     * @param roleId           角色id
     * @param stationBaseSetId 测站id
     */
    @RequestMapping(value = "/bind_by_role", method = RequestMethod.POST)
    public SaResult bindStationBaseSetByRole(String roleId, String[] stationBaseSetId) {
        return stationBaseSetApplication.bindStationBaseSetByRole(roleId, stationBaseSetId);
    }

    /**
     * 解绑测站到用户
     *
     * @param userId           用户id
     * @param stationBaseSetId id
     */
    @RequestMapping(value = "/unbind_by_user", method = RequestMethod.POST)
    public SaResult unBindStationBaseSetByUser(String userId, String[] stationBaseSetId) {
        return stationBaseSetApplication.unBindStationBaseSetByUser(userId, stationBaseSetId);
    }

    /**
     * 解绑测站到角色
     *
     * @param roleId           角色id
     * @param stationBaseSetId id
     */
    @RequestMapping(value = "/unbind_by_role", method = RequestMethod.POST)
    public SaResult unBindStationBaseSetByRole(String roleId, String[] stationBaseSetId) {
        return stationBaseSetApplication.unBindStationBaseSetByRole(roleId, stationBaseSetId);
    }
}
