package com.wbs.pipe.application;

import cn.hutool.core.util.EnumUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.wbs.common.database.base.DataTable;
import com.wbs.common.database.base.DbTypeEnum;
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
import com.wbs.pipe.model.ColumnConfigModel;
import com.wbs.pipe.model.sink.SinkInfoModel;
import com.wbs.pipe.model.source.SourceInfoModel;
import com.wbs.pipe.model.task.TaskStatusEnum;
import com.wbs.pipe.model.task.TaskInfoModel;
import com.wbs.pipe.model.task.TaskLogModel;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.time.LocalDateTime;

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
    private final ConnectApplication connectApplication;
    private final MySqlReader mySqlReader;
    private final MySqlWriter mySqlWriter;
    private final SqlServerReader sqlServerReader;
    private final SqlServerWriter sqlServerWriter;
    private final ClickHouseReader clickHouseReader;
    private final ClickHouseWriter clickHouseWriter;
    private final PgSqlReader pgSqlReader;
    private final PgSqlWriter pgSqlWriter;
    private final MongoCollection<TaskLogModel> taskLogCollection;


    private TaskStatusEnum taskStatusEnum;
    private TaskInfoModel currentTask;
    private LocalDateTime st;
    private LocalDateTime et;
    private WriterResult insertResult;
    private WriterResult updateResult;


    public PipeApplication(MongoDatabase defaultMongoDatabase, TaskApplication taskApplication, SourceApplication sourceApplication, SinkApplication sinkApplication, ColumnConfigApplication columnConfigApplication, ConnectApplication connectApplication, MySqlReader mySqlReader, MySqlWriter mySqlWriter, SqlServerReader sqlServerReader, SqlServerWriter sqlServerWriter, ClickHouseReader clickHouseReader, ClickHouseWriter clickHouseWriter, PgSqlReader pgSqlReader, PgSqlWriter pgSqlWriter) {
        this.taskApplication = taskApplication;
        this.sourceApplication = sourceApplication;
        this.sinkApplication = sinkApplication;
        this.columnConfigApplication = columnConfigApplication;
        this.connectApplication = connectApplication;
        this.mySqlReader = mySqlReader;
        this.mySqlWriter = mySqlWriter;
        this.sqlServerReader = sqlServerReader;
        this.sqlServerWriter = sqlServerWriter;
        this.clickHouseReader = clickHouseReader;
        this.clickHouseWriter = clickHouseWriter;
        this.pgSqlReader = pgSqlReader;
        this.pgSqlWriter = pgSqlWriter;
        this.taskLogCollection = defaultMongoDatabase.getCollection("task_log", TaskLogModel.class);
    }

    /**
     * 运行任务
     *
     * @param taskId
     */
    public boolean startTask(String taskId) {
        try {
            this.currentTask = taskApplication.getTask(taskId, null);
            if (this.currentTask == null) {
                throw new RuntimeException("任务不存在！");
            }

            taskStatusEnum = TaskStatusEnum.WAIT;
            this.insertResult = new WriterResult();
            this.updateResult = new WriterResult();
            this.st = LocalDateTime.now();

            String sinkId = this.currentTask.getSink_id();
            String sourceId = this.currentTask.getSource_id();
            taskStatusEnum = TaskStatusEnum.RUNNING;
            DataTable dataTable = readData(sourceId);

            ColumnConfigModel columnConfig = columnConfigApplication.getColumnConfigByTask(taskId);
            if (columnConfig != null) {
                // 转换
                dataTable = dataTable.mapper(columnConfig.getMapper());
            }
            writeData(sinkId, dataTable);
            this.et = LocalDateTime.now();
            taskStatusEnum = TaskStatusEnum.COMPLETE;
        } catch (Exception e) {
            taskStatusEnum = TaskStatusEnum.ERROR;
            return false;
        } finally {
            addTaskLog();
        }
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
        String sql = format("select * from %s  ", sourceInfo.getTable_name());
        switch (dbType) {
            case MYSQL:
                mySqlReader.config(sourceInfo.getTable_name(), sourceConnection);
                dataTable = mySqlReader.readData(sql);
                break;
            case SQLSERVER:
                sqlServerReader.config(sourceInfo.getTable_name(), sourceConnection);
                dataTable = sqlServerReader.readData(sql);
                break;
            case CLICKHOUSE:
                clickHouseReader.config(sourceInfo.getTable_name(), sourceConnection);
                dataTable = clickHouseReader.readData(sql);
                break;
            case POSTGRESQL:
                pgSqlReader.config(sourceInfo.getTable_name(), sourceConnection);
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
            TaskLogModel model = new TaskLogModel();
            ObjectId id = new ObjectId();
            model.setId(id.toString());
            model.setInsert(this.insertResult);
            model.setUpdate(this.updateResult);
            model.setCt(LocalDateTime.now());
            model.setTask_id(this.currentTask.getId());
            model.setSt(this.st);
            model.setEt(this.et);
            model.setStatus(this.taskStatusEnum.name());
            taskLogCollection.insertOne(model);
        } catch (Exception e) {
            logger.error("------PipeApplication addTaskLog error------", e);
        }
    }
}
