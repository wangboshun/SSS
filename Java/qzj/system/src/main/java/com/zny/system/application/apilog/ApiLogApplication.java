package com.zny.system.application.apilog;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zny.common.model.PageResult;
import com.zny.common.utils.PageUtils;
import com.zny.system.mapper.apilog.ApiLogMapper;
import com.zny.system.model.apilog.ApiLogModel;
import org.springframework.stereotype.Service;

/**
 * @author WBS
 * Date:2022/9/2
 */

@Service
@DS("main")
public class ApiLogApplication extends ServiceImpl<ApiLogMapper, ApiLogModel> {

    /**
     * @param userId    用户id
     * @param apiName   接口名称
     * @param method    请求类型：GET、POST
     * @param ip        ip地址
     * @param pageIndex 页码
     * @param pageSize  分页大小
     */
    public PageResult getApiLogPage(
            String userId, String apiName, String method, String ip, Integer pageIndex, Integer pageSize) {
        pageSize = PageUtils.getPageSize(pageSize);
        pageIndex = PageUtils.getPageIndex(pageIndex);
        QueryWrapper<ApiLogModel> wrapper = new QueryWrapper<ApiLogModel>();
        wrapper.eq(StringUtils.isNotBlank(apiName), "api_name", apiName);
        wrapper.eq(StringUtils.isNotBlank(userId), "user_id", userId);
        wrapper.eq(StringUtils.isNotBlank(ip), "ip", ip);
        wrapper.orderByDesc("start_time");
        wrapper.eq(StringUtils.isNotBlank(method), "method", method);
        Page<ApiLogModel> page = new Page<>(pageIndex, pageSize);
        Page<ApiLogModel> result = this.page(page, wrapper);
        PageResult pageResult = new PageResult();
        pageResult.setPages(result.getPages());
        pageResult.setRows(result.getRecords());
        pageResult.setTotal(result.getTotal());
        pageResult.setCurrent(result.getCurrent());
        return pageResult;
    }
}
