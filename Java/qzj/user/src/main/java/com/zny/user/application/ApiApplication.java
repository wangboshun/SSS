package com.zny.user.application;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zny.common.utils.DateUtils;
import com.zny.common.utils.ReflectUtils;
import com.zny.user.mapper.ApiMapper;
import com.zny.user.model.ApiModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author WBS
 * Date:2022/9/2
 */

@Service
public class ApiApplication extends ServiceImpl<ApiMapper, ApiModel> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private WebApplicationContext applicationContext;

    /**
     * 添加接口
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public SaResult addApi() {
        try {
            RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
            List<ApiModel> list = new ArrayList<>();
            for (Map.Entry<RequestMappingInfo, HandlerMethod> m : mapping.getHandlerMethods().entrySet()) {
                //控制器类
                Class<?> mainClass = m.getValue().getMethod().getDeclaringClass();

                //控制器名称
                String controllerClass = mainClass.getName();

                //请求方法
                String methodName = m.getValue().getMethod().getName();

                //找到类上的注解
                RestController restController = AnnotationUtils.findAnnotation(mainClass, RestController.class);
                if (restController == null) {
                    continue;
                }
                if (!controllerClass.contains("Controller")) {
                    continue;
                }
                ApiModel model = new ApiModel();
                model.setId(UUID.randomUUID().toString());
                model.setCreate_time(DateUtils.dateToStr(LocalDateTime.now()));
                model.setApi_path(controllerClass + "." + methodName);

                //获取模块名
                String group = AnnotationUtils.findAnnotation(mainClass, Tag.class).description();
                model.setApi_group(group);

                //获取方法注释
                String doc = ReflectUtils.getMethodDoc(controllerClass, methodName);
                model.setApi_name(doc);

                //URL
                RequestMappingInfo info = m.getKey();
                PatternsRequestCondition p = info.getPatternsCondition();
                model.setApi_code(p.toString().replace("[", "").replace("]", ""));

                //请求类型
                RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
                model.setApi_type(methodsCondition.toString().replace("[", "").replace("]", ""));
                list.add(model);
            }

            List<ApiModel> allData = this.list();
            removeBatchByIds(allData);
            if (saveBatch(list)) {
                return SaResult.ok("添加接口成功");
            }
            else {
                logger.error("添加接口失败");
                return SaResult.error("添加接口失败");
            }
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("添加接口异常", e);
            return SaResult.error("添加接口异常");
        }
    }

    /**
     * 查询接口列表
     *
     * @param apiName   接口名
     * @param pageIndex 页码
     * @param pageSize  分页大小
     */
    public Map<String, Object> getApiList(
            String apiId, String apiName, String apiCode, Integer pageIndex, Integer pageSize) {
        if (pageSize == null) {
            pageSize = 10;
        }
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        QueryWrapper<ApiModel> wrapper = new QueryWrapper<ApiModel>();
        wrapper.eq(StringUtils.isNotBlank(apiId), "id", apiId);
        wrapper.eq(StringUtils.isNotBlank(apiName), "api_name", apiName);
        wrapper.eq(StringUtils.isNotBlank(apiCode), "api_code", apiCode);
        Page<ApiModel> page = new Page<>(pageIndex, pageSize);
        Page<ApiModel> result = this.page(page, wrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("total", result.getTotal());
        map.put("rows", result.getRecords());
        map.put("pages", result.getPages());
        map.put("current", result.getCurrent());
        return map;
    }

    /**
     * 删除接口
     *
     * @param id 用户id
     */
    public SaResult deleteApi(String id) {
        QueryWrapper<ApiModel> wrapper = new QueryWrapper<ApiModel>();
        wrapper.eq("id", id);
        ApiModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResult.error("接口不存在！");
        }
        if (removeById(id)) {
            return SaResult.ok("删除接口成功！");
        }
        else {
            return SaResult.error("删除接口失败！");
        }
    }

    /**
     * 更新接口信息
     *
     * @param id      接口id
     * @param apiName 接口名
     */
    public SaResult updateApi(String id, String apiName, String apiCode) {
        QueryWrapper<ApiModel> wrapper = new QueryWrapper<ApiModel>();
        wrapper.eq("id", id);
        ApiModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResult.error("接口不存在！");
        }
        model.setApi_name(apiName);
        model.setApi_code(apiCode);
        if (updateById(model)) {
            return SaResult.ok("更新接口信息成功！");
        }
        else {
            return SaResult.error("删除接口信息失败！");
        }
    }
}
