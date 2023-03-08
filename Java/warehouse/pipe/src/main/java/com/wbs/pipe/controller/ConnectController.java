package com.wbs.pipe.controller;

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
    private ConnectApplication connectApplication;

    public ConnectController(ConnectApplication connectApplication) {
        this.connectApplication = connectApplication;
    }

    @GetMapping(value = "/list")
    public ResponseResult list() {
        List<ConnectInfoModel> list = connectApplication.getConnectList();
        return new ResponseResult().Ok(list);
    }

    @GetMapping(value = "/info")
    public ResponseResult info(@RequestParam(required = false) String id, @RequestParam(required = false) String name) {
        ConnectInfoModel model = connectApplication.getConnectInfo(id, name);
        if (model != null) {
            return new ResponseResult().Ok(model);
        } else {
            return new ResponseResult().Error("无此记录!");
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseResult get(@PathVariable String id) {
        ConnectInfoModel model = connectApplication.getConnectInfo(id, null);
        if (model != null) {
            return new ResponseResult().Ok(model);
        } else {
            return new ResponseResult().Error("无此记录!");
        }
    }

    @PostMapping(value = "/add")
    public ResponseResult add(@RequestBody ConnectInfoModel model) {
        return connectApplication.addConnect(model);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseResult delete(@PathVariable String id) {
        boolean b = connectApplication.deleteConnect(id);
        if (b) {
            return new ResponseResult().Ok("删除成功!");
        } else {
            return new ResponseResult().Error("删除失败!");
        }
    }

    @PatchMapping(value = "/update")
    public ResponseResult update(@RequestBody ConnectInfoModel model) {
        boolean b = connectApplication.updateConnect(model);
        if (b) {
            return new ResponseResult().Ok("更新成功!");
        } else {
            return new ResponseResult().Error("更新失败!");
        }
    }
}
