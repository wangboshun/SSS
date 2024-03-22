package com.zny.pipe.component.base;

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
    public boolean config(SinkConfigModel sinkConfig, ConnectConfigModel connectConfig, TaskConfigModel taskConfig, List<ColumnConfigModel> columnList, Integer version) {
        this.sinkConfig = sinkConfig;
        this.connectConfig = connectConfig;
        this.taskConfig = taskConfig;
        this.cacheKey = RedisKeyEnum.SINK_TIME_CACHE + ":" + taskConfig.getId() + ":" + version;
        try {
            connection = ConnectionFactory.getConnection(connectConfig);
            if (connection != null) {
                dbType = DbTypeEnum.values()[this.connectConfig.getDb_type()];
                tableName = this.sinkConfig.getTable_name();
                this.columnList = columnList;
                try {
                    //如果获取表结构抛出异常，例如表不存在
                    tableInfo = DbEx.getTableInfo(connection, tableName);
                    return true;
                } catch (SQLException e) {
                    setStatus(TaskStatusEnum.CONNECT_FAIL);
                    logger.error("getTableInfo exception", e);
                    return false;
                }
            } else {
                setStatus(TaskStatusEnum.CONNECT_FAIL);
                return false;
            }
        } catch (SQLException e) {
            logger.error("SinkAbstract config", e);
            return false;
        }
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
        Map<String, String> map = new HashMap<>(columnList.size());
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
     * 拆分数据，分为添加数据、忽略数据、更新数据，并按照相应的方式进行处理
     *
     * @param list 数据消息
     */
    public void splitData(List<Map<String, Object>> list) {
        List<Map<String, Object>> ignoreList = new ArrayList<>();
        List<Map<String, Object>> addList = new ArrayList<>();
        List<Map<String, Object>> updateList = new ArrayList<>();
        Map<String, String> primaryColumnMap = getPrimaryKey();
        try {
            InsertTypeEnum insertType = InsertTypeEnum.values()[this.taskConfig.getInsert_type()];
            String countSql = "SELECT COUNT(0) FROM " + DbEx.convertName(tableName, dbType);
            int rowCount = DbEx.getCount(connection, countSql);
            boolean hasData = false;
            for (Map<String, Object> item : list) {
                //如果sink表里面的数据大于0，才进行是否存在判断
                if (rowCount > 0) {
                    hasData = exists(item, connection, tableName, primaryColumnMap, dbType);
                }
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
            logger.error("SinkAbstract splitData", e);
            System.out.println("SinkAbstract splitData: " + e.getMessage());
        } finally {
            try {
                DbEx.release(connection);
            } catch (SQLException e) {
                logger.error("release ", e);
            }
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
            LinkedHashMap<String, String> columnMap = new LinkedHashMap<>();
            for (ColumnConfigModel item : columnList) {
                //找到对应的列信息
                TableInfo info = tableInfo.stream().filter(x -> x.getColumn_name().equals(item.getSink_column())).findFirst().get();
                columnSql.append(DbEx.convertName(item.getSink_column(), dbType)).append(",");
                valueSql.append("?,");
                columnMap.put(item.getSink_column(), info.getJava_type());
            }
            columnSql.deleteCharAt(columnSql.length() - 1);
            valueSql.deleteCharAt(valueSql.length() - 1);
            String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", DbEx.convertName(tableName, dbType), columnSql, valueSql);
            this.connection.setAutoCommit(false);
            pstm = connection.prepareStatement(sql);
            for (Map<String, Object> dataItem : list) {
                int index = 1;
                for (Map.Entry<String, String> entry : columnMap.entrySet()) {
                    DbEx.setParam(pstm, index, dataItem.get(entry.getKey()), entry.getValue());
                    index++;
                }
                pstm.addBatch();
            }
            pstm.executeBatch();
            pstm.clearBatch();
            connection.commit();
        } catch (SQLException e) {
            try {
                DbEx.release(pstm);
            } catch (SQLException ex) {
                logger.error("release ", e);
            }
            logger.error("SinkAbstract addData", e);
        } finally {
            try {
                DbEx.release(pstm);
            } catch (SQLException e) {
                logger.error("release ", e);
            }
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
            //定义两个有序map，为后面的参数赋值做依据
            LinkedHashMap<String, String> primaryColumnMap = new LinkedHashMap<>(); //主键列
            LinkedHashMap<String, String> notPrimaryColumnMap = new LinkedHashMap<>();  //非主键列
            for (ColumnConfigModel item : columnList) {
                //找到对应的列信息
                String columnName = item.getSink_column();
                TableInfo info = tableInfo.stream().filter(x -> x.getColumn_name().equals(columnName)).findFirst().get();
                String javaType = info.getJava_type();
                //主键
                if (info.getIs_primary() > 0) {
                    whereSql.append(DbEx.convertName(columnName, dbType)).append("=?");
                    whereSql.append(" AND ");
                    primaryColumnMap.put(columnName, javaType);
                }
                //非主键
                else {
                    columnSql.append(DbEx.convertName(columnName, dbType)).append("=?");
                    columnSql.append(",");
                    notPrimaryColumnMap.put(columnName, javaType);
                }
            }
            columnSql.deleteCharAt(columnSql.length() - 1);
            whereSql.delete(whereSql.length() - 4, whereSql.length());
            String sql = String.format("UPDATE %s SET %s WHERE %s", DbEx.convertName(tableName, dbType), columnSql, whereSql);
            this.connection.setAutoCommit(false);
            pstm = connection.prepareStatement(sql);
            for (Map<String, Object> dataItem : list) {
                int index = 1;
                //这里做了特殊处理，因为sql语句中非主键的参数在前面，所以先把非主键和参数先封装进去
                for (Map.Entry<String, String> entry : notPrimaryColumnMap.entrySet()) {
                    DbEx.setParam(pstm, index, dataItem.get(entry.getKey()), entry.getValue());
                    index++;
                }
                for (Map.Entry<String, String> entry : primaryColumnMap.entrySet()) {
                    DbEx.setParam(pstm, index, dataItem.get(entry.getKey()), entry.getValue());
                    index++;
                }
                pstm.addBatch();
            }
            pstm.executeBatch();
            pstm.clearBatch();
            connection.commit();
        } catch (SQLException e) {
            try {
                DbEx.release(pstm);
            } catch (SQLException ex) {
                logger.error("release ", e);
            }
            logger.error("SinkAbstract updateData", e);
        } finally {
            try {
                DbEx.release(pstm);
            } catch (SQLException e) {
                logger.error("release ", e);
            }
        }
        return true;
    }

    /**
     * 查询数据是否存在
     *
     * @param data             数据
     * @param connection       链接
     * @param tableName        表名
     * @param primaryColumnMap 主键
     * @param dbType           数据库类型
     */
    public boolean exists(Map<String, Object> data, Connection connection, String tableName, Map<String, String> primaryColumnMap, DbTypeEnum dbType) {
        tableName = DbEx.convertName(tableName, dbType);
        int number = 0;
        ResultSet result = null;
        PreparedStatement pstm = null;
        try {
            String sql = "";
            StringBuilder whereSql = new StringBuilder(" WHERE ");

            for (Map.Entry<String, String> entry : primaryColumnMap.entrySet()) {
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
            for (Map.Entry<String, String> entry : primaryColumnMap.entrySet()) {
                DbEx.setParam(pstm, index, data.get(entry.getKey()), entry.getValue());
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
            logger.error("exist ", e);
        } finally {
            try {
                DbEx.release(pstm, result);
            } catch (SQLException e) {
                logger.error("release ", e);
            }
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
