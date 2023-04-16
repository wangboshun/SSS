package com.wbs.pipe.application.pipe;

import cn.hutool.core.util.EnumUtil;
import com.wbs.common.database.DbUtils;
import com.wbs.common.database.base.DataTable;
import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.common.database.base.model.WhereInfo;
import com.wbs.engine.core.base.IReader;
import com.wbs.engine.core.base.IWriter;
import com.wbs.engine.core.base.WriteTypeEnum;
import com.wbs.engine.core.base.EngineManager;
import com.wbs.engine.model.WriterResult;
import com.wbs.pipe.application.ConnectApplication;
import com.wbs.pipe.application.SinkApplication;
import com.wbs.pipe.application.SourceApplication;
import com.wbs.pipe.application.TaskApplication;
import com.wbs.pipe.model.pipe.ColumnConfigModel;
import com.wbs.pipe.model.sink.SinkInfoModel;
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
    private final SinkApplication sinkApplication;
    private final ColumnConfigApplication columnConfigApplication;
    private final WhereConfigApplication whereConfigApplication;
    private final ConnectApplication connectApplication;
    private final ThreadPoolTaskExecutor defaultExecutor;


    private TaskStatusEnum taskStatusEnum;
    private TaskInfoModel currentTask;
    private LocalDateTime st;
    private LocalDateTime et;
    private WriterResult insertResult;
    private WriterResult updateResult;
    private Map<String, Future> threadMap = new HashMap<>();


    public PipeApplication(TaskApplication taskApplication, SourceApplication sourceApplication, SinkApplication sinkApplication, ColumnConfigApplication columnConfigApplication, WhereConfigApplication whereConfigApplication, ConnectApplication connectApplication, ThreadPoolTaskExecutor defaultExecutor) {
        this.taskApplication = taskApplication;
        this.sourceApplication = sourceApplication;
        this.sinkApplication = sinkApplication;
        this.columnConfigApplication = columnConfigApplication;
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
                this.insertResult = new WriterResult();
                this.updateResult = new WriterResult();
                this.st = LocalDateTime.now();

                String sinkId = this.currentTask.getSink_id();
                String sourceId = this.currentTask.getSource_id();
                taskStatusEnum = TaskStatusEnum.RUNNING;
                DataTable dataTable = readData(sourceId);

                // 如果线程中断，停止后续
                if (Thread.currentThread().isInterrupted()) {
                    this.et = LocalDateTime.now();
                    taskStatusEnum = TaskStatusEnum.CANCEL;
                } else {
                    ColumnConfigModel columnConfig = columnConfigApplication.getColumnConfigByTask(taskId);
                    if (columnConfig != null) {
                        // 转换
                        dataTable = dataTable.mapper(columnConfig.getMapper());
                    }
                    writeData(sinkId, dataTable);
                    this.et = LocalDateTime.now();
                    taskStatusEnum = TaskStatusEnum.COMPLETE;
                }
            } catch (Exception e) {
                taskStatusEnum = TaskStatusEnum.ERROR;
                logger.error("startTask Exception", e);
            } finally {
                addTaskLog();
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
     * 写入数据
     *
     * @param sinkId
     * @param dataTable
     * @return
     */
    private void writeData(String sinkId, DataTable dataTable) {
        SinkInfoModel sinkInfo = sinkApplication.getSink(sinkId, null);
        Connection connection = connectApplication.getConnection(sinkInfo.getConnect_id());
        DbTypeEnum dbType = EnumUtil.getEnumMap(DbTypeEnum.class).get(sinkInfo.getType().toUpperCase());
        WriteTypeEnum writeTypeEnum = EnumUtil.getEnumMap(WriteTypeEnum.class).get(this.currentTask.getExist_type().toUpperCase());
        List<ColumnInfo> columnList = DbUtils.getColumns(connection, sinkInfo.getTable_name());
        IWriter writer = EngineManager.getWriter(dbType);
        if (writer != null) {
            writer.config(sinkInfo.getTable_name(), connection, columnList);
            insertResult = writer.insertData(dataTable);
            if (insertResult.getExistData() != null) {
                // 如果是存在更新
                if (writeTypeEnum.equals(WriteTypeEnum.UPSERT)) {
                    updateResult = writer.updateData(insertResult.getExistData());
                }
                // 如果是存在忽略
                else if (writeTypeEnum.equals(WriteTypeEnum.IGNORE)) {
                    insertResult.setIgnoreCount(insertResult.getExistData().size());
                }
            }
        }
    }

    /**
     * 读取数据
     *
     * @param sourceId
     * @return
     */
    private DataTable readData(String sourceId) {
        SourceInfoModel sourceInfo = sourceApplication.getSource(sourceId, null);
        Connection connection = connectApplication.getConnection(sourceInfo.getConnect_id());
        DbTypeEnum dbType = EnumUtil.getEnumMap(DbTypeEnum.class).get(sourceInfo.getType().toUpperCase());
        DataTable dataTable = new DataTable();
        List<ColumnInfo> columnList = DbUtils.getColumns(connection, sourceInfo.getTable_name());
        List<WhereInfo> whereList = whereConfigApplication.getWhereConfigByTask(currentTask.getId());
        IReader reader = EngineManager.getReader(dbType);
        if (reader != null) {
            reader.config(sourceInfo.getTable_name(), connection, columnList, whereList);
            dataTable = reader.readData();
        }

        return dataTable;
    }

    /**
     * 添加任务日志
     */
    public void addTaskLog() {
        try {
            defaultExecutor.execute(() -> {
                TaskLogModel model = new TaskLogModel();
                model.setInsert(this.insertResult);
                model.setUpdate(this.updateResult);
                model.setTask_id(this.currentTask.getId());
                model.setSt(this.st);
                model.setEt(this.et);
                model.setStatus(this.taskStatusEnum.name());
                taskApplication.addTaskLog(model);
            });
        } catch (Exception e) {
            logger.error("------PipeApplication addTaskLog error------", e);
        }
    }
}
