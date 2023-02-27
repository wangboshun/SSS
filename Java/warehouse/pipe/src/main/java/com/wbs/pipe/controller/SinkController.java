package com.wbs.pipe.controller;

import com.wbs.pipe.application.SinkApplication;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @return
     */
    @GetMapping(value = "/query")
    public String query() {
        sinkApplication.query();
        return "sink Test";
    }

    /**
     * sink add
     * @return
     */
    @GetMapping(value = "/add")
    public String add() {
        sinkApplication.add();
        return "sink Test";
    }

    /**
     * sink update
     * @return
     */
    @GetMapping(value = "/update")
    public String update() {
        sinkApplication.update();
        return "sink Test";
    }

    /**
     * sink remove
     * @return
     */
    @GetMapping(value = "/remove")
    public String remove() {
        sinkApplication.remove();
        return "sink Test";
    }
}
