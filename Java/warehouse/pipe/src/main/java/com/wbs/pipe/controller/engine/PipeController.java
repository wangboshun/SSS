package com.wbs.pipe.controller.engine;

import com.wbs.common.extend.ResponseResult;
import com.wbs.pipe.application.PipeApplication;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WBS
 * @date 2023/3/9 16:02
 * @desciption PipeController
 */
@Controller
@RestController
@RequestMapping("/pipe")
@Tag(name = "pipe", description = "pipe模块")
public class PipeController {
    private final PipeApplication pipeApplication;

    public PipeController(PipeApplication pipeApplication) {
        this.pipeApplication = pipeApplication;
    }

    /**
     * 开启任务
     *
     * @param id 任务id
     * @return
     */
    @PostMapping(value = "/start/{id}")
    public ResponseResult start(@PathVariable String id) {
        boolean b = pipeApplication.startTask(id);
        if (!b) {
            return new ResponseResult().FAILED();
        }
        return new ResponseResult().OK();
    }

    /**
     * 停止任务
     *
     * @param id 任务id
     */
    @PostMapping(value = "/stop/{id}")
    public ResponseResult stop(@PathVariable String id) {
        boolean b = pipeApplication.stopTask(id);
        if (!b) {
            return new ResponseResult().FAILED();
        }
        return new ResponseResult().OK();
    }
}
