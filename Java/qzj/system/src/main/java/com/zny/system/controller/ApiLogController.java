package com.zny.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.util.SaResult;
import com.zny.system.application.apilog.ApiLogApplication;
import com.zny.system.model.apilog.ApiLogModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/9/2
 */

@RestController
@RequestMapping("/system/apilog/")
@Tag(name = "system", description = "系统模块")
public class ApiLogController {

    @Autowired
    private ApiLogApplication apiLogApplication;

    /**
     * 查询日志列表
     *
     * @param userId    用户名
     * @param method    请求方法
     * @param ip        ip地址
     * @param pageIndex 页码
     * @param pageSize  分页大小
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public SaResult list(@RequestParam(required = false) String userId, @RequestParam(required = false) String method, @RequestParam(required = false) String ip, @RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
        Map<String, Object> result = apiLogApplication.getApiLogList(userId, method, ip, pageIndex, pageSize);
        return SaResult.data(result);
    }

    /**
     * 查询日志
     *
     * @param id 日志id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public SaResult get(@PathVariable String id) {
        ApiLogModel model = apiLogApplication.getById(id);
        return SaResult.data(model);
    }

    /**
     * 批量删除日志
     *
     * @param ids id数组
     */
    @RequestMapping(value = "/{ids}", method = RequestMethod.DELETE)
    public SaResult delete(@PathVariable String[] ids) {
        boolean b = apiLogApplication.removeBatchByIds(Arrays.asList(ids));
        if (b) {
            return SaResult.ok("日志删除成功！");
        } else {
            return SaResult.error("日志删除失败！");
        }
    }
}