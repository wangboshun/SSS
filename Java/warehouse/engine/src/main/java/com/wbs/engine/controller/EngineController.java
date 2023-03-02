package com.wbs.engine.controller;

import com.wbs.common.database.ConnectionFactory;
import com.wbs.common.database.DataSourceFactory;
import com.wbs.common.database.DbTypeEnum;
import com.wbs.common.database.DbUtils;
import com.wbs.common.extend.ResponseResult;
import com.wbs.engine.core.mysql.MySqlReader;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

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
    private final DataSourceFactory dataSourceFactory;
    private final ConnectionFactory connectionFactory;
    private final Environment environment;

    public EngineController(MySqlReader mySqlReader, DataSourceFactory dataSourceFactory, ConnectionFactory connectionFactory, Environment environment) {
        this.mySqlReader = mySqlReader;
        this.dataSourceFactory = dataSourceFactory;
        this.connectionFactory = connectionFactory;
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
            mySqlReader.config(connection);
            Statement stmt = connection.createStatement();
            Map<String, String> columns = DbUtils.getColumns(connection, "iot_data");
            List<Map<String, Object>> list = mySqlReader.getData("select * from iot_data", stmt, columns);
        } catch (Exception e) {

        }
        return new ResponseResult().Ok("test");
    }
}
