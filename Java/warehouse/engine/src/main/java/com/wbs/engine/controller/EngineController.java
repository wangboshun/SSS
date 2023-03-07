package com.wbs.engine.controller;

import com.wbs.common.database.DbUtils;
import com.wbs.common.database.base.DataTable;
import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.common.database.base.model.TableInfo;
import com.wbs.common.database.base.model.WhereInfo;
import com.wbs.common.database.factory.ConnectionFactory;
import com.wbs.common.database.factory.DataSourceFactory;
import com.wbs.common.extend.ResponseResult;
import com.wbs.engine.core.base.TransformAbstract;
import com.wbs.engine.core.clickhouse.ClickHouseReader;
import com.wbs.engine.core.clickhouse.ClickHouseWriter;
import com.wbs.engine.core.mysql.MySqlReader;
import com.wbs.engine.core.mysql.MySqlWriter;
import com.wbs.engine.core.pgsql.PgSqlReader;
import com.wbs.engine.core.pgsql.PgSqlWriter;
import com.wbs.engine.core.sqlserver.SqlServerReader;
import com.wbs.engine.core.sqlserver.SqlServerWriter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WBS
 * @date 2023/3/2 17:03
 * @desciption EngineController
 */
@RestController
@RequestMapping("/engine")
@Tag(name = "engine", description = "engine模块")
public class EngineController {
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
    private final TransformAbstract transformAbstract;
    private final Environment environment;

    public EngineController(MySqlReader mySqlReader, MySqlWriter mySqlWriter, SqlServerReader sqlServerReader, SqlServerWriter sqlServerWriter, ClickHouseReader clickHouseReader, ClickHouseWriter clickHouseWriter, PgSqlReader pgSqlReader, PgSqlWriter pgSqlWriter, DataSourceFactory dataSourceFactory, ConnectionFactory connectionFactory, TransformAbstract transformAbstract, Environment environment) {
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
        this.transformAbstract = transformAbstract;
        this.environment = environment;
        mysqlInit();
        sqlserverInit();
        pgsqlInit();
        ckInit();
    }

    public void mysqlInit() {
        try {
            String host = "123.60.141.63";
            int port = 10001;
            String username = "root";
            String password = "mima123456mima";
            String database = "wbs";
            DataSource dataSource = dataSourceFactory.createDataSource("mysql", host, port, username, password, database, DbTypeEnum.MySql);
            Connection connection = connectionFactory.createConnection("mysql", dataSource);
        } catch (Exception e) {

        }
    }

    public void sqlserverInit() {
        try {
            String host = "123.60.141.63";
            int port = 10012;
            String username = "sa";
            String password = "mima123456mima";
            String database = "test";
            DataSource dataSource = dataSourceFactory.createDataSource("mssql", host, port, username, password, database, DbTypeEnum.SqlServer);
            Connection connection = connectionFactory.createConnection("mssql", dataSource);
        } catch (Exception e) {

        }
    }

    public void pgsqlInit() {
        try {
            String host = "123.60.141.63";
            int port = 10005;
            String username = "postgres";
            String password = "mima123456mima";
            String database = "postgres";
            DataSource dataSource = dataSourceFactory.createDataSource("pgsql", host, port, username, password, database, "public", DbTypeEnum.PostgreSql);
            Connection connection = connectionFactory.createConnection("pgsql", dataSource);
        } catch (Exception e) {

        }
    }

    public void ckInit() {
        try {
            String host = "123.60.141.63";
            int port = 10009;
            String username = "default";
            String password = "mima123456mima";
            String database = "default";

            DataSource dataSource = dataSourceFactory.createDataSource("ck", host, port, username, password, database, DbTypeEnum.ClickHouse);
            Connection connection = connectionFactory.createConnection("ck", dataSource);
        } catch (Exception e) {

        }
    }

    public void getTableAndColumn() {
        Connection mysqlConnection = connectionFactory.getConnect("mysql");
        Connection sqlserverConnection = connectionFactory.getConnect("mssql");
        Connection pgsqlConnection = connectionFactory.getConnect("pgsql");
        Connection ckConnection = connectionFactory.getConnect("ck");

        List<ColumnInfo> columns1 = DbUtils.getColumns(mysqlConnection, "iot_data");
        List<ColumnInfo> columns2 = DbUtils.getColumns(sqlserverConnection, "iot_data");
        List<ColumnInfo> columns3 = DbUtils.getColumns(pgsqlConnection, "iot_data");
        List<ColumnInfo> columns4 = DbUtils.getColumns(ckConnection, "iot_data");

        List<TableInfo> ckTables = DbUtils.getTables(ckConnection);
        List<TableInfo> pgTables = DbUtils.getTables(pgsqlConnection);
        List<TableInfo> mysqlTables = DbUtils.getTables(mysqlConnection);
        List<TableInfo> sqlserverTables = DbUtils.getTables(sqlserverConnection);

        System.out.println();
    }

    @GetMapping("/test")
    public ResponseResult test() {
        try {
            getTableAndColumn();
            System.out.println();
        } catch (Exception e) {
            System.out.println(e);
        }
        return new ResponseResult().Ok("test");
    }

    @GetMapping("/where")
    public ResponseResult where() {
        try {
            Connection mysqlConnection = connectionFactory.getConnect("mysql");
            mySqlReader.config("iot_data", mysqlConnection);

            List<WhereInfo> whereList = new ArrayList<>();
            WhereInfo where1 = new WhereInfo();
            where1.setColumn("id");
            List<Integer> list = new ArrayList<>();
            list.add(1);
            where1.setValue(list);
            where1.setSymbol("IN");
            where1.setOperate("AND");
            whereList.add(where1);


            WhereInfo where2 = new WhereInfo();
            where2.setColumn("val");
            where2.setValue("916.92");
            where2.setSymbol("=");
            where2.setOperate("AND");
            whereList.add(where2);


            WhereInfo where3 = new WhereInfo();
            where3.setColumn("name");
            where3.setValue("%Shimizu Hazuki%");
            where3.setSymbol("like");
            where3.setOperate("AND");
            whereList.add(where3);


            WhereInfo where4 = new WhereInfo();
            where4.setColumn("tm");
            where4.setSymbol("is not null");
            whereList.add(where4);


            //
            DataTable dataTable = mySqlReader.readData(whereList);

            System.out.println();
        } catch (Exception e) {
            System.out.println(e);
        }
        return new ResponseResult().Ok("test");
    }

    @GetMapping("/mysql")
    public ResponseResult mysql() {
        try {
            getTableAndColumn();
            Connection mysqlConnection = connectionFactory.getConnect("mysql");
            mySqlReader.config("iot_data", mysqlConnection);
            DataTable dataTable = mySqlReader.readData("select * from iot_data limit 1000 ");
            mySqlWriter.config("iot_data1", mysqlConnection);
            mySqlWriter.writeData(dataTable);
            System.out.println();

        } catch (Exception e) {
            System.out.println(e);
        }
        return new ResponseResult().Ok("test");
    }

    @GetMapping("/mssql")
    public ResponseResult mssql() {
        try {
            Connection mysqlConnection = connectionFactory.getConnect("mysql");
            Connection sqlserverConnection = connectionFactory.getConnect("mssql");
            mySqlReader.config("iot_data", mysqlConnection);
            DataTable dataTable = mySqlReader.readData("select * from iot_data limit 1000");
            sqlServerWriter.config("iot_data", sqlserverConnection);
            sqlServerWriter.writeData(dataTable);
            System.out.println();

        } catch (Exception e) {
            System.out.println(e);
        }
        return new ResponseResult().Ok("test");
    }

    @GetMapping("/ck")
    public ResponseResult ck() {
        try {
            Connection mysqlConnection = connectionFactory.getConnect("mysql");
            Connection ckConnection = connectionFactory.getConnect("ck");
            mySqlReader.config("iot_data", mysqlConnection);
            DataTable dataTable = mySqlReader.readData("select * from iot_data limit 1000 ");
            clickHouseWriter.config("iot_data", ckConnection);
            clickHouseWriter.writeData(dataTable);
            System.out.println();
        } catch (Exception e) {
            System.out.println(e);
        }
        return new ResponseResult().Ok("test");
    }

    @GetMapping("/pg")
    public ResponseResult pg() {
        try {
            Connection mysqlConnection = connectionFactory.getConnect("mysql");
            Connection pgsqlConnection = connectionFactory.getConnect("pgsql");
            mySqlReader.config("iot_data", mysqlConnection);
            DataTable dataTable = mySqlReader.readData("select * from iot_data ");
            pgSqlWriter.config("iot_data", pgsqlConnection);
            pgSqlWriter.writeData(dataTable);
            System.out.println();
        } catch (Exception e) {
            System.out.println(e);
        }
        return new ResponseResult().Ok("test");
    }
}
