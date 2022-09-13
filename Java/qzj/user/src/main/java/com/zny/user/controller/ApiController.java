package com.zny.user.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.user.application.ApiApplication;
import com.zny.user.model.ApiModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author WBS
 * Date:2022/9/6
 */

@RestController
@RequestMapping("/user/api")
@Tag(name = "api", description = "接口模块")
public class ApiController {

    @Autowired
    private ApiApplication apiApplication;

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
        Map<String, Object> result = apiApplication.getApiList(apiId, apiName, apiCode, pageIndex, pageSize);
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
}
