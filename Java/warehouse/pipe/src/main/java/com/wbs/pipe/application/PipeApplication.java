package com.wbs.pipe.application;

import cn.hutool.core.util.EnumUtil;
import com.wbs.common.database.DbUtils;
import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.common.database.base.model.WhereInfo;
import com.wbs.pipe.application.engine.base.EngineManager;
import com.wbs.pipe.application.engine.base.db.IDbReader;
import com.wbs.pipe.model.source.SourceInfoModel;
import com.wbs.pipe.model.task.TaskInfoModel;
import com.wbs.pipe.model.task.TaskLogModel;
import com.wbs.pipe.model.task.TaskStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author WBS
 * @date 2023/3/9 14:53
 * @desciption PipeApplication
 */
@Service
public class PipeApplication {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final TaskApplication taskApplication;
    private final SourceApplication sourceApplication;
    private final WhereConfigApplication whereConfigApplication;
    private final ConnectApplication connectApplication;
    private final ThreadPoolTaskExecutor defaultExecutor;
    private TaskStatusEnum taskStatusEnum;
    private TaskInfoModel currentTask;
    private Map<String, Future> threadMap = new HashMap<>();


    public PipeApplication(TaskApplication taskApplication, SourceApplication sourceApplication, WhereConfigApplication whereConfigApplication, ConnectApplication connectApplication, ThreadPoolTaskExecutor defaultExecutor) {
        this.taskApplication = taskApplication;
        this.sourceApplication = sourceApplication;
        this.whereConfigApplication = whereConfigApplication;
        this.connectApplication = connectApplication;
        this.defaultExecutor = defaultExecutor;
    }

    /**
     * 运行任务
     *
     * @param taskId
     */
    public boolean startTask(String taskId) {
        this.currentTask = taskApplication.getTask(taskId, null);
        if (this.currentTask == null) {
            throw new RuntimeException("任务不存在！");
        }
        Future<?> future = defaultExecutor.submit(() -> {
            try {
                taskStatusEnum = TaskStatusEnum.WAIT;
                // this.st = LocalDateTime.now();
                String sourceId = this.currentTask.getSource_id();
                SourceInfoModel sourceInfo = sourceApplication.getSource(sourceId, null);
                Connection connection = connectApplication.getConnection(sourceInfo.getConnect_id());
                DbTypeEnum dbType = EnumUtil.getEnumMap(DbTypeEnum.class).get(sourceInfo.getType().toUpperCase());
                List<ColumnInfo> columnList = DbUtils.getColumns(connection, sourceInfo.getTable_name());
                List<WhereInfo> whereList = whereConfigApplication.getWhereConfigByTask(this.currentTask.getId());
                IDbReader reader = EngineManager.getReader(dbType);
                if (reader != null) {
                    taskStatusEnum = TaskStatusEnum.RUNNING;
                    addTaskLog();
                    reader.config(this.currentTask.getId(), sourceInfo.getTable_name(), connection, columnList, whereList);
                    reader.readData();
                }

            } catch (Exception e) {
                logger.error("startTask Exception", e);
            }
        });
        threadMap.put(taskId, future);
        return true;
    }


    public boolean stopTask(String taskId) {
        Future future = threadMap.get(taskId);
        future.cancel(true);
        return true;
    }

    /**
     * 添加任务日志
     */
    public void addTaskLog() {
        try {
            defaultExecutor.execute(() -> {
                TaskLogModel model = new TaskLogModel();
                model.setTask_id(this.currentTask.getId());
                model.setSt(LocalDateTime.now());
                model.setStatus(this.taskStatusEnum.name());
                taskApplication.addTaskLog(model);
            });
        } catch (Exception e) {
            logger.error("------PipeApplication addTaskLog error------", e);
        }
    }
}
