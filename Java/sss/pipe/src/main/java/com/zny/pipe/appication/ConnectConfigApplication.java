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
import com.zny.pipe.mapper.ConnectConfigMapper;
import com.zny.pipe.model.ConnectConfigModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ConnectConfigApplication extends ServiceImpl<ConnectConfigMapper, ConnectConfigModel> {
    private final ResourceApplication resourceApplication;

    public ConnectConfigApplication(ResourceApplication resourceApplication) {
        this.resourceApplication = resourceApplication;
    }

    /**
     * 根据id获取链接信息
     *
     * @param id id
     */
    public SaResult getConnectById(String id) {
        if (resourceApplication.haveResource(id, ResourceEnum.Connect)) {
            ConnectConfigModel model = this.getById(id);
            if (model == null) {
                return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "链接不存在！");
            }
            return SaResult.data(model);
        } else {
            return SaResultEx.error(MessageCodeEnum.AUTH_INVALID);
        }
    }

    /**
     * 添加链接
     *
     * @param connectName 链接名
     * @param host        主机
     * @param port        端口号
     * @param userName    用户名
     * @param passWord    密码
     * @param dbName      数据库名
     * @param dbType      数据库类型
     */
    public SaResult addConnect(String connectName, String host, Integer port, String userName, String passWord, String dbName, Integer dbType) {
        QueryWrapper<ConnectConfigModel> wrapper = new QueryWrapper<ConnectConfigModel>();
        wrapper.eq("connect_name", connectName);
        ConnectConfigModel model = this.getOne(wrapper);
        if (model != null) {
            return SaResult.error("链接名已存在！");
        }
        model = new ConnectConfigModel();
        model.setId(UUID.randomUUID().toString());
        model.setConnect_name(connectName);
        model.setHost(host);
        model.setPort(port);
        model.setUsername(userName);
        model.setPassword(passWord);
        model.setDb_name(dbName);
        model.setDb_type(dbType);
        model.setCreate_time(DateUtils.dateToStr(LocalDateTime.now()));
        if (save(model)) {
            return SaResult.ok("添加链接成功！");
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "添加链接失败！");
        }
    }

    /**
     * 查询链接列表
     *
     * @param connectName 链接名
     * @param pageIndex   页码
     * @param pageSize    分页大小
     */
    public PageResult getConnectPage(String connectId, String connectName, Integer pageIndex, Integer pageSize) {
        pageSize = PageUtils.getPageSize(pageSize);
        pageIndex = PageUtils.getPageIndex(pageIndex);
        QueryWrapper<ConnectConfigModel> wrapper = new QueryWrapper<ConnectConfigModel>();
        if (!resourceApplication.haveResource(wrapper, connectId, "id", ResourceEnum.Connect)) {
            return null;
        }
        wrapper.eq(StringUtils.isNotBlank(connectName), "connect_name", connectName);
        wrapper.orderByDesc("create_time");
        Page<ConnectConfigModel> page = new Page<>(pageIndex, pageSize);
        Page<ConnectConfigModel> result = this.page(page, wrapper);
        PageResult pageResult = new PageResult();
        pageResult.setPages(result.getPages());
        pageResult.setRows(result.getRecords());
        pageResult.setTotal(result.getTotal());
        pageResult.setCurrent(result.getCurrent());
        return pageResult;
    }

    /**
     * 删除链接
     *
     * @param id 用户id
     */
    public SaResult deleteConnect(String id) {
        QueryWrapper<ConnectConfigModel> wrapper = new QueryWrapper<ConnectConfigModel>();
        wrapper.eq("id", id);
        ConnectConfigModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "链接不存在！");
        }
        if (removeById(id)) {
            return SaResult.ok("删除链接成功！");
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "删除链接失败！");
        }
    }

    /**
     * 更新链接信息
     *
     * @param id          链接id
     * @param connectName 链接名
     * @param host        主机
     * @param port        端口号
     * @param userName    用户名
     * @param passWord    密码
     * @param dbName      数据库名
     * @param dbType      数据库类型
     */
    public SaResult updateConnect(String id, String connectName, String host, Integer port, String userName, String passWord, String dbName, Integer dbType) {
        QueryWrapper<ConnectConfigModel> wrapper = new QueryWrapper<ConnectConfigModel>();
        wrapper.eq("id", id);
        ConnectConfigModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "链接不存在！");
        }
        if (StringUtils.isNotBlank(connectName)) {
            model.setConnect_name(connectName);
        }
        if (StringUtils.isNotBlank(host)) {
            model.setHost(host);
        }
        if (StringUtils.isNotBlank(userName)) {
            model.setUsername(userName);
        }
        if (StringUtils.isNotBlank(passWord)) {
            model.setPassword(passWord);
        }
        if (StringUtils.isNotBlank(dbName)) {
            model.setDb_name(dbName);
        }
        if (port != null) {
            model.setPort(port);
        }
        if (dbType != null) {
            model.setDb_type(dbType);
        }
        if (updateById(model)) {
            return SaResult.ok("更新链接信息成功！");
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "删除链接信息失败！");
        }
    }

    /**
     * 根据用户获取链接
     *
     * @param userId 用户id
     */
    public List<ConnectConfigModel> getConnectByUser(String userId) {
        Set<String> ids = resourceApplication.getIdsByUser(userId, ResourceEnum.Connect);
        List<ConnectConfigModel> connectList = new ArrayList<ConnectConfigModel>(getConnectByIds(ids));

        //获取所有角色
        Set<String> roleList = resourceApplication.getRoleByUser(userId);

        //遍历角色id，获取资源
        for (String roleId : roleList) {
            connectList.addAll(getConnectByRole(roleId));
        }

        return connectList;
    }

    /**
     * 根据角色获取链接
     *
     * @param roleId 角色id
     */
    public List<ConnectConfigModel> getConnectByRole(String roleId) {
        Set<String> ids = resourceApplication.getIdsByRole(roleId, ResourceEnum.Connect);
        return getConnectByIds(ids);
    }

    /**
     * 根据资源映射获取链接
     *
     * @param ids 资源id
     */
    private List<ConnectConfigModel> getConnectByIds(Set<String> ids) {
        List<ConnectConfigModel> list = new ArrayList<ConnectConfigModel>();
        if (ids == null || ids.isEmpty()) {
            return list;
        }
        for (String id : ids) {
            ConnectConfigModel model = this.getById(id);
            list.add(model);
        }
        return list;
    }

    /**
     * 绑定链接到用户
     *
     * @param userIds    用户id
     * @param connectIds 链接id
     */
    public SaResult bindConnectByUser(String[] userIds, String[] connectIds) {
        if (connectIds == null || connectIds.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.addResource(userIds, ResourceEnum.USER.getIndex(), connectIds, ResourceEnum.Connect.getIndex());
    }

    /**
     * 绑定链接到角色
     *
     * @param roleIds    角色id
     * @param connectIds 链接id
     */
    public SaResult bindConnectByRole(String[] roleIds, String[] connectIds) {
        if (connectIds == null || connectIds.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.addResource(roleIds, ResourceEnum.ROLE.getIndex(), connectIds, ResourceEnum.Connect.getIndex());
    }

    /**
     * 解绑链接到用户
     *
     * @param userId    用户id
     * @param connectId id
     */
    public SaResult unBindConnectByUser(String[] userId, String[] connectId) {
        if (connectId == null || connectId.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.deleteResource(null, userId, ResourceEnum.USER.getIndex(), connectId, ResourceEnum.Connect.getIndex());
    }

    /**
     * 解绑链接到角色
     *
     * @param roleId    角色id
     * @param connectId id
     */
    public SaResult unBindConnectByRole(String[] roleId, String[] connectId) {
        if (connectId == null || connectId.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.deleteResource(null, roleId, ResourceEnum.ROLE.getIndex(), connectId, ResourceEnum.Connect.getIndex());
    }
}
