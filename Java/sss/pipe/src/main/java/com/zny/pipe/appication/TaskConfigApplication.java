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
import com.zny.pipe.mapper.TaskConfigMapper;
import com.zny.pipe.model.TaskConfigModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class TaskConfigApplication extends ServiceImpl<TaskConfigMapper, TaskConfigModel> {
    private final ResourceApplication resourceApplication;

    public TaskConfigApplication(ResourceApplication resourceApplication) {
        this.resourceApplication = resourceApplication;
    }

    /**
     * 根据id获取任务信息
     *
     * @param id id
     */
    public SaResult getTaskById(String id) {
        if (resourceApplication.haveResource(id, ResourceEnum.Task)) {
            TaskConfigModel model = this.getById(id);
            if (model == null) {
                return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "任务不存在！");
            }
            return SaResult.data(model);
        } else {
            return SaResultEx.error(MessageCodeEnum.AUTH_INVALID);
        }
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
     */
    public SaResult addTask(String taskName, String sinkId, String sourceId, String startTime, String endTime, Integer timeStep, Integer insertType, String whereParam, Integer executeType, String executeParam) {
        QueryWrapper<TaskConfigModel> wrapper = new QueryWrapper<TaskConfigModel>();
        wrapper.eq("task_name", taskName);
        TaskConfigModel model = this.getOne(wrapper);
        if (model != null) {
            return SaResult.error("任务名已存在！");
        }
        model = new TaskConfigModel();
        model.setId(UUID.randomUUID().toString());
        model.setTask_name(taskName);
        model.setSink_id(sinkId);
        model.setSource_id(sourceId);
        model.setStart_time(startTime);
        model.setEnd_time(endTime);
        model.setTime_step(timeStep);
        model.setInsert_type(insertType);
        model.setWhere_param(whereParam);
        model.setExecute_type(executeType);
        model.setExecute_param(executeParam);
        model.setCreate_time(DateUtils.dateToStr(LocalDateTime.now()));
        if (save(model)) {
            return SaResult.ok("添加任务成功！");
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "添加任务失败！");
        }
    }

    /**
     * 查询任务列表
     *
     * @param taskName  任务名
     * @param pageIndex 页码
     * @param pageSize  分页大小
     */
    public PageResult getTaskPage(String taskId, String taskName, Integer pageIndex, Integer pageSize) {
        pageSize = PageUtils.getPageSize(pageSize);
        pageIndex = PageUtils.getPageIndex(pageIndex);
        QueryWrapper<TaskConfigModel> wrapper = new QueryWrapper<TaskConfigModel>();
        if (!resourceApplication.haveResource(wrapper, taskId, "id", ResourceEnum.Task)) {
            return null;
        }
        wrapper.eq(StringUtils.isNotBlank(taskName), "task_name", taskName);
        wrapper.orderByDesc("create_time");
        Page<TaskConfigModel> page = new Page<>(pageIndex, pageSize);
        Page<TaskConfigModel> result = this.page(page, wrapper);
        PageResult pageResult = new PageResult();
        pageResult.setPages(result.getPages());
        pageResult.setRows(result.getRecords());
        pageResult.setTotal(result.getTotal());
        pageResult.setCurrent(result.getCurrent());
        return pageResult;
    }

    /**
     * 删除任务
     *
     * @param id 用户id
     */
    public SaResult deleteTask(String id) {
        QueryWrapper<TaskConfigModel> wrapper = new QueryWrapper<TaskConfigModel>();
        wrapper.eq("id", id);
        TaskConfigModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "任务不存在！");
        }
        if (removeById(id)) {
            return SaResult.ok("删除任务成功！");
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "删除任务失败！");
        }
    }

    /**
     * 更新任务信息
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
     */
    public SaResult updateTask(String id, String taskName, String sinkId, String sourceId, String startTime, String endTime, Integer timeStep, Integer insertType, String whereParam, Integer executeType, String executeParam) {
        QueryWrapper<TaskConfigModel> wrapper = new QueryWrapper<TaskConfigModel>();
        wrapper.eq("id", id);
        TaskConfigModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "任务不存在！");
        }
        if (StringUtils.isNotBlank(taskName)) {
            model.setTask_name(taskName);
        }
        if (StringUtils.isNotBlank(sinkId)) {
            model.setSink_id(sinkId);
        }
        if (StringUtils.isNotBlank(sourceId)) {
            model.setSource_id(sourceId);
        }
        if (StringUtils.isNotBlank(startTime)) {
            model.setStart_time(startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            model.setEnd_time(endTime);
        }
        if (StringUtils.isNotBlank(whereParam)) {
            model.setWhere_param(whereParam);
        }
        if (StringUtils.isNotBlank(executeParam)) {
            model.setExecute_param(executeParam);
        }
        if (timeStep != null) {
            model.setTime_step(timeStep);
        }
        if (insertType != null) {
            model.setInsert_type(insertType);
        }
        if (executeType != null) {
            model.setExecute_type(executeType);
        }

        if (updateById(model)) {
            return SaResult.ok("更新任务信息成功！");
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "删除任务信息失败！");
        }
    }

    /**
     * 根据用户获取任务
     *
     * @param userId 用户id
     */
    public List<TaskConfigModel> getTaskByUser(String userId) {
        Set<String> ids = resourceApplication.getIdsByUser(userId, ResourceEnum.Task);
        List<TaskConfigModel> taskList = new ArrayList<TaskConfigModel>(getTaskByIds(ids));

        //获取所有角色
        Set<String> roleList = resourceApplication.getRoleByUser(userId);

        //遍历角色id，获取资源
        for (String roleId : roleList) {
            taskList.addAll(getTaskByRole(roleId));
        }

        return taskList;
    }

    /**
     * 根据角色获取任务
     *
     * @param roleId 角色id
     */
    public List<TaskConfigModel> getTaskByRole(String roleId) {
        Set<String> ids = resourceApplication.getIdsByRole(roleId, ResourceEnum.Task);
        return getTaskByIds(ids);
    }

    /**
     * 根据资源映射获取任务
     *
     * @param ids 资源id
     */
    private List<TaskConfigModel> getTaskByIds(Set<String> ids) {
        List<TaskConfigModel> list = new ArrayList<TaskConfigModel>();
        if (ids == null || ids.isEmpty()) {
            return list;
        }
        for (String id : ids) {
            TaskConfigModel model = this.getById(id);
            list.add(model);
        }
        return list;
    }

    /**
     * 绑定任务到用户
     *
     * @param userIds 用户id
     * @param taskIds 任务id
     */
    public SaResult bindTaskByUser(String[] userIds, String[] taskIds) {
        if (taskIds == null || taskIds.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.addResource(userIds, ResourceEnum.USER.getIndex(), taskIds, ResourceEnum.Task.getIndex());
    }

    /**
     * 绑定任务到角色
     *
     * @param roleIds 角色id
     * @param taskIds 任务id
     */
    public SaResult bindTaskByRole(String[] roleIds, String[] taskIds) {
        if (taskIds == null || taskIds.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.addResource(roleIds, ResourceEnum.ROLE.getIndex(), taskIds, ResourceEnum.Task.getIndex());
    }

    /**
     * 解绑任务到用户
     *
     * @param userId 用户id
     * @param taskId id
     */
    public SaResult unBindTaskByUser(String[] userId, String[] taskId) {
        if (taskId == null || taskId.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.deleteResource(null, userId, ResourceEnum.USER.getIndex(), taskId, ResourceEnum.Task.getIndex());
    }

    /**
     * 解绑任务到角色
     *
     * @param roleId 角色id
     * @param taskId id
     */
    public SaResult unBindTaskByRole(String[] roleId, String[] taskId) {
        if (taskId == null || taskId.length == 0) {
            return SaResultEx.error(MessageCodeEnum.PARAM_VALID_ERROR, "请输入id");
        }
        return resourceApplication.deleteResource(null, roleId, ResourceEnum.ROLE.getIndex(), taskId, ResourceEnum.Task.getIndex());
    }
}
