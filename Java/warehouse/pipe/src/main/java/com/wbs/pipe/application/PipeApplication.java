package com.wbs.pipe.application;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.wbs.common.database.base.DataTable;
import com.wbs.common.database.base.DbTypeEnum;
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
import com.wbs.pipe.model.task.TaskEnum;
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


    private TaskEnum taskEnum;
    private String taskId;
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
            TaskInfoModel taskInfoModel = taskApplication.getTask(taskId, null);
            if (taskInfoModel == null) {
                throw new RuntimeException("任务不存在！");
            }

            taskEnum = TaskEnum.WAIT;
            this.taskId = taskId;
            this.insertResult = new WriterResult();
            this.updateResult = new WriterResult();
            this.st = LocalDateTime.now();

            String sinkId = taskInfoModel.getSink_id();
            String sourceId = taskInfoModel.getSource_id();
            taskEnum = TaskEnum.RUNNING;
            DataTable dataTable = readData(sourceId);

            ColumnConfigModel columnConfig = columnConfigApplication.getColumnConfigByTask(taskId);
            if (columnConfig != null) {
                // 转换
                dataTable = dataTable.mapper(columnConfig.getMapper());
            }
            writeData(sinkId, dataTable);
            this.et = LocalDateTime.now();
            taskEnum = TaskEnum.COMPLETE;
        } catch (Exception e) {
            taskEnum = TaskEnum.ERROR;
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
        DbTypeEnum dbType = DbTypeEnum.values()[sinkInfo.getType()];
        switch (dbType) {
            case MySql:
                mySqlWriter.config(sinkInfo.getTable_name(), connection);
                insertResult = mySqlWriter.insertData(dataTable);
                if (insertResult.getExitsData() != null) {
                    updateResult = mySqlWriter.updateData(insertResult.getExitsData());
                }
                break;
            case SqlServer:
                sqlServerWriter.config(sinkInfo.getTable_name(), connection);
                insertResult = sqlServerWriter.insertData(dataTable);
                if (insertResult.getExitsData() != null) {
                    updateResult = sqlServerWriter.updateData(insertResult.getExitsData());
                }
                break;
            case ClickHouse:
                clickHouseWriter.config(sinkInfo.getTable_name(), connection);
                insertResult = clickHouseWriter.insertData(dataTable);
                if (insertResult.getExitsData() != null) {
                    updateResult = clickHouseWriter.updateData(insertResult.getExitsData());
                }
                break;
            case PostgreSql:
                pgSqlWriter.config(sinkInfo.getTable_name(), connection);
                insertResult = pgSqlWriter.insertData(dataTable);
                if (insertResult.getExitsData() != null) {
                    updateResult = pgSqlWriter.updateData(insertResult.getExitsData());
                }
                break;
            default:
                break;
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
        DbTypeEnum dbType = DbTypeEnum.values()[sourceInfo.getType()];
        DataTable dataTable = new DataTable();
        String sql = format("select * from %s  ", sourceInfo.getTable_name());
        switch (dbType) {
            case MySql:
                mySqlReader.config(sourceInfo.getTable_name(), sourceConnection);
                dataTable = mySqlReader.readData(sql);
                break;
            case SqlServer:
                sqlServerReader.config(sourceInfo.getTable_name(), sourceConnection);
                dataTable = sqlServerReader.readData(sql);
                break;
            case ClickHouse:
                clickHouseReader.config(sourceInfo.getTable_name(), sourceConnection);
                dataTable = clickHouseReader.readData(sql);
                break;
            case PostgreSql:
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
            model.setTask_id(this.taskId);
            model.setSt(this.st);
            model.setEt(this.et);
            model.setStatus(this.taskEnum.name());
            taskLogCollection.insertOne(model);
        } catch (Exception e) {
            logger.error("------PipeApplication addTaskLog error------", e);
        }
    }
}
