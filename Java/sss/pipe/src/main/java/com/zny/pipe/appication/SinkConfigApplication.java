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
import com.zny.pipe.mapper.SinkConfigMapper;
import com.zny.pipe.model.SinkConfigModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class SinkConfigApplication extends ServiceImpl<SinkConfigMapper, SinkConfigModel> {
    private final ResourceApplication resourceApplication;

    public SinkConfigApplication(ResourceApplication resourceApplication) {
        this.resourceApplication = resourceApplication;
    }

    /**
     * 根据id获取目的节点信息
     *
     * @param id id
     */
    public SaResult getSinkById(String id) {
        if (resourceApplication.haveResource(id, ResourceEnum.Sink)) {
            SinkConfigModel model = this.getById(id);
            if (model == null) {
                return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "目的节点不存在！");
            }
            return SaResult.data(model);
        } else {
            return SaResultEx.error(MessageCodeEnum.AUTH_INVALID);
        }
    }

    /**
     * 添加目的节点
     *
     * @param sinkName  目的节点名
     * @param connectId 连接id
     * @param tableName 表名
     */
    public SaResult addSink(String sinkName, String connectId, String tableName) {
        QueryWrapper<SinkConfigModel> wrapper = new QueryWrapper<SinkConfigModel>();
        wrapper.eq("sink_name", sinkName);
        SinkConfigModel model = this.getOne(wrapper);
        if (model != null) {
            return SaResult.error("目的节点名已存在！");
        }
        model = new SinkConfigModel();
        model.setId(UUID.randomUUID().toString());
        model.setSink_name(sinkName);
        model.setConnect_id(connectId);
        model.setTable_name(tableName);
        model.setCreate_time(DateUtils.dateToStr(LocalDateTime.now()));
        if (save(model)) {
            return SaResult.ok("添加目的节点成功！");
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "添加目的节点失败！");
        }
    }

    /**
     * 查询目的节点列表
     *
     * @param sinkName  目的节点名
     * @param pageIndex 页码
     * @param pageSize  分页大小
     */
    public PageResult getSinkPage(String sinkId, String sinkName, Integer pageIndex, Integer pageSize) {
        pageSize = PageUtils.getPageSize(pageSize);
        pageIndex = PageUtils.getPageIndex(pageIndex);
        QueryWrapper<SinkConfigModel> wrapper = new QueryWrapper<SinkConfigModel>();
        if (!resourceApplication.haveResource(wrapper, sinkId, "id", ResourceEnum.Sink)) {
            return null;
        }
        wrapper.eq(StringUtils.isNotBlank(sinkName), "sink_name", sinkName);
        wrapper.orderByDesc("create_time");
        Page<SinkConfigModel> page = new Page<>(pageIndex, pageSize);
        Page<SinkConfigModel> result = this.page(page, wrapper);
        PageResult pageResult = new PageResult();
        pageResult.setPages(result.getPages());
        pageResult.setRows(result.getRecords());
        pageResult.setTotal(result.getTotal());
        pageResult.setCurrent(result.getCurrent());
        return pageResult;
    }

    /**
     * 删除目的节点
     *
     * @param id 用户id
     */
    public SaResult deleteSink(String id) {
        QueryWrapper<SinkConfigModel> wrapper = new QueryWrapper<SinkConfigModel>();
        wrapper.eq("id", id);
        SinkConfigModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "目的节点不存在！");
        }
        if (removeById(id)) {
            return SaResult.ok("删除目的节点成功！");
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "删除目的节点失败！");
        }
    }

    /**
     * 更新目的节点信息
     *
     * @param id        目的节点id
     * @param sinkName  目的节点名
     * @param connectId 连接id
     * @param tableName 表名
     */
    public SaResult updateSink(String id, String sinkName, String connectId, String tableName) {
        QueryWrapper<SinkConfigModel> wrapper = new QueryWrapper<SinkConfigModel>();
        wrapper.eq("id", id);
        SinkConfigModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "目的节点不存在！");
        }
        if (StringUtils.isNotBlank(sinkName)) {
            model.setSink_name(sinkName);
        }
        if (StringUtils.isNotBlank(connectId)) {
            model.setConnect_id(connectId);
        }
        if (StringUtils.isNotBlank(tableName)) {
            model.setTable_name(tableName);
        }
        if (updateById(model)) {
            return SaResult.ok("更新目的节点信息成功！");
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "删除目的节点信息失败！");
        }
    }

    /**
     * 根据用户获取目的节点
     *
     * @param userId 用户id
     */
    public List<SinkConfigModel> getSinkByUser(String userId) {
        Set<String> ids = resourceApplication.getIdsByUser(userId, ResourceEnum.Sink);
        List<SinkConfigModel> sinkList = new ArrayList<SinkConfigModel>(getSinkByIds(ids));

        //获取所有角色
        Set<String> roleList = resourceApplication.getRoleByUser(userId);

        //遍历角色id，获取资源
        for (String roleId : roleList) {
            sinkList.addAll(getSinkByRole(roleId));
        }

        return sinkList;
    }

    /**
     * 根据角色获取目的节点
     *
     * @param roleId 角色id
     */
    public List<SinkConfigModel> getSinkByRole(String roleId) {
        Set<String> ids = resourceApplication.getIdsByRole(roleId, ResourceEnum.Sink);
        return getSinkByIds(ids);
    }

    /**
     * 根据资源映射获取目的节点
     *
     * @param ids 资源id
     */
    private List<SinkConfigModel> getSinkByIds(Set<String> ids) {
        List<SinkConfigModel> list = new ArrayList<SinkConfigModel>();
        if (ids == null || ids.isEmpty()) {
            return list;
        }
        for (String id : ids) {
            SinkConfigModel model = this.getById(id);
            list.add(model);
        }
        return list;
    }

    /**
     * 绑定目的节点到用户
     *
     * @param userIds 用户id
     * @param sinkIds 目的节点id
     */
    public SaResult bindSinkByUser(String[] userIds, String[] sinkIds) {
        if (sinkIds == null || sinkIds.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.addResource(userIds, ResourceEnum.USER.getIndex(), sinkIds, ResourceEnum.Sink.getIndex());
    }

    /**
     * 绑定目的节点到角色
     *
     * @param roleIds 角色id
     * @param sinkIds 目的节点id
     */
    public SaResult bindSinkByRole(String[] roleIds, String[] sinkIds) {
        if (sinkIds == null || sinkIds.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.addResource(roleIds, ResourceEnum.ROLE.getIndex(), sinkIds, ResourceEnum.Sink.getIndex());
    }

    /**
     * 解绑目的节点到用户
     *
     * @param userId 用户id
     * @param sinkId id
     */
    public SaResult unBindSinkByUser(String[] userId, String[] sinkId) {
        if (sinkId == null || sinkId.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.deleteResource(null, userId, ResourceEnum.USER.getIndex(), sinkId, ResourceEnum.Sink.getIndex());
    }

    /**
     * 解绑目的节点到角色
     *
     * @param roleId 角色id
     * @param sinkId id
     */
    public SaResult unBindSinkByRole(String[] roleId, String[] sinkId) {
        if (sinkId == null || sinkId.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.deleteResource(null, roleId, ResourceEnum.ROLE.getIndex(), sinkId, ResourceEnum.Sink.getIndex());
    }
}
