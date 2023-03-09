package com.wbs.pipe.controller;

import cn.hutool.core.util.StrUtil;
import com.wbs.common.enums.HttpEnum;
import com.wbs.common.extend.ResponseResult;
import com.wbs.pipe.application.ColumnConfigApplication;
import com.wbs.pipe.model.ColumnConfigModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        return columnConfigApplication.getColumnConfigList();
    }

    @GetMapping(value = "/info")
    public ResponseResult info(@RequestParam(required = false) String id, @RequestParam(required = false) String taskId) {
        if (StrUtil.isEmpty(id) && StrUtil.isEmpty(taskId)) {
            return new ResponseResult().ERROR("请输入id或名称", HttpEnum.PARAM_VALID_ERROR);
        }
        return columnConfigApplication.getColumnConfig(id, taskId);
    }

    @GetMapping(value = "/{id}")
    public ResponseResult get(@PathVariable String id) {
        return columnConfigApplication.getColumnConfig(id, null);
    }

    @PostMapping(value = "/add")
    public ResponseResult add(@RequestBody ColumnConfigModel model) {
        return columnConfigApplication.addColumnConfig(model);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseResult delete(@PathVariable String id) {
        return columnConfigApplication.deleteColumnConfig(id);
    }

    @PatchMapping(value = "/update")
    public ResponseResult update(@RequestBody ColumnConfigModel model) {
        return columnConfigApplication.updateColumnConfig(model);
    }
}
