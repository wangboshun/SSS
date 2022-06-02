package org.wbs.quality.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WBS
 * Date:2022/6/1
 */

@Slf4j
@Tag(name = "TestController", description = "Test接口")
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Operation(summary = "get请求", description = "get测试", responses = {@ApiResponse(description = "请求成功", content = @Content(mediaType = "application/json")), @ApiResponse(responseCode = "400", description = "返回400时候错误的原因")})
    @GetMapping(value = "/get")
    public String get() {
        log.info("---get log----");
        return "get";
    }

    @Operation(summary = "post请求", description = "post测试", responses = {@ApiResponse(description = "请求成功", content = @Content(mediaType = "application/json")), @ApiResponse(responseCode = "400", description = "返回400时候错误的原因")})
    @PostMapping(value = "/post")
    public String post() {
        log.info("---post log----");
        return "post";
    }

    @Operation(summary = "添加用户", description = "添加一个用户", parameters = {@Parameter(name = "name", description = "名称")})
    @PostMapping(value = "/add")
    public String add(String name,Integer age) {
        log.info("---add " + name + "   log----");
        return "添加：" + name + "成功";
    }
}
