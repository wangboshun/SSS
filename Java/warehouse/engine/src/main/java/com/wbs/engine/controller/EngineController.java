package com.wbs.engine.controller;

import com.wbs.common.database.ConnectionFactory;
import com.wbs.common.database.DataSourceFactory;
import com.wbs.common.database.DbTypeEnum;
import com.wbs.common.extend.ResponseResult;
import com.wbs.engine.core.base.TransformAbstract;
import com.wbs.engine.core.mysql.MySqlReader;
import com.wbs.engine.core.mysql.MySqlWriter;
import com.wbs.engine.model.DataRow;
import com.wbs.engine.model.DataTable;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
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
    private final MySqlWriter mySqlWriter;
    private final DataSourceFactory dataSourceFactory;
    private final ConnectionFactory connectionFactory;
    private final TransformAbstract transformAbstract;
    private final Environment environment;

    public EngineController(MySqlReader mySqlReader, MySqlWriter mySqlWriter, DataSourceFactory dataSourceFactory, ConnectionFactory connectionFactory, TransformAbstract transformAbstract, Environment environment) {
        this.mySqlReader = mySqlReader;
        this.mySqlWriter = mySqlWriter;
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


            Map<String, String> mapping = new HashMap<>();
            mapping.put("id", "aaa");
            mapping.put("name", "bbb");
            mapping.put("val", "ccc");

            DataRow row1 = dataTable1.get(1).mapper(mapping);

            //row1.replaceValue("bbb", "u", 6);
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
}
