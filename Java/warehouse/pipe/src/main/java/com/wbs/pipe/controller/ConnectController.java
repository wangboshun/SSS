package com.wbs.pipe.controller;

import cn.hutool.core.util.StrUtil;
import com.wbs.common.enums.HttpEnum;
import com.wbs.common.extend.ResponseResult;
import com.wbs.pipe.application.ConnectApplication;
import com.wbs.pipe.model.connect.ConnectInfoModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    private ConnectApplication connectApplication;

    public ConnectController(ConnectApplication connectApplication) {
        this.connectApplication = connectApplication;
    }

    @GetMapping(value = "/list")
    public ResponseResult list() {
        return connectApplication.getConnectList();
    }

    @GetMapping(value = "/info")
    public ResponseResult info(@RequestParam(required = false) String id, @RequestParam(required = false) String name) {
        if (StrUtil.isEmpty(id) && StrUtil.isEmpty(name)) {
            return new ResponseResult().ERROR("请输入id或名称", HttpEnum.PARAM_VALID_ERROR);
        }
        return connectApplication.getConnectInfo(id, name);
    }

    @GetMapping(value = "/{id}")
    public ResponseResult get(@PathVariable String id) {
        return connectApplication.getConnectInfo(id, null);
    }

    @PostMapping(value = "/add")
    public ResponseResult add(@RequestBody ConnectInfoModel model) {
        return connectApplication.addConnect(model);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseResult delete(@PathVariable String id) {
        return connectApplication.deleteConnect(id);
    }

    @PatchMapping(value = "/update")
    public ResponseResult update(@RequestBody ConnectInfoModel model) {
        return connectApplication.updateConnect(model);
    }
}
