package com.zny.pipe.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.common.model.PageResult;
import com.zny.pipe.appication.SourceConfigApplication;
import com.zny.pipe.model.SourceConfigModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author WBS
 * Date:2022/10/12
 * source源控制器
 */

@RestController
@RequestMapping("/pipe/source")
@Tag(name = "source", description = "source模块")
public class SourceController {

    private final SourceConfigApplication sourceConfigApplication;

    public SourceController(SourceConfigApplication sourceConfigApplication) {
        this.sourceConfigApplication = sourceConfigApplication;
    }

    /**
     * 获取源节点列表
     *
     * @param sourceId   源节点id
     * @param sourceName 源节点名
     * @param pageSize   分页大小
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public SaResult list(@RequestParam(required = false) String sourceId, @RequestParam(required = false) String sourceName, @RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
        PageResult result = sourceConfigApplication.getSourcePage(sourceId, sourceName, pageIndex, pageSize);
        return SaResult.data(result);
    }

    /**
     * 获取源节点信息
     *
     * @param id 源节点id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public SaResult get(@PathVariable String id) {
        return SaResult.data(sourceConfigApplication.getSourceById(id));
    }

    /**
     * 添加源节点
     *
     * @param sourceName    源节点名
     * @param connectId     连接id
     * @param tableName     表名
     * @param primaryColumn 主键字段
     * @param timeColumn    数据时间字段
     * @param wrtmColumn    写入时间字段
     * @param orderColumn    排序字段
     * @param orderType     排序类型
     * @param getType       获取数据的方式
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public SaResult add(String sourceName, String connectId, String tableName, String primaryColumn, String timeColumn, String wrtmColumn, String orderColumn, Integer orderType, Integer getType) {
        return sourceConfigApplication.addSource(sourceName, connectId, tableName, primaryColumn, timeColumn, wrtmColumn, orderColumn, orderType, getType);
    }


    /**
     * 删除源节点
     *
     * @param id 源节点id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public SaResult delete(@PathVariable String id) {
        return sourceConfigApplication.deleteSource(id);
    }

    /**
     * 更新源节点信息
     *
     * @param id            源节点id
     * @param sourceName    源节点名
     * @param connectId     连接id
     * @param tableName     表名
     * @param primaryColumn 主键字段
     * @param timeColumn    数据时间字段
     * @param wrtmColumn    写入时间字段
     * @param orderColumn   排序字段
     * @param orderType     排序类型
     * @param getType       获取数据的方式
     */
    public SaResult update(@PathVariable String id, @RequestParam(required = false) String sourceName, @RequestParam(required = false) String connectId, @RequestParam(required = false) String tableName, @RequestParam(required = false) String primaryColumn, @RequestParam(required = false) String timeColumn, @RequestParam(required = false) String wrtmColumn, @RequestParam(required = false) String orderColumn, @RequestParam(required = false) Integer orderType, @RequestParam(required = false) Integer getType) {
        return sourceConfigApplication.updateSource(id, sourceName, connectId, tableName, primaryColumn, timeColumn, wrtmColumn, orderColumn, orderType, getType);
    }

    /**
     * 根据用户获取源节点
     *
     * @param userId 用户id
     */
    @RequestMapping(value = "/by_user", method = RequestMethod.GET)
    public SaResult getSourceByUser(String userId) {
        List<SourceConfigModel> list = sourceConfigApplication.getSourceByUser(userId);
        return SaResult.data(list);
    }

    /**
     * 根据角色获取源节点
     *
     * @param roleId 角色id
     */
    @RequestMapping(value = "/by_role", method = RequestMethod.GET)
    public SaResult getSourceByRole(String roleId) {
        List<SourceConfigModel> list = sourceConfigApplication.getSourceByRole(roleId);
        return SaResult.data(list);
    }


    /**
     * 绑定源节点到用户
     *
     * @param userIds   用户id
     * @param sourceIds 源节点id
     */
    @RequestMapping(value = "/bind_by_user", method = RequestMethod.POST)
    public SaResult bindSourceByUser(String[] userIds, String[] sourceIds) {
        return sourceConfigApplication.bindSourceByUser(userIds, sourceIds);
    }

    /**
     * 绑定源节点到角色
     *
     * @param roleIds   角色id
     * @param sourceIds 源节点id
     */
    @RequestMapping(value = "/bind_by_role", method = RequestMethod.POST)
    public SaResult bindSourceByRole(String[] roleIds, String[] sourceIds) {
        return sourceConfigApplication.bindSourceByRole(roleIds, sourceIds);
    }

    /**
     * 解绑源节点到用户
     *
     * @param userIds   用户id
     * @param sourceIds id
     */
    @RequestMapping(value = "/unbind_by_user", method = RequestMethod.POST)
    public SaResult unBindSourceByUser(String[] userIds, String[] sourceIds) {
        return sourceConfigApplication.unBindSourceByUser(userIds, sourceIds);
    }

    /**
     * 解绑源节点到角色
     *
     * @param roleIds   角色id
     * @param sourceIds id
     */
    @RequestMapping(value = "/unbind_by_role", method = RequestMethod.POST)
    public SaResult unBindSourceByRole(String[] roleIds, String[] sourceIds) {
        return sourceConfigApplication.unBindSourceByRole(roleIds, sourceIds);
    }
}
