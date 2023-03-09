package com.wbs.pipe.controller;

import cn.hutool.core.util.StrUtil;
import com.wbs.common.enums.HttpEnum;
import com.wbs.common.extend.ResponseResult;
import com.wbs.pipe.application.SourceApplication;
import com.wbs.pipe.model.source.SourceInfoModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption SourceController
 */
@RestController
@RequestMapping("/pipe/source")
@Tag(name = "pipe", description = "pipe模块")
public class SourceController {

    private SourceApplication sourceApplication;

    public SourceController(SourceApplication sourceApplication) {
        this.sourceApplication = sourceApplication;
    }

    @GetMapping(value = "/list")
    public ResponseResult list() {
        return sourceApplication.getSourceList();
    }

    @GetMapping(value = "/info")
    public ResponseResult info(@RequestParam(required = false) String id, @RequestParam(required = false) String name) {
        if (StrUtil.isEmpty(id) && StrUtil.isEmpty(name)) {
            return new ResponseResult().ERROR("请输入id或名称", HttpEnum.PARAM_VALID_ERROR);
        }
        return sourceApplication.getSource(id, name);
    }

    @GetMapping(value = "/{id}")
    public ResponseResult get(@PathVariable String id) {
        return sourceApplication.getSource(id, null);
    }

    @PostMapping(value = "/add")
    public ResponseResult add(@RequestBody SourceInfoModel model) {
        return sourceApplication.addSource(model);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseResult delete(@PathVariable String id) {
        return sourceApplication.deleteSource(id);
    }

    @PatchMapping(value = "/update")
    public ResponseResult update(@RequestBody SourceInfoModel model) {
        return sourceApplication.updateSource(model);
    }
}
