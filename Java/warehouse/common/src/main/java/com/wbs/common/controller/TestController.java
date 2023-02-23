package com.wbs.common.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common/test")
@Tag(name = "common", description = "common模块")
public class TestController {

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