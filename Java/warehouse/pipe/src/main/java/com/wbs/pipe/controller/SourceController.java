package com.wbs.pipe.controller;

import cn.hutool.core.text.CharSequenceUtil;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.common.database.base.model.TableInfo;
import com.wbs.common.enums.HttpEnum;
import com.wbs.common.extend.ResponseResult;
import com.wbs.pipe.application.SourceApplication;
import com.wbs.pipe.model.source.SourceInfoModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption SourceController
 */
@RestController
@RequestMapping("/pipe/source")
@Tag(name = "pipe", description = "pipe模块")
public class SourceController {

    private final SourceApplication sourceApplication;

    public SourceController(SourceApplication sourceApplication) {
        this.sourceApplication = sourceApplication;
    }

    @GetMapping(value = "/list")
    public ResponseResult list() {
        List<SourceInfoModel> list = sourceApplication.getSourceList();
        if (list.isEmpty()) {
            return new ResponseResult().NULL();
        } else {
            return new ResponseResult().OK(list);
        }
    }

    @GetMapping(value = "/info")
    public ResponseResult info(@RequestParam(required = false) String id, @RequestParam(required = false) String name) {
        if (CharSequenceUtil.isEmpty(id) && CharSequenceUtil.isEmpty(name)) {
            return new ResponseResult().ERROR("请输入id或名称", HttpEnum.PARAM_VALID_ERROR);
        }
        SourceInfoModel model = sourceApplication.getSource(id, name);
        if (model == null) {
            return new ResponseResult().NULL();
        } else {
            return new ResponseResult().OK(model);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseResult get(@PathVariable String id) {
        SourceInfoModel model = sourceApplication.getSource(id, null);
        if (model == null) {
            return new ResponseResult().NULL();
        } else {
            return new ResponseResult().OK(model);
        }
    }

    @PostMapping(value = "/add")
    public ResponseResult add(@RequestBody SourceInfoModel model) {
        return sourceApplication.addSource(model);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseResult delete(@PathVariable String id) {
        return sourceApplication.deleteSource(id);
    }

    @DeleteMapping(value = "/delete_all")
    public ResponseResult deleteAll() {
        return sourceApplication.deleteAll();
    }

    @PatchMapping(value = "/update")
    public ResponseResult update(@RequestBody SourceInfoModel model) {
        return sourceApplication.updateSource(model);
    }

    @GetMapping(value = "/tables/{id}")
    public ResponseResult tables(@PathVariable String id) {
        List<TableInfo> list = sourceApplication.getTables(id);
        if (list.isEmpty()) {
            return new ResponseResult().NULL();
        } else {
            return new ResponseResult().OK(list);
        }
    }

    @GetMapping(value = "/columns/{id}/{table}")
    public ResponseResult columns(@PathVariable String id, @PathVariable String table) {
        List<ColumnInfo> list = sourceApplication.getColumns(id,table);
        if (list.isEmpty()) {
            return new ResponseResult().NULL();
        } else {
            return new ResponseResult().OK(list);
        }
    }
}
