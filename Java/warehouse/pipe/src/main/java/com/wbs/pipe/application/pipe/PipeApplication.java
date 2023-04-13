package com.wbs.pipe.application.pipe;

import cn.hutool.core.util.EnumUtil;
import com.wbs.common.database.DbUtils;
import com.wbs.common.database.base.DataTable;
import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.common.database.base.model.WhereInfo;
import com.wbs.engine.core.base.WriteTypeEnum;
import com.wbs.engine.core.clickhouse.ClickHouseReader;
import com.wbs.engine.core.clickhouse.ClickHouseWriter;
import com.wbs.engine.core.mysql.MySqlReader;
import com.wbs.engine.core.mysql.MySqlWriter;
import com.wbs.engine.core.pgsql.PgSqlReader;
import com.wbs.engine.core.pgsql.PgSqlWriter;
import com.wbs.engine.core.sqlserver.SqlServerReader;
import com.wbs.engine.core.sqlserver.SqlServerWriter;
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

import static java.lang.String.format;

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
    private final MySqlReader mySqlReader;
    private final MySqlWriter mySqlWriter;
    private final SqlServerReader sqlServerReader;
    private final SqlServerWriter sqlServerWriter;
    private final ClickHouseReader clickHouseReader;
    private final ClickHouseWriter clickHouseWriter;
    private final PgSqlReader pgSqlReader;
    private final PgSqlWriter pgSqlWriter;
    private final ThreadPoolTaskExecutor defaultExecutor;


    private TaskStatusEnum taskStatusEnum;
    private TaskInfoModel currentTask;
    private LocalDateTime st;
    private LocalDateTime et;
    private WriterResult insertResult;
    private WriterResult updateResult;
    private Map<String, Future> threadMap = new HashMap<>();


    public PipeApplication(TaskApplication taskApplication, SourceApplication sourceApplication, SinkApplication sinkApplication, ColumnConfigApplication columnConfigApplication, WhereConfigApplication whereConfigApplication, ConnectApplication connectApplication, MySqlReader mySqlReader, MySqlWriter mySqlWriter, SqlServerReader sqlServerReader, SqlServerWriter sqlServerWriter, ClickHouseReader clickHouseReader, ClickHouseWriter clickHouseWriter, PgSqlReader pgSqlReader, PgSqlWriter pgSqlWriter, ThreadPoolTaskExecutor defaultExecutor) {
        this.taskApplication = taskApplication;
        this.sourceApplication = sourceApplication;
        this.sinkApplication = sinkApplication;
        this.columnConfigApplication = columnConfigApplication;
        this.whereConfigApplication = whereConfigApplication;
        this.connectApplication = connectApplication;
        this.mySqlReader = mySqlReader;
        this.mySqlWriter = mySqlWriter;
        this.sqlServerReader = sqlServerReader;
        this.sqlServerWriter = sqlServerWriter;
        this.clickHouseReader = clickHouseReader;
        this.clickHouseWriter = clickHouseWriter;
        this.pgSqlReader = pgSqlReader;
        this.pgSqlWriter = pgSqlWriter;
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
        switch (dbType) {
            case MYSQL:
                mySqlWriter.config(sinkInfo.getTable_name(), connection);
                insertResult = mySqlWriter.insertData(dataTable);
                if (writeTypeEnum.equals(WriteTypeEnum.UPSERT) && insertResult.getExistData() != null) {
                    updateResult = mySqlWriter.updateData(insertResult.getExistData());
                }
                break;
            case SQLSERVER:
                sqlServerWriter.config(sinkInfo.getTable_name(), connection);
                insertResult = sqlServerWriter.insertData(dataTable);
                if (writeTypeEnum.equals(WriteTypeEnum.UPSERT) && insertResult.getExistData() != null) {
                    updateResult = sqlServerWriter.updateData(insertResult.getExistData());
                }
                break;
            case CLICKHOUSE:
                clickHouseWriter.config(sinkInfo.getTable_name(), connection);
                insertResult = clickHouseWriter.insertData(dataTable);
                if (writeTypeEnum.equals(WriteTypeEnum.UPSERT) && insertResult.getExistData() != null) {
                    updateResult = clickHouseWriter.updateData(insertResult.getExistData());
                }
                break;
            case POSTGRESQL:
                pgSqlWriter.config(sinkInfo.getTable_name(), connection);
                insertResult = pgSqlWriter.insertData(dataTable);
                if (writeTypeEnum.equals(WriteTypeEnum.UPSERT) && insertResult.getExistData() != null) {
                    updateResult = pgSqlWriter.updateData(insertResult.getExistData());
                }
                break;
            default:
                break;
        }

        // 如果是存在忽略
        if (writeTypeEnum.equals(WriteTypeEnum.IGNORE) && insertResult.getExistData() != null) {
            insertResult.setIgnoreCount(insertResult.getExistData().size());
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
        Connection sourceConnection = connectApplication.getConnection(sourceInfo.getConnect_id());
        DbTypeEnum dbType = EnumUtil.getEnumMap(DbTypeEnum.class).get(sourceInfo.getType().toUpperCase());
        DataTable dataTable = new DataTable();
        List<ColumnInfo> columnList = DbUtils.getColumns(sourceConnection, sourceInfo.getTable_name());
        List<WhereInfo> whereList = whereConfigApplication.getWhereConfigByTask(currentTask.getId());
        String sql = format("select * from %s  ", sourceInfo.getTable_name());

        switch (dbType) {
            case MYSQL:
                mySqlReader.setTableName(sourceInfo.getTable_name());
                mySqlReader.setConnection(sourceConnection);
                mySqlReader.setColumnList(columnList);
                mySqlReader.setWhereList(whereList);
                mySqlReader.config();
                dataTable = mySqlReader.readData();
                break;
            case SQLSERVER:
                sqlServerReader.setTableName(sourceInfo.getTable_name());
                sqlServerReader.setConnection(sourceConnection);
                sqlServerReader.setColumnList(columnList);
                sqlServerReader.setWhereList(whereList);
                sqlServerReader.config();
                dataTable = sqlServerReader.readData();
                break;
            case CLICKHOUSE:
                clickHouseReader.setTableName(sourceInfo.getTable_name());
                clickHouseReader.setConnection(sourceConnection);
                clickHouseReader.setColumnList(columnList);
                clickHouseReader.setWhereList(whereList);
                clickHouseReader.config();
                dataTable = clickHouseReader.readData();
                break;
            case POSTGRESQL:
                pgSqlReader.setTableName(sourceInfo.getTable_name());
                pgSqlReader.setConnection(sourceConnection);
                pgSqlReader.setColumnList(columnList);
                pgSqlReader.setWhereList(whereList);
                pgSqlReader.config();
                dataTable = pgSqlReader.readData(sql);
                break;
            default:
                break;
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
