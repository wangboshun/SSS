package com.zny.pipe.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.pipe.appication.TableConfigApplication;
import com.zny.pipe.model.TableConfigModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author WBS
 * Date 2022-11-14 16:43
 * TableController
 */

@RestController
@RequestMapping("/pipe/table")
@Tag(name = "table", description = "table模块")
public class TableController {
    private final TableConfigApplication tableConfigApplication;

    public TableController(TableConfigApplication tableConfigApplication) {
        this.tableConfigApplication = tableConfigApplication;
    }

    @GetMapping(value = "/{id}")
    public SaResult get(String id) {
        return tableConfigApplication.getTableConfigById(id);
    }

    @GetMapping(value = "/get_by_connect")
    public SaResult get(String connectId, String tableName) {
        List<TableConfigModel> list = tableConfigApplication.getByConnectId(connectId, tableName);
        if (list.isEmpty()) {
            return SaResult.error("表信息不存在！");
        } else {
            return SaResult.data(list);
        }
    }

    @PostMapping(value = "/add")
    public SaResult add(String connectId, String tableName) {
        return tableConfigApplication.addTableConfig(connectId, tableName);
    }

    @PostMapping(value = "/update")
    public SaResult update(String connectId, String tableName) {
        return tableConfigApplication.updateTableConfig(connectId, tableName);
    }

    @DeleteMapping(value = "/delete")
    public SaResult delete(String connectId, String tableName) {
        return tableConfigApplication.deleteTableConfig(connectId, tableName);
    }

}
