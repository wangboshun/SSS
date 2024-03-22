package com.wbs.common.controller;

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

    public TestController() {
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