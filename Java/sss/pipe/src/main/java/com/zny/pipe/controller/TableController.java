package com.zny.pipe.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.pipe.appication.TableConfigApplication;
import com.zny.pipe.model.TableConfigModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date 2022-11-14 16:43
 * 表信息控制器
 */

@RestController
@RequestMapping("/pipe/table")
@Tag(name = "table", description = "table模块")
public class TableController {
    private final TableConfigApplication tableConfigApplication;

    public TableController(TableConfigApplication tableConfigApplication) {
        this.tableConfigApplication = tableConfigApplication;
    }

    /**
     * 获取表信息配置
     *
     * @param id 源节点id
     */
    @GetMapping(value = "/{id}")
    public SaResult get(String id) {
        return tableConfigApplication.getTableConfigById(id);
    }

    /**
     * 根据连接和表名获取表信息
     *
     * @param connectId 连接id
     * @param tableName 表名
     */
    @GetMapping(value = "/get_by_connect")
    public SaResult getByConnect(String connectId, String tableName) {
        List<TableConfigModel> list = tableConfigApplication.getByConnectId(connectId, tableName);
        if (list.isEmpty()) {
            return SaResult.error("表信息不存在！");
        } else {
            return SaResult.data(list);
        }
    }

    /**
     * 根据连接获取下面所有的表名
     *
     * @param connectId 连接id
     */
    @GetMapping(value = "/get_by_db")
    public SaResult getByDb(String connectId) {
        List<Map<String, String>> list = tableConfigApplication.getByDb(connectId);
        if (list.isEmpty()) {
            return SaResult.error("表信息不存在！");
        } else {
            return SaResult.data(list);
        }
    }

    /**
     * 添加表配置
     *
     * @param connectId 连接id
     * @param tableName 表名
     */
    @PostMapping(value = "/add")
    public SaResult add(String connectId, String tableName) {
        return tableConfigApplication.addTableConfig(connectId, tableName);
    }

    /**
     * 更新表配置
     *
     * @param connectId 连接id
     * @param tableName 表名
     */
    @PostMapping(value = "/update")
    public SaResult update(String connectId, String tableName) {
        return tableConfigApplication.updateTableConfig(connectId, tableName);
    }

    /**
     * 删除表配置
     *
     * @param connectId 连接id
     * @param tableName 表名
     */
    @DeleteMapping(value = "/delete")
    public SaResult delete(String connectId, String tableName) {
        return tableConfigApplication.deleteTableConfig(connectId, tableName);
    }
}
