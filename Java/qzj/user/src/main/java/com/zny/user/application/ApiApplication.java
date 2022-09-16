package com.zny.user.application;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zny.common.utils.DateUtils;
import com.zny.common.utils.ReflectUtils;
import com.zny.user.mapper.ApiMapper;
import com.zny.user.model.api.ApiModel;
import com.zny.user.model.api.ApiStatusEnum;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final WebApplicationContext applicationContext;

    public ApiApplication(WebApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 添加接口
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void addApi() {
        try {
            List<ApiModel> oldList = this.list();
            RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
            List<ApiModel> newsList = new ArrayList<>();
            for (Map.Entry<RequestMappingInfo, HandlerMethod> m : mapping.getHandlerMethods().entrySet()) {
                //控制器类
                Class<?> mainClass = m.getValue().getMethod().getDeclaringClass();

                //URL
                RequestMappingInfo info = m.getKey();
                PatternsRequestCondition p = info.getPatternsCondition();
                String url = p.toString().replace("[", "").replace("]", "");

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

                //获取模块名
                String group = AnnotationUtils.findAnnotation(mainClass, Tag.class).description();

                //获取方法注释
                String doc = ReflectUtils.getMethodDoc(controllerClass, methodName);

                //请求类型
                RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();

                ApiModel model = new ApiModel();

                model.setApi_path(controllerClass + "." + methodName);
                model.setApi_status(ApiStatusEnum.ON.getIndex());
                model.setApi_code(url);
                model.setApi_group(group);
                model.setApi_name(doc);
                model.setApi_type(methodsCondition.toString().replace("[", "").replace("]", ""));
                newsList.add(model);
            }

            List<ApiModel> addList = new ArrayList<>();
            List<ApiModel> updateList = new ArrayList<>();

            for (ApiModel news : newsList) {
                Optional<ApiModel> apiModel = oldList.stream().filter(x -> x.getApi_code().equals(news.getApi_code())).findFirst();
                if (apiModel.isPresent()) {
                    //如果存在，除了id和状态以外都需要更新
                    ApiModel model = apiModel.get();
                    news.setId(model.getId());
                    news.setApi_status(model.getApi_status());
                    updateList.add(news);
                }
                else {
                    news.setId(UUID.randomUUID().toString());
                    news.setCreate_time(DateUtils.dateToStr(LocalDateTime.now()));
                    addList.add(news);
                }
            }

            if (addList.size() > 0) {
                if (!saveBatch(addList)) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    logger.error("接口添加失败");
                }
            }
            if (updateList.size() > 0) {
                if (!updateBatchById(updateList)) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    logger.error("接口更新失败");
                }
            }
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("接口处理异常");
        }
        logger.info("接口处理完成");
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
        Map<String, Object> map = new HashMap<>(4);
        map.put("total", result.getTotal());
        map.put("rows", result.getRecords());
        map.put("pages", result.getPages());
        map.put("current", result.getCurrent());
        return map;
    }

    /**
     * 禁用接口
     *
     * @param id 用户id
     */
    public SaResult offApi(String id) {
        QueryWrapper<ApiModel> wrapper = new QueryWrapper<ApiModel>();
        wrapper.eq("id", id);
        ApiModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResult.error("接口不存在！");
        }
        model.setApi_status(ApiStatusEnum.OFF.getIndex());
        if (updateById(model)) {
            return SaResult.ok("禁用接口成功！");
        }
        else {
            return SaResult.error("禁用接口失败！");
        }
    }

    /**
     * 启用接口
     *
     * @param id 接口id
     */
    public SaResult onApi(String id) {
        QueryWrapper<ApiModel> wrapper = new QueryWrapper<ApiModel>();
        wrapper.eq("id", id);
        ApiModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResult.error("接口不存在！");
        }

        model.setApi_status(ApiStatusEnum.ON.getIndex());
        if (updateById(model)) {
            return SaResult.ok("启用接口成功！");
        }
        else {
            return SaResult.error("启用接口失败！");
        }
    }
}
