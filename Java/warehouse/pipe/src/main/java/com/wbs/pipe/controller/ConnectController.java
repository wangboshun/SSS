package com.wbs.pipe.controller;

import cn.hutool.core.text.CharSequenceUtil;
import com.wbs.common.enums.HttpEnum;
import com.wbs.common.extend.ResponseResult;
import com.wbs.pipe.application.ConnectApplication;
import com.wbs.pipe.model.connect.ConnectInfoModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author WBS
 * @date 2023/3/8 14:43
 * @desciption ConnectController
 */

@Controller
@RestController
@RequestMapping("/pipe/connect")
@Tag(name = "pipe", description = "pipe模块")
public class ConnectController {
    private final ConnectApplication connectApplication;

    public ConnectController(ConnectApplication connectApplication) {
        this.connectApplication = connectApplication;
    }

    @GetMapping(value = "/list")
    public ResponseResult list() {
        List<ConnectInfoModel> list = connectApplication.getConnectList();
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
        ConnectInfoModel model = connectApplication.getConnectInfo(id, name);
        if (model == null) {
            return new ResponseResult().NULL();
        } else {
            return new ResponseResult().OK(model);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseResult get(@PathVariable String id) {
        ConnectInfoModel model = connectApplication.getConnectInfo(id, null);
        if (model == null) {
            return new ResponseResult().NULL();
        } else {
            return new ResponseResult().OK(model);
        }
    }

    @PostMapping(value = "/add")
    public ResponseResult add(@RequestBody ConnectInfoModel model) {
        return connectApplication.addConnect(model);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseResult delete(@PathVariable String id) {
        return connectApplication.deleteConnect(id);
    }

    @DeleteMapping(value = "/delete_all")
    public ResponseResult deleteAll() {
        return connectApplication.deleteAll();
    }

    @PatchMapping(value = "/update")
    public ResponseResult update(@RequestBody ConnectInfoModel model) {
        return connectApplication.updateConnect(model);
    }
}
