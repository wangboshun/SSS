package com.wbs.common.controller;

import com.wbs.common.database.ConnectionFactory;
import com.wbs.common.database.DataSourceFactory;
import com.wbs.common.database.DbTypeEnum;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Map;

/**
 * @author WBS
 * @date 2023/2/23 9:42
 * @desciption TestController
 */
@RestController
@RequestMapping("/common/test")
@Tag(name = "common", description = "common模块")
public class TestController {

    private DataSourceFactory dataSourceFactory;
    private ConnectionFactory connectionFactory;

    public TestController(DataSourceFactory dataSourceFactory, ConnectionFactory connectionFactory) {
        this.dataSourceFactory = dataSourceFactory;
        this.connectionFactory = connectionFactory;
    }

    /**
     * test测试
     *
     * @return
     */
    @GetMapping(value = "/test")
    public String test() {
        try {
            DataSource dataSource1 = dataSourceFactory.createDataSource("test1", "127.0.0.1", 3307, "root", "123456", "test", DbTypeEnum.MySql);
            DataSource dataSource2 = dataSourceFactory.createDataSource("test2", "123.60.141.63", 10009, "default", "mima123456mima", "default", DbTypeEnum.ClickHouse);
            Map<String, DataSource> allDataSource = dataSourceFactory.getAllDataSource();
            Connection test1 = connectionFactory.createConnection("test1", dataSource1);
            Connection test2 = connectionFactory.createConnection("test2", dataSource2);
            Map<String, Connection> allConnection = connectionFactory.getAllConnection();
            System.out.println();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "test Test";
    }
}