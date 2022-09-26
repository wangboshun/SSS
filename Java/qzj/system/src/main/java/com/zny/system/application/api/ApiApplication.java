package com.zny.system.application.api;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zny.common.enums.ResourceEnum;
import com.zny.common.resource.ResourceApplication;
import com.zny.common.resource.ResourceModel;
import com.zny.common.utils.DateUtils;
import com.zny.common.utils.ReflectUtils;
import com.zny.system.mapper.api.ApiMapper;
import com.zny.system.model.api.ApiModel;
import com.zny.system.model.api.ApiStatusEnum;
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

    private final ResourceApplication resourceApplication;

    public ApiApplication(WebApplicationContext applicationContext, ResourceApplication resourceApplication) {
        this.applicationContext = applicationContext;
        this.resourceApplication = resourceApplication;
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
            List<ApiModel> deleteList = new ArrayList<>();

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

            for (ApiModel old : oldList) {
                Optional<ApiModel> apiModel = newsList.stream().filter(x -> x.getApi_code().equals(old.getApi_code())).findFirst();
                if (!apiModel.isPresent()) {
                    //如果接口不存在
                    deleteList.add(old);
                }
            }

            if (addList.size() > 0) {
                if (!saveBatch(addList)) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    logger.error("添加接口失败");
                }
            }
            if (updateList.size() > 0) {
                if (!updateBatchById(updateList)) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    logger.error("更新接口失败");
                }
            }

            if (deleteList.size() > 0) {
                if (!removeBatchByIds(deleteList)) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    logger.error("删除接口失败");
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


    /**
     * 根据用户获取api
     *
     * @param userId 用户id
     */
    public List<ApiModel> getApiByUser(String userId) {
        List<ResourceModel> resourceList = resourceApplication.getResourceList(userId, ResourceEnum.USER.getIndex(), ResourceEnum.API.getIndex());
        List<ApiModel> apiList = new ArrayList<ApiModel>(getApiByResourceModel(resourceList));

        //获取所有角色
        List<String> roleList = resourceApplication.getRoleByUser(userId);

        //遍历角色id，获取资源
        for (String roleId : roleList) {
            apiList.addAll(getApiByRole(roleId));
        }

        return apiList;
    }

    /**
     * 根据角色获取api
     *
     * @param roleId 角色id
     */
    public List<ApiModel> getApiByRole(String roleId) {
        List<ResourceModel> resourceList = resourceApplication.getResourceList(roleId, ResourceEnum.ROLE.getIndex(), ResourceEnum.API.getIndex());
        return new ArrayList<ApiModel>(getApiByResourceModel(resourceList));
    }

    /**
     * 根据资源映射获取api
     *
     * @param list 资源列表
     */
    private List<ApiModel> getApiByResourceModel(List<ResourceModel> list) {
        List<ApiModel> apiList = new ArrayList<ApiModel>();
        for (ResourceModel resourceModel : list) {
            ApiModel apiModel = this.getById(resourceModel.getSlave_id());
            apiList.add(apiModel);
        }
        return apiList;
    }

    /**
     * 绑定api到用户
     *
     * @param userId 用户id
     * @param apiId  id
     */
    public SaResult bindApiByUser(String userId, String[] apiId) {
        return resourceApplication.addResource(userId, ResourceEnum.USER.getIndex(), apiId, ResourceEnum.API.getIndex());
    }

    /**
     * 绑定api到角色
     *
     * @param roleId 角色id
     * @param apiId  id
     */
    public SaResult bindApiByRole(String roleId, String[] apiId) {
        return resourceApplication.addResource(roleId, ResourceEnum.ROLE.getIndex(), apiId, ResourceEnum.API.getIndex());
    }

    /**
     * 解绑api到用户
     *
     * @param userId 用户id
     * @param apiId  id
     */
    public SaResult unBindApiByUser(String userId, String[] apiId) {
        return resourceApplication.deleteResource(null, userId, ResourceEnum.USER.getIndex(), apiId, ResourceEnum.API.getIndex());
    }

    /**
     * 解绑api到角色
     *
     * @param roleId 角色id
     * @param apiId  id
     */
    public SaResult unBindApiByRole(String roleId, String[] apiId) {
        return resourceApplication.deleteResource(null, roleId, ResourceEnum.ROLE.getIndex(), apiId, ResourceEnum.API.getIndex());
    }
}
