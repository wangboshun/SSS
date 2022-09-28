package com.zny.system.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.common.model.PageResult;
import com.zny.system.application.api.ApiApplication;
import com.zny.system.model.api.ApiModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/9/6
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
    @RequestMapping(value = "/list", method = RequestMethod.GET)
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
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public SaResult get(@PathVariable String id) {
        ApiModel model = apiApplication.getById(id);
        return SaResult.data(model);
    }

    /**
     * 禁用接口
     *
     * @param id 接口id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public SaResult off(@PathVariable String id) {
        return apiApplication.offApi(id);
    }

    /**
     * 启用接口
     *
     * @param id 接口id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public SaResult on(@PathVariable String id) {
        return apiApplication.onApi(id);
    }

    /**
     * 根据用户获取Api
     *
     * @param userId 用户id
     */
    @RequestMapping(value = "/get_by_user", method = RequestMethod.GET)
    public SaResult getApiByUser(String userId) {
        List<ApiModel> list = apiApplication.getApiByUser(userId);
        return SaResult.data(list);
    }

    /**
     * 根据角色获取Api
     *
     * @param roleId 角色id
     */
    @RequestMapping(value = "/get_by_role", method = RequestMethod.GET)
    public SaResult getApiByRole(String roleId) {
        List<ApiModel> list = apiApplication.getApiByRole(roleId);
        return SaResult.data(list);
    }

    /**
     * 绑定api到用户
     *
     * @param userId 用户id
     * @param apiId  id
     */
    @RequestMapping(value = "/bind_by_user", method = RequestMethod.POST)
    public SaResult bindApiByUser(String userId, String[] apiId) {
        return apiApplication.bindApiByUser(userId, apiId);
    }

    /**
     * 绑定api到角色
     *
     * @param roleId 角色id
     * @param apiId  id
     */
    @RequestMapping(value = "/bind_by_role", method = RequestMethod.POST)
    public SaResult bindApiByRole(String roleId, String[] apiId) {
        return apiApplication.bindApiByRole(roleId, apiId);
    }

    /**
     * 解绑api到用户
     *
     * @param userId 用户id
     * @param apiId  id
     */
    @RequestMapping(value = "/unbind_by_user", method = RequestMethod.POST)
    public SaResult unBindApiByUser(String userId, String[] apiId) {
        return apiApplication.unBindApiByUser(userId, apiId);
    }

    /**
     * 解绑api到角色
     *
     * @param roleId 角色id
     * @param apiId  id
     */
    @RequestMapping(value = "/unbind_by_role", method = RequestMethod.POST)
    public SaResult unBindApiByRole(String roleId, String[] apiId) {
        return apiApplication.unBindApiByRole(roleId, apiId);
    }
}
