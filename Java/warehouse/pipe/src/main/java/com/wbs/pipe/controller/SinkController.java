package com.wbs.pipe.controller;

import com.wbs.pipe.application.SinkApplication;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption SinkController
 */
@Service
@RestController
@RequestMapping("/pipe/sink")
@Tag(name = "pipe", description = "pipe模块")
public class SinkController {

    private SinkApplication sinkApplication;

    public SinkController(SinkApplication sinkApplication) {
        this.sinkApplication = sinkApplication;
    }

    /**
     * sink query
     *
     * @return
     */
    @GetMapping(value = "/query")
    public String query() {
        sinkApplication.query();
        return "sink query";
    }

    /**
     * sink add
     *
     * @return
     */
    @GetMapping(value = "/add")
    public String add() {
        sinkApplication.add();
        return "sink add";
    }

    /**
     * sink update
     *
     * @return
     */
    @GetMapping(value = "/update")
    public String update() {
        sinkApplication.update();
        return "sink update";
    }

    /**
     * sink remove
     *
     * @return
     */
    @GetMapping(value = "/remove")
    public String remove() {
        sinkApplication.remove();
        return "sink remove";
    }
}
