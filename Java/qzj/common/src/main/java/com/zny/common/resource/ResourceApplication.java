package com.zny.common.resource;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zny.common.enums.ResourceEnum;
import com.zny.common.enums.UserTypeEnum;
import com.zny.common.model.PageResult;
import com.zny.common.utils.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author WBS
 * Date:2022/9/2
 */

@Service
@DS("main")
public class ResourceApplication extends ServiceImpl<ResourceMapper, ResourceModel> {

    /**
     * 添加资源
     *
     * @param mainId    主id
     * @param mainType  主类型
     * @param slaveId   副id
     * @param slaveType 副类型
     */
    public SaResult addResource(String mainId, int mainType, String[] slaveId, int slaveType) {
        List<ResourceModel> list = new ArrayList<>();
        for (String item : slaveId) {
            List<ResourceModel> resourceList = getResourceList(null, mainId, mainType, item, slaveType);
            if (resourceList != null && resourceList.size() > 0) {
                continue;
            }
            ResourceModel resourceModel = new ResourceModel();
            resourceModel.setId(UUID.randomUUID().toString());
            resourceModel.setCreate_time(DateUtils.dateToStr(LocalDateTime.now()));
            resourceModel.setMain_id(mainId);
            resourceModel.setMain_type(mainType);
            resourceModel.setSlave_id(item);
            resourceModel.setSlave_type(slaveType);
            list.add(resourceModel);
        }
        if (slaveId.length > 0 && list.size() < 1) {
            return SaResult.error("所添加的资源已存在！");
        }
        if (saveOrUpdateBatch(list)) {
            return SaResult.ok("添加资源成功！");
        }
        else {
            return SaResult.error("添加资源失败！");
        }
    }

    /**
     * 删除资源信息
     *
     * @param id        id
     * @param mainId    主id
     * @param mainType  主类型
     * @param slaveId   副id
     * @param slaveType 副类型
     */
    public SaResult deleteResource(String id, String mainId, Integer mainType, String[] slaveId, Integer slaveType) {
        try {
            QueryWrapper<ResourceModel> wrapper = new QueryWrapper<ResourceModel>();
            wrapper.eq(StringUtils.isNotBlank(id), "id", id);
            wrapper.eq(StringUtils.isNotBlank(mainId), "main_id", mainId);
            wrapper.eq(mainType != null, "main_type", mainType);
            wrapper.in("slave_id", new ArrayList<>(Arrays.asList(slaveId)));
            wrapper.eq(slaveType != null, "slave_type", slaveType);
            if (remove(wrapper)) {
                return SaResult.ok("删除资源信息成功！");
            }
            else {
                return SaResult.error("删除资源信息失败！");
            }
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return SaResult.error("删除资源信息失败！");
        }
    }

    /**
     * 查询资源列表
     *
     * @param mainId    主id
     * @param mainType  主类型
     * @param slaveId   副id
     * @param slaveType 副类型
     * @param pageIndex 页码
     * @param pageSize  分页大小
     */
    public PageResult getResourcePage(
            String id, String mainId, Integer mainType, String slaveId, Integer slaveType, Integer pageIndex,
            Integer pageSize) {
        if (pageSize == null) {
            pageSize = 10;
        }
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        QueryWrapper<ResourceModel> wrapper = new QueryWrapper<ResourceModel>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(StringUtils.isNotBlank(mainId), "main_id", mainId);
        wrapper.eq(mainType != null, "main_type", mainType);
        wrapper.eq(StringUtils.isNotBlank(slaveId), "slave_id", slaveId);
        wrapper.eq(slaveType != null, "slave_type", slaveType);

        Page<ResourceModel> page = new Page<>(pageIndex, pageSize);
        Page<ResourceModel> result = this.page(page, wrapper);

        PageResult pageResult = new PageResult();
        pageResult.setPages(result.getPages());
        pageResult.setRows(result.getRecords());
        pageResult.setTotal(result.getTotal());
        pageResult.setCurrent(result.getCurrent());
        return pageResult;
    }

    /**
     * 查询资源列表
     *
     * @param id        id
     * @param mainId    主id
     * @param mainType  主类型
     * @param slaveId   副id
     * @param slaveType 副类型
     */
    public List<ResourceModel> getResourceList(
            String id, String mainId, Integer mainType, String slaveId, Integer slaveType) {
        QueryWrapper<ResourceModel> wrapper = new QueryWrapper<ResourceModel>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(StringUtils.isNotBlank(mainId), "main_id", mainId);
        wrapper.eq(mainType != null, "main_type", mainType);
        wrapper.eq(StringUtils.isNotBlank(slaveId), "slave_id", slaveId);
        wrapper.eq(slaveType != null, "slave_type", slaveType);
        return this.list(wrapper);
    }

    /**
     * 查询资源列表
     *
     * @param mainId    主id
     * @param mainType  主类型
     * @param slaveType 副类型
     */
    public List<ResourceModel> getResourceList(String mainId, Integer mainType, Integer slaveType) {
        return getResourceList(null, mainId, mainType, null, slaveType);
    }

    /**
     * 更新资源信息
     *
     * @param id        id
     * @param mainId    主id
     * @param mainType  主类型
     * @param slaveId   副id
     * @param slaveType 副类型
     */
    public SaResult updateResource(String id, String mainId, Integer mainType, String slaveId, Integer slaveType) {
        QueryWrapper<ResourceModel> wrapper = new QueryWrapper<ResourceModel>();
        wrapper.eq("id", id);
        ResourceModel model = this.getOne(wrapper);
        model.setMain_id(mainId);
        model.setMain_type(mainType);
        model.setSlave_id(slaveId);
        model.setSlave_type(slaveType);
        if (updateById(model)) {
            return SaResult.ok("更新资源信息成功！");
        }
        else {
            return SaResult.error("更新资源信息失败！");
        }
    }

    /**
     * 根据用户获取关联的角色
     *
     * @param userId 用户id
     */
    public List<String> getRoleByUser(String userId) {
        List<String> roleList = new ArrayList<String>();
        //获取角色获取关联的用户
        List<ResourceModel> list = getResourceList(null, null, ResourceEnum.ROLE.getIndex(), userId, ResourceEnum.USER.getIndex());
        for (ResourceModel model : list) {
            roleList.add(model.getMain_id());
        }
        return roleList;
    }

    /**
     * 根据用户获取ids
     *
     * @param userId 用户id
     */
    public List<String> getIdsByUser(String userId, ResourceEnum slaveType) {
        //获取用户关联的资源id
        List<ResourceModel> resourceList = getResourceList(userId, ResourceEnum.USER.getIndex(), slaveType.getIndex());
        if (resourceList == null || resourceList.isEmpty()) {
            return null;
        }

        List<String> ids = new ArrayList<>();
        for (ResourceModel resourceModel : resourceList) {
            ids.add(resourceModel.getSlave_id());
        }

        //获取所有角色
        List<String> roleList = getRoleByUser(userId);
        //遍历角色id，获取资源
        for (String roleId : roleList) {
            ids.addAll(getIdsByRole(roleId, slaveType));
        }

        return ids;
    }

    /**
     * 根据角色获取ids
     *
     * @param roleId 角色id
     */
    public List<String> getIdsByRole(String roleId, ResourceEnum slaveType) {
        //获取角色关联的资源id
        List<ResourceModel> resourceList = getResourceList(roleId, ResourceEnum.ROLE.getIndex(), slaveType.getIndex());
        if (resourceList == null || resourceList.isEmpty()) {
            return null;
        }
        List<String> ids = new ArrayList<>();
        for (ResourceModel resourceModel : resourceList) {
            ids.add(resourceModel.getSlave_id());
        }
        return ids;
    }


    /**
     * 根据用户查看是否有权限
     *
     * @param wrapper   构建条件
     * @param id        id
     * @param idField   id字段
     * @param slaveType 副资源类型
     */
    public boolean haveResource(QueryWrapper wrapper, String id, String idField, ResourceEnum slaveType) {
        Object userType = StpUtil.getSession().get("userType");
        //如果不是超级管理员
        if (!userType.equals(UserTypeEnum.SUPER.getIndex())) {
            String userId = (String) StpUtil.getSession().get("userId");
            List<String> ids = getIdsByUser(userId, slaveType);
            if (ids.size() < 1) {
                return false;
            }
            else {
                //如果有前端where条件
                if (id != null) {
                    //判断在资源范围内
                    if (ids.stream().anyMatch(x -> x.equals(id))) {
                        wrapper.eq(idField, id);
                    }
                    else {
                        return false;
                    }
                }
                //如果没有前端where条件
                else {
                    wrapper.in(idField, ids);
                }
            }
        }
        else {
            wrapper.eq(id != null, idField, id);
        }
        return true;
    }
}
