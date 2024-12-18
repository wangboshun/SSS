package com.zny.pipe.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.common.model.PageResult;
import com.zny.pipe.appication.SinkConfigApplication;
import com.zny.pipe.model.SinkConfigModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author WBS
 * Date:2022/10/12
 * sink目的控制器
 */

@RestController
@RequestMapping("/pipe/sink")
@Tag(name = "sink", description = "sink模块")
public class SinkController {

    private final SinkConfigApplication sinkConfigApplication;

    public SinkController(SinkConfigApplication sinkConfigApplication) {
        this.sinkConfigApplication = sinkConfigApplication;
    }

    /**
     * 获取目的节点列表
     *
     * @param sinkId   目的节点id
     * @param sinkName 目的节点名
     * @param pageSize 分页大小
     */
    @GetMapping(value = "/list")
    public SaResult list(@RequestParam(required = false) String sinkId, @RequestParam(required = false) String sinkName, @RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
        PageResult result = sinkConfigApplication.getSinkPage(sinkId, sinkName, pageIndex, pageSize);
        return SaResult.data(result);
    }

    /**
     * 获取目的节点信息
     *
     * @param id 目的节点id
     */
    @GetMapping(value = "/{id}")
    public SaResult get(@PathVariable String id) {
        return SaResult.data(sinkConfigApplication.getSinkById(id));
    }

    /**
     * 添加目的节点
     *
     * @param sinkName  目的节点名
     * @param connectId 连接id
     * @param tableName 表名
     */
    @PostMapping(value = "/add")
    public SaResult add(String sinkName, String connectId, String tableName) {
        return sinkConfigApplication.addSink(sinkName, connectId, tableName);
    }


    /**
     * 删除目的节点
     *
     * @param id 目的节点id
     */
    @DeleteMapping(value = "/{id}")
    public SaResult delete(@PathVariable String id) {
        return sinkConfigApplication.deleteSink(id);
    }

    /**
     * 更新目的节点信息
     *
     * @param id        目的节点id
     * @param sinkName  目的节点名
     * @param connectId 连接id
     * @param tableName 表名
     */
    @PatchMapping(value = "/{id}")
    public SaResult update(@PathVariable String id, @RequestParam(required = false) String sinkName, @RequestParam(required = false) String connectId, @RequestParam(required = false) String tableName) {
        return sinkConfigApplication.updateSink(id, sinkName, connectId, tableName);
    }

    /**
     * 根据用户获取目的节点
     *
     * @param userId 用户id
     */
    @GetMapping(value = "/{userId}/user")
    public SaResult getSinkByUser(@PathVariable String userId) {
        List<SinkConfigModel> list = sinkConfigApplication.getSinkByUser(userId);
        return SaResult.data(list);
    }

    /**
     * 根据角色获取目的节点
     *
     * @param roleId 角色id
     */
    @GetMapping(value = "/{roleId}/role")
    public SaResult getSinkByRole(@PathVariable String roleId) {
        List<SinkConfigModel> list = sinkConfigApplication.getSinkByRole(roleId);
        return SaResult.data(list);
    }


    /**
     * 绑定目的节点到用户
     *
     * @param userIds 用户id
     * @param sinkIds 目的节点id
     */
    @PatchMapping(value = "/bind_by_user")
    public SaResult bindSinkByUser(String[] userIds, String[] sinkIds) {
        return sinkConfigApplication.bindSinkByUser(userIds, sinkIds);
    }

    /**
     * 绑定目的节点到角色
     *
     * @param roleIds 角色id
     * @param sinkIds 目的节点id
     */
    @PatchMapping(value = "/bind_by_role")
    public SaResult bindSinkByRole(String[] roleIds, String[] sinkIds) {
        return sinkConfigApplication.bindSinkByRole(roleIds, sinkIds);
    }

    /**
     * 解绑目的节点到用户
     *
     * @param userIds 用户id
     * @param sinkIds id
     */
    @PatchMapping(value = "/unbind_by_user")
    public SaResult unBindSinkByUser(String[] userIds, String[] sinkIds) {
        return sinkConfigApplication.unBindSinkByUser(userIds, sinkIds);
    }

    /**
     * 解绑目的节点到角色
     *
     * @param roleIds 角色id
     * @param sinkIds id
     */
    @PatchMapping(value = "/unbind_by_role")
    public SaResult unBindSinkByRole(String[] roleIds, String[] sinkIds) {
        return sinkConfigApplication.unBindSinkByRole(roleIds, sinkIds);
    }
}
