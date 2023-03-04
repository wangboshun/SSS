package com.wbs.engine.controller;

import com.wbs.common.database.*;
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
import java.util.*;

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
    }

    @GetMapping("/test")
    public ResponseResult test() {
        try {
            String host = environment.getProperty("iot_db.host");
            int port = Integer.parseInt(environment.getRequiredProperty("iot_db.port"));
            String username = environment.getProperty("iot_db.username");
            String password = environment.getProperty("iot_db.password");
            String database = environment.getProperty("iot_db.database");

            DataSource dataSource = dataSourceFactory.createDataSource("iot", host, port, username, password, database, DbTypeEnum.MySql);
            Connection connection = connectionFactory.createConnection("iot", dataSource);
            mySqlReader.config("iot_data", connection);
            DataTable dataTable1 = mySqlReader.readData("select * from iot_data");

            DbUtils.getColumn(connection,"iot_data");
            DbUtils.getTables(connection);
            Map<String, String> mapping = new HashMap<>();
            mapping.put("id", "aaa");
            mapping.put("name", "bbb");
            mapping.put("val", "ccc");


            DataRow add = new DataRow();
            add.put("id1", "aaa");
            add.put("name1", "bbb");
            add.put("val1", "ccc");

            dataTable1.addRow(add);

            List<String> list = new ArrayList<>();
            list.add("id");
            list.add("name");
            dataTable1.removeKeys(list);

            DataRow row1 = dataTable1.get(1).mapper(mapping);

            // row1.replaceValue("bbb", "u", 6);
            // row1.increase("cc",new BigDecimal("22.22"));


            DataTable dataTable2 = dataTable1.mapper(mapping);
            dataTable2.replaceValue("bbb", "u", 6);

            mySqlWriter.config("iot_data1", connection);
            boolean b1 = mySqlWriter.writeData(dataTable1);

            DataRow row = dataTable1.get(0);
            row.put("deviceId", "1111111111111111111111");
            mySqlWriter.exists(row);

            DataTable dataTable = new DataTable();
            dataTable.add(row);
            boolean b2 = mySqlWriter.updateData(dataTable);

        } catch (Exception e) {
            System.out.println(e);
        }
        return new ResponseResult().Ok("test");
    }


    @GetMapping("/test2")
    public ResponseResult test2() {
        try {
            String host = "123.60.141.63";
            int port = 10009;
            String username = "default";
            String password = "mima123456mima";
            String database = "system";

            DataSource dataSource = dataSourceFactory.createDataSource("ck", host, port, username, password, database, DbTypeEnum.ClickHouse);
            Connection connection = connectionFactory.createConnection("ck", dataSource);
            clickHouseReader.config("iot_data", connection);
            DbUtils.getColumn(connection,"iot_data");
            DbUtils.getTables(connection);
            System.out.println();

        } catch (Exception e) {
            System.out.println(e);
        }
        return new ResponseResult().Ok("test");
    }

    @GetMapping("/test3")
    public ResponseResult test3() {
        try {
            String host = "123.60.141.63";
            int port = 10005;
            String username = "postgres";
            String password = "mima123456mima";
            String database = "postgres";

            DataSource dataSource = dataSourceFactory.createDataSource("pg", host, port, username, password, database, "public", DbTypeEnum.PostgreSql);
            Connection connection = connectionFactory.createConnection("pg", dataSource);
            DbUtils.getColumn(connection,"iot_data");
            DbUtils.getTables(connection);
            System.out.println();

        } catch (Exception e) {
            System.out.println(e);
        }
        return new ResponseResult().Ok("test");
    }
}
