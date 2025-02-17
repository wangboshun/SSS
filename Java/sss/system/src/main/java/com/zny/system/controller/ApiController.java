package com.zny.system.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.common.model.PageResult;
import com.zny.system.application.api.ApiApplication;
import com.zny.system.model.api.ApiModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author WBS
 * Date:2022/9/6
 * api控制器
 */

@RestController
@RequestMapping("/user/api")
@Tag(name = "api", description = "接口模块")
public class ApiController {

    private final ApiApplication apiApplication;

    public ApiController(ApiApplication apiApplication) {
        this.apiApplication = apiApplication;
    }

    /**
     * 获取接口列表
     *
     * @param apiId    接口id
     * @param apiName  接口名
     * @param pageSize 分页大小
     */
    @GetMapping(value = "/list")
    public SaResult list(
            @RequestParam(required = false) String apiId, @RequestParam(required = false) String apiName,
            @RequestParam(required = false) String apiCode, @RequestParam(required = false) Integer pageIndex,
            @RequestParam(required = false) Integer pageSize) {
        PageResult result = apiApplication.getApiPage(apiId, apiName, apiCode, pageIndex, pageSize);
        return SaResult.data(result);
    }

    /**
     * 获取接口信息
     *
     * @param id 接口id
     */
    @GetMapping(value = "/{id}")
    public SaResult get(@PathVariable String id) {
        return SaResult.data(apiApplication.getApiById(id));
    }

    /**
     * 禁用接口
     *
     * @param id 接口id
     */
    @DeleteMapping(value = "/{id}")
    public SaResult off(@PathVariable String id) {
        return apiApplication.offApi(id);
    }

    /**
     * 启用接口
     *
     * @param id 接口id
     */
    @PatchMapping(value = "/{id}")
    public SaResult on(@PathVariable String id) {
        return apiApplication.onApi(id);
    }

    /**
     * 根据用户获取Api
     *
     * @param userId 用户id
     */
    @GetMapping(value = "/{userId}/user")
    public SaResult getApiByUser(@PathVariable String userId) {
        List<ApiModel> list = apiApplication.getApiByUser(userId);
        return SaResult.data(list);
    }

    /**
     * 根据角色获取Api
     *
     * @param roleId 角色id
     */
    @GetMapping(value = "/{roleId}/role")
    public SaResult getApiByRole(@PathVariable String roleId) {
        List<ApiModel> list = apiApplication.getApiByRole(roleId);
        return SaResult.data(list);
    }

    /**
     * 绑定api到用户
     *
     * @param userIds 用户id
     * @param apiIds  id
     */
    @PatchMapping(value = "/bind_by_user")
    public SaResult bindApiByUser(String[] userIds, String[] apiIds) {
        return apiApplication.bindApiByUser(userIds, apiIds);
    }

    /**
     * 绑定api到角色
     *
     * @param roleIds 角色id
     * @param apiIds  id
     */
    @PatchMapping(value = "/bind_by_role")
    public SaResult bindApiByRole(String[] roleIds, String[] apiIds) {
        return apiApplication.bindApiByRole(roleIds, apiIds);
    }

    /**
     * 解绑api到用户
     *
     * @param userIds 用户id
     * @param apiIds  id
     */
    @PatchMapping(value = "/unbind_by_user")
    public SaResult unBindApiByUser(String[] userIds, String[] apiIds) {
        return apiApplication.unBindApiByUser(userIds, apiIds);
    }

    /**
     * 解绑api到角色
     *
     * @param roleIds 角色id
     * @param apiIds  id
     */
    @PatchMapping(value = "/unbind_by_role")
    public SaResult unBindApiByRole(String[] roleIds, String[] apiIds) {
        return apiApplication.unBindApiByRole(roleIds, apiIds);
    }
}
