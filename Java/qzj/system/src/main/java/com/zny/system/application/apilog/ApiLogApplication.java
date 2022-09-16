package com.zny.system.application.apilog;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zny.system.mapper.apilog.ApiLogMapper;
import com.zny.system.model.apilog.ApiLogModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/9/2
 */

@Service
public class ApiLogApplication extends ServiceImpl<ApiLogMapper, ApiLogModel> {

    @Autowired
    private ApiLogMapper apiLogMapper;

    /**
     * @param userId    用户id
     * @param apiName   接口名称
     * @param method    请求类型：GET、POST
     * @param ip        ip地址
     * @param pageIndex 页码
     * @param pageSize  分页大小
     */
    public Map<String, Object> getApiLogList(
            String userId, String apiName, String method, String ip, Integer pageIndex, Integer pageSize) {
        if (pageSize == null) {
            pageSize = 10;
        }
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        QueryWrapper<ApiLogModel> wrapper = new QueryWrapper<ApiLogModel>();
        wrapper.eq(StringUtils.isNotBlank(apiName), "api_name", apiName);
        wrapper.eq(StringUtils.isNotBlank(userId), "user_id", userId);
        wrapper.eq(StringUtils.isNotBlank(ip), "ip", ip);
        wrapper.eq(StringUtils.isNotBlank(method), "method", method);
        Page<ApiLogModel> page = new Page<>(pageIndex, pageSize);
        Page<ApiLogModel> result = this.page(page, wrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("total", result.getTotal());
        map.put("rows", result.getRecords());
        map.put("pages", result.getPages());
        map.put("current", result.getCurrent());
        return map;
    }
}
