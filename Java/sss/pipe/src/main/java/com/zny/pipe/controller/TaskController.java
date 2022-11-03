package com.zny.pipe.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.common.model.PageResult;
import com.zny.pipe.appication.TaskConfigApplication;
import com.zny.pipe.model.TaskConfigModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author WBS
 * Date:2022/10/12
 * task控制器
 */

@RestController
@RequestMapping("/pipe/task")
@Tag(name = "task", description = "task模块")
public class TaskController {

    private final TaskConfigApplication taskConfigApplication;

    public TaskController(TaskConfigApplication taskConfigApplication) {
        this.taskConfigApplication = taskConfigApplication;
    }

    /**
     * 运行任务
     *
     * @param taskId 任务id
     */
    @GetMapping(value = "/run")
    public SaResult run(String taskId) {
        return this.taskConfigApplication.run(taskId);
    }

    /**
     * 获取任务列表
     *
     * @param taskId   任务id
     * @param taskName 任务名
     * @param pageSize 分页大小
     */
    @GetMapping(value = "/list")
    public SaResult list(@RequestParam(required = false) String taskId, @RequestParam(required = false) String taskName, @RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
        PageResult result = taskConfigApplication.getTaskPage(taskId, taskName, pageIndex, pageSize);
        return SaResult.data(result);
    }

    /**
     * 获取任务信息
     *
     * @param id 任务id
     */
    @GetMapping(value = "/{id}")
    public SaResult get(@PathVariable String id) {
        return SaResult.data(taskConfigApplication.getTaskById(id));
    }

    /**
     * 添加任务
     *
     * @param taskName     任务名
     * @param sinkId       目的节点id
     * @param sourceId     源节点id
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param timeStep     步长
     * @param insertType   插入方式
     * @param whereParam   查询条件
     * @param executeType  执行类型
     * @param executeParam 执行参数
     * @param addType      新增方式
     */
    @PostMapping(value = "/add")
    public SaResult add(String taskName, String sinkId, String sourceId, String startTime, String endTime, Integer timeStep, Integer insertType, String whereParam, Integer executeType, String executeParam, Integer addType) {
        return taskConfigApplication.addTask(taskName, sinkId, sourceId, startTime, endTime, timeStep, insertType, whereParam, executeType, executeParam, addType);
    }

    /**
     * 删除任务
     *
     * @param id 任务id
     */
    @DeleteMapping(value = "/{id}")
    public SaResult delete(@PathVariable String id) {
        return taskConfigApplication.deleteTask(id);
    }

    /**
     * 更新任务信息
     *
     * @param id           任务id
     * @param taskName     任务名
     * @param sinkId       目的节点id
     * @param sourceId     源节点id
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param timeStep     步长
     * @param insertType   插入方式
     * @param whereParam   查询条件
     * @param executeType  执行类型
     * @param executeParam 执行参数
     * @param addType      新增方式
     */
    @PatchMapping(value = "/{id}")
    public SaResult update(@PathVariable String id, String taskName, String sinkId, String sourceId, String startTime, String endTime, Integer timeStep, Integer insertType, String whereParam, Integer executeType, String executeParam, Integer addType) {
        return taskConfigApplication.updateTask(id, taskName, sinkId, sourceId, startTime, endTime, timeStep, insertType, whereParam, executeType, executeParam, addType);
    }

    /**
     * 根据用户获取任务
     *
     * @param userId 用户id
     */
    @GetMapping(value = "/by_user")
    public SaResult getTaskByUser(String userId) {
        List<TaskConfigModel> list = taskConfigApplication.getTaskByUser(userId);
        return SaResult.data(list);
    }

    /**
     * 根据角色获取任务
     *
     * @param roleId 角色id
     */
    @GetMapping(value = "/by_role")
    public SaResult getTaskByRole(String roleId) {
        List<TaskConfigModel> list = taskConfigApplication.getTaskByRole(roleId);
        return SaResult.data(list);
    }


    /**
     * 绑定任务到用户
     *
     * @param userIds 用户id
     * @param taskIds 任务id
     */
    @PostMapping(value = "/bind_by_user")
    public SaResult bindTaskByUser(String[] userIds, String[] taskIds) {
        return taskConfigApplication.bindTaskByUser(userIds, taskIds);
    }

    /**
     * 绑定任务到角色
     *
     * @param roleIds 角色id
     * @param taskIds 任务id
     */
    @PostMapping(value = "/bind_by_role")
    public SaResult bindTaskByRole(String[] roleIds, String[] taskIds) {
        return taskConfigApplication.bindTaskByRole(roleIds, taskIds);
    }

    /**
     * 解绑任务到用户
     *
     * @param userIds 用户id
     * @param taskIds id
     */
    @PostMapping(value = "/unbind_by_user")
    public SaResult unBindTaskByUser(String[] userIds, String[] taskIds) {
        return taskConfigApplication.unBindTaskByUser(userIds, taskIds);
    }

    /**
     * 解绑任务到角色
     *
     * @param roleIds 角色id
     * @param taskIds id
     */
    @PostMapping(value = "/unbind_by_role")
    public SaResult unBindTaskByRole(String[] roleIds, String[] taskIds) {
        return taskConfigApplication.unBindTaskByRole(roleIds, taskIds);
    }
}
