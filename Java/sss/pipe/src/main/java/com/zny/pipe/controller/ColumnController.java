package com.zny.pipe.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.pipe.appication.ColumnConfigApplication;
import com.zny.pipe.model.ColumnConfigModel;
import com.zny.pipe.model.dto.ColumnConfigDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author WBS
 * Date 2022-11-15 16:52
 * 字段控制器
 */

@RestController
@RequestMapping("/pipe/column")
@Tag(name = "column", description = "column模块")
public class ColumnController {
    private final ColumnConfigApplication columnConfigApplication;

    public ColumnController(ColumnConfigApplication columnConfigApplication) {
        this.columnConfigApplication = columnConfigApplication;
    }

    /**
     * 获取字段配置列表
     *
     * @param taskId 任务id
     */
    @GetMapping(value = "/get_by_task")
    public SaResult list(String taskId) {
        List<ColumnConfigModel> result = columnConfigApplication.getColumnByTaskId(taskId);
        return SaResult.data(result);
    }

    /**
     * 获取字段配置信息
     *
     * @param id 字段配置id
     */
    @GetMapping(value = "/{id}")
    public SaResult get(@PathVariable String id) {
        return SaResult.data(columnConfigApplication.getColumnById(id));
    }

    /**
     * 添加字段配置
     *
     * @param cloumnData 映射数据
     */
    @PostMapping(value = "/add")
    public SaResult add(@RequestBody List<ColumnConfigDto> cloumnData) {
        return columnConfigApplication.addColumn(cloumnData);
    }

    /**
     * 删除字段配置
     *
     * @param id 字段配置id
     */
    @DeleteMapping(value = "/{id}")
    public SaResult delete(@PathVariable String id) {
        return columnConfigApplication.deleteColumn(id);
    }

    /**
     * 删除字段配置
     *
     * @param taskId 字段配置id
     */
    @DeleteMapping(value = "/delete_by_task")
    public SaResult deleteByTask(String taskId) {
        return columnConfigApplication.deleteColumnByTask(taskId);
    }

    /**
     * 更新字段配置信息
     *
     * @param cloumnData 映射数据
     */
    @PatchMapping(value = "/update")
    public SaResult update(@RequestBody List<ColumnConfigDto> cloumnData) {
        return columnConfigApplication.updateColumn(cloumnData);
    }

}
