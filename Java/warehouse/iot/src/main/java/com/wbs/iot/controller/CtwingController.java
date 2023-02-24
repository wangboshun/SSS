package com.wbs.iot.controller;

import com.wbs.iot.application.CtWingApplication;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption CtwingController
 */

@RestController
@RequestMapping("/iot/ctwing")
@Tag(name = "iot", description = "iot模块")
public class CtwingController {
    private CtWingApplication ctwingApplication;

    public CtwingController(CtWingApplication ctwingApplication) {
        this.ctwingApplication = ctwingApplication;
    }

    @GetMapping(value = "/test")
    public String test()
    {
        ctwingApplication.getProductList();
        return "test Test";
    }
}
