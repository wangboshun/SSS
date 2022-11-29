package com.zny.pipe.appication;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zny.common.result.MessageCodeEnum;
import com.zny.common.result.SaResultEx;
import com.zny.common.utils.DateUtils;
import com.zny.common.utils.DbEx;
import com.zny.pipe.component.ConnectionFactory;
import com.zny.pipe.mapper.TableConfigMapper;
import com.zny.pipe.model.ConnectConfigModel;
import com.zny.pipe.model.TableConfigModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author WBS
 * 表信息配置
 */

@Service
public class TableConfigApplication extends ServiceImpl<TableConfigMapper, TableConfigModel> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ConnectConfigApplication connectConfigApplication;

    public TableConfigApplication(ConnectConfigApplication connectConfigApplication) {
        this.connectConfigApplication = connectConfigApplication;
    }

    /**
     * 根据连接和表名获取表信息
     *
     * @param connectId 连接id
     * @param tableName 表名
     */
    public List<TableConfigModel> getByConnectId(String connectId, String tableName) {
        QueryWrapper<TableConfigModel> wrapper = new QueryWrapper<>();
        wrapper.eq("connect_id", connectId);
        wrapper.eq("table_name", tableName);
        return this.list(wrapper);
    }

    /**
     * 根据id获取表信息
     *
     * @param id id
     */
    public SaResult getTableConfigById(String id) {
        TableConfigModel model = this.getById(id);
        if (model == null) {
            return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "表信息不存在！");
        }
        return SaResult.data(model);
    }

    /**
     * 获取表信息
     *
     * @param connectId  连接id
     * @param connection 连接
     * @param tableName  表名
     */
    private List<TableConfigModel> getTableColumns(String connectId, Connection connection, String tableName) {
        Statement stmt = null;
        ResultSet result = null;
        List<TableConfigModel> list = new ArrayList<>();
        try {
            stmt = connection.createStatement();
            result = stmt.executeQuery(String.format("SELECT * FROM %s WHERE 1=1 ", tableName));
            List<String> primaryKeys = DbEx.getPrimaryKey(connection, tableName);
            ResultSetMetaData meta = result.getMetaData();
            int columnCount = meta.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = meta.getColumnName(i);
                String[] className = meta.getColumnClassName(i).split("\\.");
                TableConfigModel model = new TableConfigModel();
                model.setId(UUID.randomUUID().toString());
                model.setConnect_id(connectId);
                model.setTable_name(tableName);
                model.setColumn_name(columnName);
                model.setData_type(className[className.length - 1]);
                model.setJdbc_type(meta.getColumnTypeName(i));
                model.setIs_null(meta.isNullable(i));
                model.setCreate_time(DateUtils.dateToStr(LocalDateTime.now()));
                if (primaryKeys.contains(columnName)) {
                    model.setIs_primary(1);
                } else {
                    model.setIs_primary(0);
                }
                list.add(model);
            }

        } catch (Exception e) {
            logger.error("addTableConfig ", e);
            System.out.println("addTableConfig : " + e.getMessage());
        } finally {
            DbEx.release(connection, stmt, result);
        }
        return list;
    }

    /**
     * 添加表信息
     *
     * @param connectId 连接id
     * @param tableName 表名
     */
    public SaResult addTableConfig(String connectId, String tableName) {
        QueryWrapper<TableConfigModel> wrapper = new QueryWrapper<>();
        wrapper.eq("connect_id", connectId);
        wrapper.eq("table_name", tableName);
        long count = this.count(wrapper);
        if (count > 0) {
            return SaResult.error("表信息已存在！");
        }
        ConnectConfigModel connectConfig = connectConfigApplication.getById(connectId);
        Connection connection = ConnectionFactory.getConnection(connectConfig);
        if (connection == null) {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "获取数据连接失败！");
        }
        List<TableConfigModel> list = getTableColumns(connectId, connection, tableName);
        if (saveBatch(list)) {
            return SaResult.ok("添加表信息成功！");
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "添加表信息失败！");
        }
    }

    /**
     * 删除表信息
     *
     * @param connectId 连接id
     * @param tableName 表名
     */
    public SaResult deleteTableConfig(String connectId, String tableName) {
        QueryWrapper<TableConfigModel> wrapper = new QueryWrapper<>();
        wrapper.eq("connect_id", connectId);
        wrapper.eq("table_name", tableName);
        List<TableConfigModel> list = this.list(wrapper);
        if (list.isEmpty()) {
            return SaResult.error("表信息不存在！");
        } else {
            if (this.removeBatchByIds(list.stream().map(TableConfigModel::getId).collect(Collectors.toList()))) {
                return SaResult.ok("删除表信息成功！");
            } else {
                return SaResultEx.error(MessageCodeEnum.DB_ERROR, "删除表信息信息失败！");
            }
        }
    }

    /**
     * 更新表信息信息
     *
     * @param connectId 连接id
     * @param tableName 表名
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public SaResult updateTableConfig(String connectId, String tableName) {
        QueryWrapper<TableConfigModel> wrapper = new QueryWrapper<>();
        wrapper.eq("connect_id", connectId);
        wrapper.eq("table_name", tableName);
        List<TableConfigModel> list = this.list(wrapper);
        if (list.isEmpty()) {
            return SaResult.error("表信息不存在！");
        } else {
            if (this.removeBatchByIds(list.stream().map(TableConfigModel::getId).collect(Collectors.toList()))) {
                return addTableConfig(connectId, tableName);
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return SaResultEx.error(MessageCodeEnum.DB_ERROR, "修改表信息信息失败！");
            }
        }
    }
}
