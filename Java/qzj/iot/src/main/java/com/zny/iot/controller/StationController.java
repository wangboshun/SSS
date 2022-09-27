package com.zny.iot.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.iot.application.StationBaseSetApplication;
import com.zny.iot.model.StationBaseSetModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
        StationBaseSetModel model = stationBaseSetApplication.getById(id);
        return SaResult.data(model);
    }

    /**
     * 获取测站列表
     *
     * @param stationId 菜单id
     * @param pageSize  分页大小
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public SaResult list(
            @RequestParam(required = false) String stationId, @RequestParam(required = false) Integer pageIndex,
            @RequestParam(required = false) Integer pageSize) {
        Map<String, Object> result = stationBaseSetApplication.getStationList(stationId, pageIndex, pageSize);
        return SaResult.data(result);
    }
}
