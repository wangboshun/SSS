package com.zny.common.resource;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
            wrapper.in(slaveId != null && slaveId.length > 0, "slave_id", new ArrayList<>(Arrays.asList(slaveId)));
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
    public Map<String, Object> getResourceList(
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

        Map<String, Object> map = new HashMap<>(4);
        map.put("total", result.getTotal());
        map.put("rows", result.getRecords());
        map.put("pages", result.getPages());
        map.put("current", result.getCurrent());
        return map;
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
        List<ResourceModel> list = getResourceList(null, null, ResourceEnum.ROLE.ordinal(), userId, ResourceEnum.USER.getIndex());
        for (ResourceModel model : list) {
            roleList.add(model.getMain_id());
        }
        return roleList;
    }
}
