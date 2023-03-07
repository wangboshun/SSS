package com.wbs.common.controller;

import com.wbs.common.database.factory.ConnectionFactory;
import com.wbs.common.database.factory.DataSourceFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WBS
 * @date 2023/2/23 9:42
 * @desciption TestController
 */
@RestController
@RequestMapping("/common/test")
@Tag(name = "common", description = "common模块")
public class TestController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
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
        return "test Test";
    }
}