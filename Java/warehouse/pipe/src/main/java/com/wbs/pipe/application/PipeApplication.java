package com.wbs.pipe.application;

import com.wbs.common.database.base.DataTable;
import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.database.factory.ConnectionFactory;
import com.wbs.common.database.factory.DataSourceFactory;
import com.wbs.engine.core.clickhouse.ClickHouseReader;
import com.wbs.engine.core.clickhouse.ClickHouseWriter;
import com.wbs.engine.core.mysql.MySqlReader;
import com.wbs.engine.core.mysql.MySqlWriter;
import com.wbs.engine.core.pgsql.PgSqlReader;
import com.wbs.engine.core.pgsql.PgSqlWriter;
import com.wbs.engine.core.sqlserver.SqlServerReader;
import com.wbs.engine.core.sqlserver.SqlServerWriter;
import com.wbs.pipe.model.ColumnConfigModel;
import com.wbs.pipe.model.connect.ConnectInfoModel;
import com.wbs.pipe.model.sink.SinkInfoModel;
import com.wbs.pipe.model.source.SourceInfoModel;
import com.wbs.pipe.model.task.TaskInfoModel;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;

import static java.lang.String.format;

/**
 * @author WBS
 * @date 2023/3/9 14:53
 * @desciption PipeApplication
 */
@Service
public class PipeApplication {
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
    private final DataSourceFactory dataSourceFactory;
    private final ConnectionFactory connectionFactory;

    public PipeApplication(TaskApplication taskApplication, SourceApplication sourceApplication, SinkApplication sinkApplication, ColumnConfigApplication columnConfigApplication, ConnectApplication connectApplication, MySqlReader mySqlReader, MySqlWriter mySqlWriter, SqlServerReader sqlServerReader, SqlServerWriter sqlServerWriter, ClickHouseReader clickHouseReader, ClickHouseWriter clickHouseWriter, PgSqlReader pgSqlReader, PgSqlWriter pgSqlWriter, DataSourceFactory dataSourceFactory, ConnectionFactory connectionFactory) {
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
        this.dataSourceFactory = dataSourceFactory;
        this.connectionFactory = connectionFactory;
    }

    /**
     * 运行任务
     *
     * @param taskId
     */
    public boolean startTask(String taskId) {
        try {
            TaskInfoModel taskInfoModel = taskApplication.getTask(taskId, null);

            String sinkId = taskInfoModel.getSink_id();
            String sourceId = taskInfoModel.getSource_id();

            DataTable dataTable = readData(sourceId);

            ColumnConfigModel columnConfig = columnConfigApplication.getColumnConfigByTask(taskId);
            dataTable = dataTable.mapper(columnConfig.getMapper()); // 转换

            boolean b = writeData(sinkId, dataTable);

            System.out.println();
        } catch (Exception e) {
            return false;
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
    private boolean writeData(String sinkId, DataTable dataTable) {
        SinkInfoModel sinkInfo = sinkApplication.getSink(sinkId, null);
        Connection connection = getConnection(sinkInfo.getConnect_id());
        DbTypeEnum dbType = DbTypeEnum.values()[sinkInfo.getType()];
        switch (dbType) {
            case MySql:
                mySqlWriter.config(sinkInfo.getTable_name(), connection);
                mySqlWriter.writeData(dataTable);
                break;
            case SqlServer:
                sqlServerWriter.config(sinkInfo.getTable_name(), connection);
                sqlServerWriter.writeData(dataTable);
                break;
            case ClickHouse:
                clickHouseWriter.config(sinkInfo.getTable_name(), connection);
                clickHouseWriter.writeData(dataTable);
                break;
            case PostgreSql:
                pgSqlWriter.config(sinkInfo.getTable_name(), connection);
                pgSqlWriter.writeData(dataTable);
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * 读取数据
     *
     * @param sourceId
     * @return
     */
    private DataTable readData(String sourceId) {
        SourceInfoModel sourceInfo = sourceApplication.getSource(sourceId, null);
        Connection sourceConnection = getConnection(sourceInfo.getConnect_id());
        DbTypeEnum dbType = DbTypeEnum.values()[sourceInfo.getType()];
        DataTable dataTable = new DataTable();
        String sql = format("select * from %s ORDER BY tm desc  LIMIT  %d ", sourceInfo.getTable_name(), 10000000);
        switch (dbType) {
            case MySql:
                mySqlReader.config(sourceInfo.getTable_name(), sourceConnection);
                dataTable = mySqlReader.readData(sql);
                break;
            case SqlServer:
                sqlServerReader.config(sourceInfo.getTable_name(), sourceConnection);
                dataTable = mySqlReader.readData(sql);
                break;
            case ClickHouse:
                clickHouseReader.config(sourceInfo.getTable_name(), sourceConnection);
                dataTable = mySqlReader.readData(sql);
                break;
            case PostgreSql:
                pgSqlReader.config(sourceInfo.getTable_name(), sourceConnection);
                dataTable = mySqlReader.readData(sql);
                break;
            default:
                break;
        }

        return dataTable;
    }

    /**
     * 获取连接
     *
     * @param connectId
     * @return
     */
    private Connection getConnection(String connectId) {
        try {
            ConnectInfoModel model = connectApplication.getConnectInfo(connectId, null);
            String host = model.getHost();
            int port = model.getPort();
            String username = model.getUsername();
            String password = model.getPassword();
            String database = model.getDatabase();
            String schema = model.getSchema();
            DbTypeEnum dbType = DbTypeEnum.values()[model.getType()];
            DataSource dataSource = dataSourceFactory.createDataSource(model.getName(), host, port, username, password, database, schema, dbType);
            return connectionFactory.createConnection(model.getName(), dataSource);
        } catch (Exception e) {
            return null;
        }
    }
}
