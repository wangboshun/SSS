package com.wbs.engine.controller.source;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/engine/source")
@Tag(name = "engine", description = "engine模块")
public class SourceController {

    /**
     * source测试
     * @return
     */
    @ApiOperation("source测试")
    @GetMapping(value = "/test")
    public String test() {
        return "source Test";
    }
}
