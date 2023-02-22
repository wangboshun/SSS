package com.wbs.engine.controller.transform;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/engine/transform")
@Tag(name = "engine", description = "engine模块")
public class TransformController {

    /**
     * transform测试
     * @return
     */
    @ApiOperation("transform测试")
    @GetMapping(value = "/test")
    public String test() {
        return "source Test";
    }
}
