package com.wbs.pipe.controller;

import cn.hutool.core.text.CharSequenceUtil;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.common.database.base.model.TableInfo;
import com.wbs.common.enums.HttpEnum;
import com.wbs.common.extend.ResponseResult;
import com.wbs.pipe.application.SinkApplication;
import com.wbs.pipe.model.sink.SinkInfoModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption SinkController
 */
@Controller
@RestController
@RequestMapping("/pipe/sink")
@Tag(name = "pipe", description = "pipe模块")
public class SinkController {

    private final SinkApplication sinkApplication;

    public SinkController(SinkApplication sinkApplication) {
        this.sinkApplication = sinkApplication;
    }

    @GetMapping(value = "/list")
    public ResponseResult list() {
        List<SinkInfoModel> list = sinkApplication.getSinkList();
        if (list.isEmpty()) {
            return new ResponseResult().NULL();
        }
        return new ResponseResult().OK(list);
    }

    @GetMapping(value = "/info")
    public ResponseResult info(@RequestParam(required = false) String id, @RequestParam(required = false) String name) {
        if (CharSequenceUtil.isEmpty(id) && CharSequenceUtil.isEmpty(name)) {
            return new ResponseResult().ERROR("请输入id或名称", HttpEnum.PARAM_VALID_ERROR);
        }
        SinkInfoModel model = sinkApplication.getSink(id, name);
        if (model == null) {
            return new ResponseResult().NULL();
        }
        return new ResponseResult().OK(model);
    }

    @GetMapping(value = "/{id}")
    public ResponseResult get(@PathVariable String id) {
        SinkInfoModel model = sinkApplication.getSink(id, null);
        if (model == null) {
            return new ResponseResult().NULL();
        }
        return new ResponseResult().OK(model);
    }

    @PostMapping(value = "/add")
    public ResponseResult add(@RequestBody SinkInfoModel model) {
        return sinkApplication.addSink(model);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseResult delete(@PathVariable String id) {
        return sinkApplication.deleteSink(id);
    }

    @DeleteMapping(value = "/delete_all")
    public ResponseResult deleteAll() {
        return sinkApplication.deleteAll();
    }

    @PatchMapping(value = "/update")
    public ResponseResult update(@RequestBody SinkInfoModel model) {
        return sinkApplication.updateSink(model);
    }

    @GetMapping(value = "/tables/{id}")
    public ResponseResult tables(@PathVariable String id) {
        List<TableInfo> list = sinkApplication.getTables(id);
        if (list.isEmpty()) {
            return new ResponseResult().NULL();
        }
        return new ResponseResult().OK(list);
    }

    @GetMapping(value = "/columns/{id}/{table}")
    public ResponseResult columns(@PathVariable String id, @PathVariable String table) {
        List<ColumnInfo> list = sinkApplication.getColumns(id, table);
        if (list.isEmpty()) {
            return new ResponseResult().NULL();
        }
        return new ResponseResult().OK(list);
    }
}
