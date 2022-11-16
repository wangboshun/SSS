package com.zny.pipe.appication;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zny.common.enums.ResourceEnum;
import com.zny.common.model.PageResult;
import com.zny.common.resource.ResourceApplication;
import com.zny.common.result.MessageCodeEnum;
import com.zny.common.result.SaResultEx;
import com.zny.common.utils.DateUtils;
import com.zny.common.utils.PageUtils;
import com.zny.pipe.mapper.SourceConfigMapper;
import com.zny.pipe.model.SourceConfigModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class SourceConfigApplication extends ServiceImpl<SourceConfigMapper, SourceConfigModel> {
    private final ResourceApplication resourceApplication;

    public SourceConfigApplication(ResourceApplication resourceApplication) {
        this.resourceApplication = resourceApplication;
    }

    /**
     * 根据id获取源节点信息
     *
     * @param id id
     */
    public SaResult getSourceById(String id) {
        if (resourceApplication.haveResource(id, ResourceEnum.Source)) {
            SourceConfigModel model = this.getById(id);
            if (model == null) {
                return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "源节点不存在！");
            }
            return SaResult.data(model);
        } else {
            return SaResultEx.error(MessageCodeEnum.AUTH_INVALID);
        }
    }

    /**
     * 添加源节点
     *
     * @param sourceName   源节点名
     * @param connectId    连接id
     * @param tableName    表名
     * @param primaryColumn 主键字段
     * @param timeColumn    数据时间字段
     * @param wrtmColumn    写入时间字段
     * @param orderColumn   排序字段
     * @param orderType    排序类型
     * @param getType      获取数据的方式
     */
    public SaResult addSource(String sourceName, String connectId, String tableName, String primaryColumn, String timeColumn, String wrtmColumn, String orderColumn, Integer orderType, Integer getType) {
        QueryWrapper<SourceConfigModel> wrapper = new QueryWrapper<SourceConfigModel>();
        wrapper.eq("source_name", sourceName);
        SourceConfigModel model = this.getOne(wrapper);
        if (model != null) {
            return SaResult.error("源节点名已存在！");
        }
        model = new SourceConfigModel();
        model.setId(UUID.randomUUID().toString());
        model.setSource_name(sourceName);
        model.setConnect_id(connectId);
        model.setTable_name(tableName);
        model.setPrimary_column(primaryColumn);
        model.setTime_column(timeColumn);
        model.setWrtm_column(wrtmColumn);
        model.setOrder_column(orderColumn);
        model.setOrder_type(orderType);
        model.setGet_type(getType);
        model.setCreate_time(DateUtils.dateToStr(LocalDateTime.now()));
        if (save(model)) {
            return SaResult.ok("添加源节点成功！");
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "添加源节点失败！");
        }
    }

    /**
     * 查询源节点列表
     *
     * @param sourceName 源节点名
     * @param pageIndex  页码
     * @param pageSize   分页大小
     */
    public PageResult getSourcePage(String sourceId, String sourceName, Integer pageIndex, Integer pageSize) {
        pageSize = PageUtils.getPageSize(pageSize);
        pageIndex = PageUtils.getPageIndex(pageIndex);
        QueryWrapper<SourceConfigModel> wrapper = new QueryWrapper<SourceConfigModel>();
        if (!resourceApplication.haveResource(wrapper, sourceId, "id", ResourceEnum.Source)) {
            return null;
        }
        wrapper.eq(StringUtils.isNotBlank(sourceName), "source_name", sourceName);
        wrapper.orderByDesc("create_time");
        Page<SourceConfigModel> page = new Page<>(pageIndex, pageSize);
        Page<SourceConfigModel> result = this.page(page, wrapper);
        PageResult pageResult = new PageResult();
        pageResult.setPages(result.getPages());
        pageResult.setRows(result.getRecords());
        pageResult.setTotal(result.getTotal());
        pageResult.setCurrent(result.getCurrent());
        return pageResult;
    }

    /**
     * 删除源节点
     *
     * @param id 用户id
     */
    public SaResult deleteSource(String id) {
        QueryWrapper<SourceConfigModel> wrapper = new QueryWrapper<SourceConfigModel>();
        wrapper.eq("id", id);
        SourceConfigModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "源节点不存在！");
        }
        if (removeById(id)) {
            return SaResult.ok("删除源节点成功！");
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "删除源节点失败！");
        }
    }

    /**
     * 更新源节点信息
     *
     * @param id           源节点id
     * @param sourceName   源节点名
     * @param connectId    连接id
     * @param tableName    表名
     * @param primaryColumn 主键字段
     * @param timeColumn    数据时间字段
     * @param wrtmColumn    写入时间字段
     * @param orderColumn   排序字段
     * @param orderType    排序类型
     * @param getType      获取数据的方式
     */
    public SaResult updateSource(String id, String sourceName, String connectId, String tableName, String primaryColumn, String timeColumn, String wrtmColumn, String orderColumn, Integer orderType, Integer getType) {
        QueryWrapper<SourceConfigModel> wrapper = new QueryWrapper<SourceConfigModel>();
        wrapper.eq("id", id);
        SourceConfigModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "源节点不存在！");
        }
        if (StringUtils.isNotBlank(sourceName)) {
            model.setSource_name(sourceName);
        }
        if (StringUtils.isNotBlank(connectId)) {
            model.setConnect_id(connectId);
        }
        if (StringUtils.isNotBlank(tableName)) {
            model.setTable_name(tableName);
        }
        if (StringUtils.isNotBlank(primaryColumn)) {
            model.setPrimary_column(primaryColumn);
        }
        if (StringUtils.isNotBlank(timeColumn)) {
            model.setTime_column(timeColumn);
        }
        if (StringUtils.isNotBlank(wrtmColumn)) {
            model.setWrtm_column(wrtmColumn);
        }
        if (StringUtils.isNotBlank(orderColumn)) {
            model.setOrder_column(orderColumn);
        }
        if (orderType != null) {
            model.setOrder_type(orderType);
        }
        if (getType != null) {
            model.setGet_type(getType);
        }

        if (updateById(model)) {
            return SaResult.ok("更新源节点信息成功！");
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "删除源节点信息失败！");
        }
    }

    /**
     * 根据用户获取源节点
     *
     * @param userId 用户id
     */
    public List<SourceConfigModel> getSourceByUser(String userId) {
        Set<String> ids = resourceApplication.getIdsByUser(userId, ResourceEnum.Source);
        List<SourceConfigModel> sourceList = new ArrayList<SourceConfigModel>(getSourceByIds(ids));

        //获取所有角色
        Set<String> roleList = resourceApplication.getRoleByUser(userId);

        //遍历角色id，获取资源
        for (String roleId : roleList) {
            sourceList.addAll(getSourceByRole(roleId));
        }

        return sourceList;
    }

    /**
     * 根据角色获取源节点
     *
     * @param roleId 角色id
     */
    public List<SourceConfigModel> getSourceByRole(String roleId) {
        Set<String> ids = resourceApplication.getIdsByRole(roleId, ResourceEnum.Source);
        return getSourceByIds(ids);
    }

    /**
     * 根据资源映射获取源节点
     *
     * @param ids 资源id
     */
    private List<SourceConfigModel> getSourceByIds(Set<String> ids) {
        List<SourceConfigModel> list = new ArrayList<SourceConfigModel>();
        if (ids == null || ids.isEmpty()) {
            return list;
        }
        for (String id : ids) {
            SourceConfigModel model = this.getById(id);
            list.add(model);
        }
        return list;
    }

    /**
     * 绑定源节点到用户
     *
     * @param userIds   用户id
     * @param sourceIds 源节点id
     */
    public SaResult bindSourceByUser(String[] userIds, String[] sourceIds) {
        if (sourceIds == null || sourceIds.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.addResource(userIds, ResourceEnum.USER.getIndex(), sourceIds, ResourceEnum.Source.getIndex());
    }

    /**
     * 绑定源节点到角色
     *
     * @param roleIds   角色id
     * @param sourceIds 源节点id
     */
    public SaResult bindSourceByRole(String[] roleIds, String[] sourceIds) {
        if (sourceIds == null || sourceIds.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.addResource(roleIds, ResourceEnum.ROLE.getIndex(), sourceIds, ResourceEnum.Source.getIndex());
    }

    /**
     * 解绑源节点到用户
     *
     * @param userId   用户id
     * @param sourceId id
     */
    public SaResult unBindSourceByUser(String[] userId, String[] sourceId) {
        if (sourceId == null || sourceId.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.deleteResource(null, userId, ResourceEnum.USER.getIndex(), sourceId, ResourceEnum.Source.getIndex());
    }

    /**
     * 解绑源节点到角色
     *
     * @param roleId   角色id
     * @param sourceId id
     */
    public SaResult unBindSourceByRole(String[] roleId, String[] sourceId) {
        if (sourceId == null || sourceId.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.deleteResource(null, roleId, ResourceEnum.ROLE.getIndex(), sourceId, ResourceEnum.Source.getIndex());
    }
}
