package com.zny.system.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.system.application.apilog.ApiLogApplication;
import com.zny.system.model.apilog.ApiLogModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author WBS
 * Date:2022/9/2
 */

@Slf4j
@RestController
@RequestMapping("/system/apilog/")
@Tag(name = "system", description = "系统模块")
public class ApiLogController {

    @Autowired
    private ApiLogApplication apiLogApplication;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String Test() {
        return "apilog";
    }

    /**
     * 查询日志列表
     *
     * @param userId
     * @param method
     * @param ip
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public SaResult getApiLogList(@RequestParam(required = false) String userId, @RequestParam(required = false) String method, @RequestParam(required = false) String ip, @RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
        Map<String, Object> result = apiLogApplication.getApiLogList(userId, method, ip, pageIndex, pageSize);
        return SaResult.data(result);
    }

    /**
     * 查询日志
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public SaResult getApiLogById(@RequestParam(required = true) @PathVariable String id) {
        ApiLogModel model = apiLogApplication.getById(id);
        return SaResult.data(model);
    }

    /**
     * 删除日志
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public SaResult deleteApiLogById(@RequestParam(required = true) @PathVariable String id) {
        boolean b = apiLogApplication.removeById(id);
        if (b) {
            return SaResult.ok("日志删除成功！");
        } else {
            return SaResult.error("日志删除失败！");
        }
    }
}
