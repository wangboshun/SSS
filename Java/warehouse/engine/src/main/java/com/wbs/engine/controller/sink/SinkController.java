package com.wbs.engine.controller.sink;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/engine/sink")
@Tag(name = "engine", description = "engine模块")
public class SinkController {

    /**
     * sink测试
     * @return
     */
    @ApiOperation("sink测试")
    @GetMapping(value = "/test")
    public String test() {
        return "Sink Test";
    }
}
