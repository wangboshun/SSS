package com.zny.pipe.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.common.result.MessageCodeEnum;
import com.zny.common.result.SaResultEx;
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
@RequestMapping("/pipe/")
@Tag(name = "db", description = "db模块")
public class DataBaseController {
    private final TableConfigApplication tableConfigApplication;

    public DataBaseController(TableConfigApplication tableConfigApplication) {
        this.tableConfigApplication = tableConfigApplication;
    }

    /**
     * 获取表信息配置
     *
     * @param id 源节点id
     */
    @GetMapping(value = "/table/{id}")
    public SaResult get(@PathVariable String id) {
        return tableConfigApplication.getTableConfigById(id);
    }

    /**
     * 根据连接和表名获取表信息
     *
     * @param connectId 连接id
     * @param tableName 表名
     */
    @GetMapping(value = "/table/{connectId}/connect")
    public SaResult getByConnect(@PathVariable String connectId, String tableName) {
        List<TableConfigModel> list = tableConfigApplication.getByConnectId(connectId, tableName);
        if (list.isEmpty()) {
            return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "数据不存在！");
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
    @PostMapping(value = "/table/add")
    public SaResult add(String connectId, String tableName) {
        return tableConfigApplication.addTableConfig(connectId, tableName);
    }

    /**
     * 更新表配置
     *
     * @param connectId 连接id
     * @param tableName 表名
     */
    @PostMapping(value = "/table/update")
    public SaResult update(String connectId, String tableName) {
        return tableConfigApplication.updateTableConfig(connectId, tableName);
    }

    /**
     * 删除表配置
     *
     * @param connectId 连接id
     * @param tableName 表名
     */
    @DeleteMapping(value = "/table/delete")
    public SaResult delete(String connectId, String tableName) {
        return tableConfigApplication.deleteTableConfig(connectId, tableName);
    }

    /**
     * 根据连接获取下面所有的表名
     *
     * @param connectId 连接id
     */
    @GetMapping(value = "/db/get_tables")
    public SaResult getTables(String connectId) {
        List<Map<String, String>> list = tableConfigApplication.getTables(connectId);
        if (list.isEmpty()) {
            return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "数据不存在！");
        } else {
            return SaResult.data(list);
        }
    }

    /**
     * 根据连接获取下面所有的数据库
     *
     * @param connectId 连接id
     */
    @GetMapping(value = "/db/get_dbs")
    public SaResult getDataBases(String connectId) {
        List<String> list = tableConfigApplication.getDataBases(connectId);
        if (list.isEmpty()) {
            return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "数据不存在！");
        } else {
            return SaResult.data(list);
        }
    }

    /**
     * 根据连接获取下面所有的模式
     *
     * @param connectId 连接id
     */
    @GetMapping(value = "/db/get_schemas")
    public SaResult getSchemas(String connectId) {
        List<String> list = tableConfigApplication.getSchemas(connectId);
        if (list.isEmpty()) {
            return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "数据不存在！");
        } else {
            return SaResult.data(list);
        }
    }
}
