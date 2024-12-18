package com.zny.pipe.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.common.model.PageResult;
import com.zny.pipe.appication.ConnectConfigApplication;
import com.zny.pipe.model.ConnectConfigModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author WBS
 * Date:2022/10/12
 * 链接控制器
 */

@RestController
@RequestMapping("/pipe/connect")
@Tag(name = "connect", description = "connect模块")
public class ConnectController {

    private final ConnectConfigApplication connectConfigApplication;

    public ConnectController(ConnectConfigApplication connectConfigApplication) {
        this.connectConfigApplication = connectConfigApplication;
    }

    /**
     * 获取链接列表
     *
     * @param connectId   链接id
     * @param connectName 链接名
     * @param pageSize    分页大小
     */
    @GetMapping(value = "/list")
    public SaResult list(@RequestParam(required = false) String connectId, @RequestParam(required = false) String connectName, @RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
        PageResult result = connectConfigApplication.getConnectPage(connectId, connectName, pageIndex, pageSize);
        return SaResult.data(result);
    }

    /**
     * 获取链接信息
     *
     * @param id 链接id
     */
    @GetMapping(value = "/{id}")
    public SaResult get(@PathVariable String id) {
        return SaResult.data(connectConfigApplication.getConnectById(id));
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
    @PostMapping(value = "/add")
    public SaResult add(String connectName, String host, Integer port, String userName, String passWord, String dbName, Integer dbType) {
        return connectConfigApplication.addConnect(connectName, host, port, userName, passWord, dbName, dbType);
    }

    /**
     * 删除链接
     *
     * @param id 链接id
     */
    @DeleteMapping(value = "/{id}")
    public SaResult delete(@PathVariable String id) {
        return connectConfigApplication.deleteConnect(id);
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
    @PatchMapping(value = "/{id}")
    public SaResult update(@PathVariable String id, String connectName, String host, Integer port, String userName, String passWord, String dbName, Integer dbType) {
        return connectConfigApplication.updateConnect(id, connectName, host, port, userName, passWord, dbName, dbType);
    }

    /**
     * 根据用户获取链接
     *
     * @param userId 用户id
     */
    @GetMapping(value = "/{userId}/user")
    public SaResult getConnectByUser(@PathVariable String userId) {
        List<ConnectConfigModel> list = connectConfigApplication.getConnectByUser(userId);
        return SaResult.data(list);
    }

    /**
     * 根据角色获取链接
     *
     * @param roleId 角色id
     */
    @GetMapping(value = "/{roleId}/role")
    public SaResult getConnectByRole(@PathVariable String roleId) {
        List<ConnectConfigModel> list = connectConfigApplication.getConnectByRole(roleId);
        return SaResult.data(list);
    }


    /**
     * 绑定链接到用户
     *
     * @param userIds    用户id
     * @param connectIds 链接id
     */
    @PatchMapping(value = "/bind_by_user")
    public SaResult bindConnectByUser(String[] userIds, String[] connectIds) {
        return connectConfigApplication.bindConnectByUser(userIds, connectIds);
    }

    /**
     * 绑定链接到角色
     *
     * @param roleIds    角色id
     * @param connectIds 链接id
     */
    @PatchMapping(value = "/bind_by_role")
    public SaResult bindConnectByRole(String[] roleIds, String[] connectIds) {
        return connectConfigApplication.bindConnectByRole(roleIds, connectIds);
    }

    /**
     * 解绑链接到用户
     *
     * @param userIds    用户id
     * @param connectIds id
     */
    @PatchMapping(value = "/unbind_by_user")
    public SaResult unBindConnectByUser(String[] userIds, String[] connectIds) {
        return connectConfigApplication.unBindConnectByUser(userIds, connectIds);
    }

    /**
     * 解绑链接到角色
     *
     * @param roleIds    角色id
     * @param connectIds id
     */
    @PatchMapping(value = "/unbind_by_role")
    public SaResult unBindConnectByRole(String[] roleIds, String[] connectIds) {
        return connectConfigApplication.unBindConnectByRole(roleIds, connectIds);
    }
}
