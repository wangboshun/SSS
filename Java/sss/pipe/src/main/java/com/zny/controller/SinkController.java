package com.zny.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.pipe.source.MsSqlSource;
import com.zny.pipe.source.MySqlSource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WBS
 * Date:2022/10/12
 * sink目的控制器
 */

@RestController
@RequestMapping("/pipe/sink")
@Tag(name = "sink", description = "sink模块")
public class SinkController {

    private final MySqlSource mySqlSource;
    private final MsSqlSource msSqlSource;

    public SinkController(MySqlSource mySqlSource, MsSqlSource msSqlSource) {
        this.mySqlSource = mySqlSource;
        this.msSqlSource = msSqlSource;
    }

    @RequestMapping(value = "/test1", method = RequestMethod.GET)
    public SaResult test1() {
        mySqlSource.start();
        return SaResult.ok();
    }

    @RequestMapping(value = "/test2", method = RequestMethod.GET)
    public SaResult test2() {
        msSqlSource.start();
        return SaResult.ok();
    }
}
