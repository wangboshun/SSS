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
import com.zny.common.result.MessageCodeEnum;
import com.zny.common.result.SaResultEx;
import com.zny.common.utils.DateUtils;
import com.zny.common.utils.PageUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author WBS
 * Date:2022/9/2
 * 资源服务类
 */

@Service
@DS("main")
public class ResourceApplication extends ServiceImpl<ResourceMapper, ResourceModel> {

    /**
     * 添加资源
     *
     * @param mainIds   主id
     * @param mainType  主类型
     * @param slaveIds  副id
     * @param slaveType 副类型
     */
    public SaResult addResource(String[] mainIds, int mainType, String[] slaveIds, int slaveType) {
        List<ResourceModel> list = new ArrayList<>();
        for (String mainIdItem : mainIds) {
            for (String slaveIdItem : slaveIds) {
                List<ResourceModel> resourceList = getResourceList(null, mainIdItem, mainType, slaveIdItem, slaveType);
                if (resourceList != null && resourceList.size() > 0) {
                    continue;
                }
                ResourceModel resourceModel = new ResourceModel();
                resourceModel.setId(UUID.randomUUID().toString());
                resourceModel.setCreate_time(DateUtils.dateToStr(LocalDateTime.now()));
                resourceModel.setMain_id(mainIdItem);
                resourceModel.setMain_type(mainType);
                resourceModel.setSlave_id(slaveIdItem);
                resourceModel.setSlave_type(slaveType);
                list.add(resourceModel);
            }
        }

        if (slaveIds.length > 0 && list.size() < 1) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "资源已存在！");
        }
        if (saveOrUpdateBatch(list)) {
            return SaResult.ok("添加资源成功！");
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "添加资源失败！");
        }
    }

    /**
     * 删除资源信息
     *
     * @param id        id
     * @param mainIds   主id
     * @param mainType  主类型
     * @param slaveIds  副id
     * @param slaveType 副类型
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public SaResult deleteResource(
            String id, String[] mainIds, Integer mainType, String[] slaveIds, Integer slaveType) {
        try {
            for (String mainIdItem : mainIds) {
                QueryWrapper<ResourceModel> wrapper = new QueryWrapper<ResourceModel>();
                wrapper.eq(StringUtils.isNotBlank(id), "id", id);
                wrapper.eq(StringUtils.isNotBlank(mainIdItem), "main_id", mainIdItem);
                wrapper.eq(mainType != null, "main_type", mainType);
                wrapper.in("slave_id", new ArrayList<>(Arrays.asList(slaveIds)));
                wrapper.eq(slaveType != null, "slave_type", slaveType);
                if (!remove(wrapper)) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return SaResultEx.error(MessageCodeEnum.DB_ERROR, "删除资源信息失败！");
                }
            }
            return SaResult.ok("删除资源信息成功！");
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return SaResultEx.error(MessageCodeEnum.EXCEPTION, "删除资源信息失败！");
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
        pageSize = PageUtils.getPageSize(pageSize);
        pageIndex = PageUtils.getPageIndex(pageIndex);
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
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "更新资源信息失败！");
        }
    }

    /**
     * 根据用户获取关联的角色
     *
     * @param userId 用户id
     */
    public Set<String> getRoleByUser(String userId) {
        Set<String> roleList = new HashSet<>();
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
    public Set<String> getIdsByUser(String userId, ResourceEnum slaveType) {
        Set<String> ids = new HashSet<>();

        //获取用户关联的资源id
        List<ResourceModel> resourceList = getResourceList(userId, ResourceEnum.USER.getIndex(), slaveType.getIndex());
        if (resourceList != null && resourceList.size() > 0) {
            for (ResourceModel resourceModel : resourceList) {
                ids.add(resourceModel.getSlave_id());
            }
        }

        //获取所有角色
        Set<String> roleList = getRoleByUser(userId);
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
    public Set<String> getIdsByRole(String roleId, ResourceEnum slaveType) {
        //获取角色关联的资源id
        List<ResourceModel> resourceList = getResourceList(roleId, ResourceEnum.ROLE.getIndex(), slaveType.getIndex());
        if (resourceList == null || resourceList.isEmpty()) {
            return null;
        }
        Set<String> ids = new HashSet<>();
        for (ResourceModel resourceModel : resourceList) {
            ids.add(resourceModel.getSlave_id());
        }
        return ids;
    }


    /**
     * 根据用户查看是否有权限
     *
     * @param wrapper   构建条件
     * @param id        资源id
     * @param idField   资源id字段名
     * @param slaveType 副资源类型
     */
    public boolean haveResource(QueryWrapper wrapper, Object id, String idField, ResourceEnum slaveType) {
        Object userType = StpUtil.getSession().get("userType");
        //如果不是超级管理员
        if (!userType.equals(UserTypeEnum.SUPER.getIndex())) {
            String userId = (String) StpUtil.getSession().get("userId");
            Set<String> ids = getIdsByUser(userId, slaveType);
            if (ids == null || ids.size() < 1) {
                return false;
            } else {
                //如果有前端where条件
                if (id != null && StringUtils.isNotBlank(id.toString())) {
                    //判断在资源范围内
                    if (ids.stream().anyMatch(x -> x.equals(id))) {
                        wrapper.eq(idField, id);
                    } else {
                        return false;
                    }
                }
                //如果没有前端where条件
                else {
                    wrapper.in(idField, ids);
                }
            }
        } else {
            if (id != null && StringUtils.isNotBlank(id.toString())) {
                wrapper.eq(idField, id);
            }
        }
        return true;
    }

    /**
     * 根据用户查看是否有权限
     *
     * @param id        资源id
     * @param slaveType 副资源类型
     */
    public boolean haveResource(String id, ResourceEnum slaveType) {
        return haveResource(id, "id", slaveType);
    }

    /**
     * 根据用户查看是否有权限
     *
     * @param id        资源id
     * @param idField   资源id字段名
     * @param slaveType 副资源类型
     */
    public boolean haveResource(String id, String idField, ResourceEnum slaveType) {
        Object userType = StpUtil.getSession().get("userType");
        //如果不是超级管理员
        if (!userType.equals(UserTypeEnum.SUPER.getIndex())) {
            String userId = (String) StpUtil.getSession().get("userId");
            Set<String> ids = getIdsByUser(userId, slaveType);
            if (ids.size() < 1) {
                return false;
            }
            return ids.stream().anyMatch(x -> x.equals(id));
        }
        return true;
    }
}
