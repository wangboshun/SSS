package com.wbs.pipe.controller;

import com.wbs.common.extend.ResponseResult;
import com.wbs.pipe.application.ColumnConfigApplication;
import com.wbs.pipe.model.ColumnConfigModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption ColumnConfigController
 */
@Controller
@RestController
@RequestMapping("/pipe/column")
@Tag(name = "pipe", description = "pipe模块")
public class ColumnConfigController {

    private ColumnConfigApplication columnConfigApplication;

    public ColumnConfigController(ColumnConfigApplication columnConfigApplication) {
        this.columnConfigApplication = columnConfigApplication;
    }

    @GetMapping(value = "/list")
    public ResponseResult list() {
        List<ColumnConfigModel> list = columnConfigApplication.getColumnConfigList();
        if (list.isEmpty()) {
            return new ResponseResult().NULL();
        } else {
            return new ResponseResult().OK(list);
        }
    }

    @GetMapping(value = "/{taskId}")
    public ResponseResult get(@PathVariable String taskId) {
        ColumnConfigModel model = columnConfigApplication.getColumnConfigByTask(taskId);
        if (model == null) {
            return new ResponseResult().NULL();
        } else {
            return new ResponseResult().OK(model);
        }
    }

    @PostMapping(value = "/add")
    public ResponseResult add(@RequestBody ColumnConfigModel model) {
        return columnConfigApplication.addColumnConfig(model);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseResult delete(@PathVariable String id) {
        return columnConfigApplication.deleteColumnConfig(id);
    }

    @DeleteMapping(value = "/delete_all")
    public ResponseResult deleteAll() {
        return columnConfigApplication.deleteAll();
    }

    @PatchMapping(value = "/update")
    public ResponseResult update(@RequestBody ColumnConfigModel model) {
        return columnConfigApplication.updateColumnConfig(model);
    }
}
