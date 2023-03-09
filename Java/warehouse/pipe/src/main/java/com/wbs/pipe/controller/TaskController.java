package com.wbs.pipe.controller;

import cn.hutool.core.util.StrUtil;
import com.wbs.common.enums.HttpEnum;
import com.wbs.common.extend.ResponseResult;
import com.wbs.pipe.application.TaskApplication;
import com.wbs.pipe.model.task.TaskInfoModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption TaskController
 */
@Controller
@RestController
@RequestMapping("/pipe/task")
@Tag(name = "pipe", description = "pipe模块")
public class TaskController {

    private TaskApplication taskApplication;

    public TaskController(TaskApplication taskApplication) {
        this.taskApplication = taskApplication;
    }

    @GetMapping(value = "/list")
    public ResponseResult list() {
        return taskApplication.getTaskList();
    }

    @GetMapping(value = "/info")
    public ResponseResult info(@RequestParam(required = false) String id, @RequestParam(required = false) String name) {
        if (StrUtil.isEmpty(id) && StrUtil.isEmpty(name)) {
            return new ResponseResult().ERROR("请输入id或名称", HttpEnum.PARAM_VALID_ERROR);
        }
        return taskApplication.getTask(id, name);
    }

    @GetMapping(value = "/{id}")
    public ResponseResult get(@PathVariable String id) {
        return taskApplication.getTask(id, null);
    }

    @PostMapping(value = "/add")
    public ResponseResult add(@RequestBody TaskInfoModel model) {
        return taskApplication.addTask(model);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseResult delete(@PathVariable String id) {
        return taskApplication.deleteTask(id);
    }

    @PatchMapping(value = "/update")
    public ResponseResult update(@RequestBody TaskInfoModel model) {
        return taskApplication.updateTask(model);
    }
}
