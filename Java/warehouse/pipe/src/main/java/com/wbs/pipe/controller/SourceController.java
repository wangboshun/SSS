package com.wbs.pipe.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption SourceController
 */
@RestController
@RequestMapping("/pipe/source")
@Tag(name = "pipe", description = "pipe模块")
public class SourceController {

    /**
     * source测试
     *
     * @return
     */
    @ApiOperation("source测试")
    @GetMapping(value = "/test")
    public String test() {
        return "source Test";
    }
}
