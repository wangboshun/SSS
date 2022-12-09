package com.zny.pipe.component.base;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.zny.common.enums.DbTypeEnum;
import com.zny.common.enums.InsertTypeEnum;
import com.zny.common.enums.RedisKeyEnum;
import com.zny.common.utils.DateUtils;
import com.zny.common.utils.database.DbEx;
import com.zny.common.utils.database.TableInfo;
import com.zny.pipe.component.ConnectionFactory;
import com.zny.pipe.component.base.enums.TaskStatusEnum;
import com.zny.pipe.component.base.interfaces.SinkBase;
import com.zny.pipe.model.ColumnConfigModel;
import com.zny.pipe.model.ConnectConfigModel;
import com.zny.pipe.model.SinkConfigModel;
import com.zny.pipe.model.TaskConfigModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author WBS
 * Date:2022/10/19
 * Sink抽象基类
 */
public class SinkAbstract implements SinkBase {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    public SinkConfigModel sinkConfig;
    public ConnectConfigModel connectConfig;
    public TaskConfigModel taskConfig;
    public Connection connection;
    public List<ColumnConfigModel> columnList;
    public List<TableInfo> tableInfo;
    private String cacheKey;
    private DbTypeEnum dbType;
    private String tableName;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 配置
     *
     * @param sinkConfig    目的信息
     * @param connectConfig 链接信息
     * @param taskConfig    任务信息
     */
    @Override
    public void config(SinkConfigModel sinkConfig, ConnectConfigModel connectConfig, TaskConfigModel taskConfig, List<ColumnConfigModel> columnList, Integer version) {
        this.sinkConfig = sinkConfig;
        this.connectConfig = connectConfig;
        this.taskConfig = taskConfig;
        this.cacheKey = RedisKeyEnum.SINK_TIME_CACHE + ":" + taskConfig.getId() + ":" + version;
        connection = ConnectionFactory.getConnection(connectConfig);
        dbType = DbTypeEnum.values()[this.connectConfig.getDb_type()];
        tableName = this.sinkConfig.getTable_name();
        this.columnList = columnList;
        tableInfo = DbEx.getTableInfo(connection, tableName);
    }

    /**
     * 开始
     *
     * @param list 数据消息
     */
    @Override
    public void start(List<Map<String, Object>> list) {
        System.out.println("SinkAbstract start");
        splitData(list);
    }

    /**
     * 获取主键
     */
    private Map<String, String> getPrimaryKey() {
        Map<String, String> map = new HashMap<>();
        for (ColumnConfigModel item : columnList) {
            //获取表信息
            TableInfo info = tableInfo.stream().filter(x -> x.getColumn_name().equals(item.getSink_column())).findFirst().get();
            if (info.getIs_primary() > 0) {
                map.put(item.getSink_column(), info.getJava_type());
            }
        }
        return map;
    }

    /**
     * 拆分数据
     *
     * @param list 数据消息
     */
    public void splitData(List<Map<String, Object>> list) {
        List<Map<String, Object>> ignoreList = new ArrayList<>();
        List<Map<String, Object>> addList = new ArrayList<>();
        List<Map<String, Object>> updateList = new ArrayList<>();
        try {
            Map<String, String> primaryColumn = getPrimaryKey();
            InsertTypeEnum insertType = InsertTypeEnum.values()[this.taskConfig.getInsert_type()];
            for (Map<String, Object> item : list) {
                //数据是否已存在
                boolean hasData = exists(connection, tableName, item, primaryColumn, dbType);
                switch (insertType) {
                    case IGNORE:
                        if (hasData) {
                            ignoreList.add(item);
                        } else {
                            addList.add(item);
                        }
                        break;
                    case UPDATE:
                        if (hasData) {
                            updateList.add(item);
                        } else {
                            addList.add(item);
                        }
                        break;
                    default:
                        break;
                }
            }

            if (!addList.isEmpty()) {
                addData(addList);
            }
            if (!updateList.isEmpty()) {
                updateData(updateList);
            }
        } catch (Exception e) {
            logger.error("SinkAbstract setData", e);
            System.out.println("SinkAbstract setData: " + e.getMessage());
        } finally {
            DbEx.release(connection);
        }

        //更新数量缓存
        updateCountCache(ignoreList.size(), addList.size(), updateList.size());
    }

    /**
     * 添加数据
     *
     * @param list 数据集
     */
    private Boolean addData(List<Map<String, Object>> list) {
        PreparedStatement pstm = null;
        try {
            StringBuilder columnSql = new StringBuilder();
            StringBuilder valueSql = new StringBuilder();
            for (ColumnConfigModel item : columnList) {
                //找到对应的列信息
                TableInfo info = tableInfo.stream().filter(x -> x.getColumn_name().equals(item.getSink_column())).findFirst().get();
                columnSql.append(DbEx.convertName(item.getSink_column(), dbType)).append(",");
                valueSql.append("?,");
            }
            columnSql.deleteCharAt(columnSql.length() - 1);
            valueSql.deleteCharAt(valueSql.length() - 1);
            String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", DbEx.convertName(tableName, dbType), columnSql, valueSql);
            this.connection.setAutoCommit(false);
            pstm = connection.prepareStatement(sql);
            for (Map<String, Object> dataItem : list) {
                int index = 1;
                for (ColumnConfigModel item : columnList) {
                    TableInfo info = tableInfo.stream().filter(x -> x.getColumn_name().equals(item.getSink_column())).findFirst().get();
                    DbEx.setParam(pstm, index, dataItem.get(item.getSink_column()), info.getJava_type());
                    index++;
                }
                pstm.addBatch();
            }
            pstm.executeBatch();
            pstm.clearBatch();
            connection.commit();
        } catch (SQLException e) {
            DbEx.release(pstm);
            logger.error("SinkAbstract setData", e);
            System.out.println("SinkAbstract setData: " + e.getMessage());
        } finally {
            DbEx.release(pstm);
        }
        return true;
    }

    /**
     * 更新数据
     *
     * @param list 数据集
     */
    private Boolean updateData(List<Map<String, Object>> list) {
        PreparedStatement pstm = null;
        try {
            StringBuilder columnSql = new StringBuilder();
            StringBuilder whereSql = new StringBuilder();
            Table<String, Integer, String> columnTable = HashBasedTable.create();

            int index = 0;
            for (ColumnConfigModel item : columnList) {
                //找到对应的列信息
                TableInfo info = tableInfo.stream().filter(x -> x.getColumn_name().equals(item.getSink_column())).findFirst().get();
                //主键
                if (info.getIs_primary() > 0) {
                    whereSql.append(DbEx.convertName(item.getSink_column(), dbType)).append("=?");
                    whereSql.append(" AND ");
                }
                //非主键
                else {
                    columnSql.append(DbEx.convertName(item.getSink_column(), dbType)).append("=?");
                    columnSql.append(",");
                }
                columnTable.put(item.getSink_column(), index++, info.getJava_type());
            }

            columnSql.deleteCharAt(columnSql.length() - 1);
            whereSql.delete(whereSql.length() - 4, whereSql.length());
            String sql = String.format("UPDATE %s SET %s WHERE %s", DbEx.convertName(tableName, dbType), columnSql, whereSql);
            this.connection.setAutoCommit(false);
            pstm = connection.prepareStatement(sql);
            for (Map<String, Object> dataItem : list) {
                Set<Table.Cell<String, Integer, String>> cells = columnTable.cellSet();
                for (Table.Cell<String, Integer, String> item : cells) {
                    DbEx.setParam(pstm, item.getColumnKey(), dataItem.get(item.getRowKey()), item.getValue());
                }
                pstm.addBatch();
            }
            pstm.executeBatch();
            pstm.clearBatch();
            connection.commit();
        } catch (SQLException e) {
            DbEx.release(pstm);
            logger.error("SinkAbstract setData", e);
            System.out.println("SinkAbstract setData: " + e.getMessage());
        } finally {
            DbEx.release(pstm);
        }
        return true;
    }

    /**
     * 查询数据是否存在
     *
     * @param connection    链接
     * @param tableName     表名
     * @param data          数据
     * @param primaryColumn 主键
     * @param dbType        数据类型
     */
    public boolean exists(Connection connection, String tableName, Map<String, Object> data, Map<String, String> primaryColumn, DbTypeEnum dbType) {
        tableName = DbEx.convertName(tableName, dbType);
        int number = 0;
        ResultSet result = null;
        PreparedStatement pstm = null;
        try {
            String sql = "";
            StringBuilder whereSql = new StringBuilder(" WHERE ");

            for (Map.Entry<String, String> entry : primaryColumn.entrySet()) {
                whereSql.append(DbEx.convertName(entry.getKey(), dbType)).append("=?");
                whereSql.append(" AND ");
            }
            whereSql.delete(whereSql.length() - 5, whereSql.length());
            switch (dbType) {
                case MySql:
                    sql = String.format("select 1 as number from %s%s  limit  1 ", tableName, whereSql);
                    break;
                case MsSql:
                    sql = String.format("SELECT TOP 1 1 as number FROM %s%s", tableName, whereSql);
                    break;
                case PostgreSql:
                    sql = String.format("select 1 as number from %s%s  limit  1 ", tableName, whereSql);
                    break;
                case ClickHouse:
                    sql = String.format("SELECT TOP 1 1 as number FROM %s%s", tableName, whereSql);
                    break;
                default:
                    sql = String.format("SELECT TOP 1 1 as number FROM %s%s", tableName, whereSql);
                    break;
            }
            pstm = connection.prepareStatement(sql);
            int index = 1;
            for (Map.Entry<String, String> entry : primaryColumn.entrySet()) {
                String column = entry.getKey();
                TableInfo info = tableInfo.stream().filter(x -> x.getColumn_name().equals(column)).findFirst().get();
                DbEx.setParam(pstm, index, data.get(column), info.getJava_type());
                index++;
            }

            result = pstm.executeQuery();
            while (result.next()) {
                number = result.getInt("number");
            }

            if (number > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("hasData: " + e.getMessage());
        } finally {
            DbEx.release(pstm, result);
        }
        return false;
    }

    /**
     * 更新添加数和忽略数缓存
     *
     * @param ignoreCount 忽略数
     * @param addCount    添加数
     * @param updateCount 更新数
     */
    private void updateCountCache(Integer ignoreCount, Integer addCount, Integer updateCount) {
        //忽略数据缓存
        if (ignoreCount > 0) {
            Object cacheIgnoreCount = redisTemplate.opsForHash().get(cacheKey, "IGNORE_COUNT");
            if (cacheIgnoreCount != null) {
                ignoreCount += Integer.parseInt(cacheIgnoreCount.toString());
            }
            redisTemplate.opsForHash().put(cacheKey, "IGNORE_COUNT", ignoreCount + "");
        }

        //添加数据缓存
        if (addCount > 0) {
            Object cacheAddCount = redisTemplate.opsForHash().get(cacheKey, "ADD_COUNT");
            if (cacheAddCount != null) {
                addCount += Integer.parseInt(cacheAddCount.toString());
            }
            redisTemplate.opsForHash().put(cacheKey, "ADD_COUNT", addCount + "");
        }

        //更新数据缓存
        if (updateCount > 0) {
            Object cacheUpdateCount = redisTemplate.opsForHash().get(cacheKey, "UPDATE_COUNT");
            if (cacheUpdateCount != null) {
                updateCount += Integer.parseInt(cacheUpdateCount.toString());
            }
            redisTemplate.opsForHash().put(cacheKey, "UPDATE_COUNT", updateCount + "");
        }
    }

    /**
     * 设置状态
     *
     * @param e 状态
     */
    public void setStatus(TaskStatusEnum e) {
        redisTemplate.opsForHash().put(this.cacheKey, e.toString(), DateUtils.dateToStr(LocalDateTime.now()));
    }

    /**
     * 结束
     */
    @Override
    public void stop() {
        this.setStatus(TaskStatusEnum.COMPLETE);
    }
}
