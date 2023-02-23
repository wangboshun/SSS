package com.wbs.iot.controller;

import com.wbs.iot.application.AliApplication;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WBS
 * @date 2023/2/23 9:57
 * @desciption AliController
 */

@RestController
@RequestMapping("/iot/ali")
@Tag(name = "iot", description = "iot模块")
public class AliController {
    private AliApplication aliApplication;

    public AliController(AliApplication aliApplication) {
        this.aliApplication = aliApplication;
    }

    @GetMapping(value = "/test")
    public String test()
    {
        aliApplication.getProductList();
        return "test Test";
    }
}
