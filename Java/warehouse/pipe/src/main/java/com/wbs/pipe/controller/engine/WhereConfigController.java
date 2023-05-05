package com.wbs.pipe.controller.engine;

import com.wbs.common.database.base.model.WhereInfo;
import com.wbs.common.extend.ResponseResult;
import com.wbs.pipe.application.WhereConfigApplication;
import com.wbs.pipe.model.engine.WhereConfigModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption WhereConfigController
 */
@Controller
@RestController
@RequestMapping("/pipe/where")
@Tag(name = "pipe", description = "pipe模块")
public class WhereConfigController {

    private final WhereConfigApplication whereConfigApplication;

    public WhereConfigController(WhereConfigApplication whereConfigApplication) {
        this.whereConfigApplication = whereConfigApplication;
    }

    @GetMapping(value = "/list")
    public ResponseResult list() {
        List<WhereConfigModel> list = whereConfigApplication.getWhereConfigList();
        if (list.isEmpty()) {
            return new ResponseResult().NULL();
        }
        return new ResponseResult().OK(list);
    }

    @GetMapping(value = "/{taskId}")
    public ResponseResult get(@PathVariable String taskId) {
        List<WhereInfo> list = whereConfigApplication.getWhereConfigByTask(taskId);
        if (list == null) {
            return new ResponseResult().NULL();
        }
        return new ResponseResult().OK(list);
    }

    @PostMapping(value = "/add")
    public ResponseResult add(@RequestBody WhereConfigModel model) {
        return whereConfigApplication.addWhereConfig(model);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseResult delete(@PathVariable String id) {
        return whereConfigApplication.deleteWhereConfig(id);
    }

    @DeleteMapping(value = "/delete_all")
    public ResponseResult deleteAll() {
        return whereConfigApplication.deleteAll();
    }

    @PatchMapping(value = "/update")
    public ResponseResult update(@RequestBody WhereConfigModel model) {
        return whereConfigApplication.updateWhereConfig(model);
    }
}
