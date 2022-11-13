package com.zny.pipe.component.base;

import com.nhl.dflib.DataFrame;
import com.zny.common.enums.DbTypeEnum;
import com.zny.common.enums.InsertTypeEnum;
import com.zny.common.enums.RedisKeyEnum;
import com.zny.common.utils.DateUtils;
import com.zny.common.utils.DbEx;
import com.zny.pipe.component.ConnectionFactory;
import com.zny.pipe.component.enums.TaskStatusEnum;
import com.zny.pipe.component.filter.FilterBase;
import com.zny.pipe.component.transform.TransformBase;
import com.zny.pipe.model.ConnectConfigModel;
import com.zny.pipe.model.SinkConfigModel;
import com.zny.pipe.model.TaskConfigModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

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
    private String cacheKey;
    private DbTypeEnum dbType;
    private String tableName;

    private FilterBase filter;
    private TransformBase transform;

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
    public void config(SinkConfigModel sinkConfig, ConnectConfigModel connectConfig, TaskConfigModel taskConfig, Integer version, FilterBase filter, TransformBase transform) {
        this.sinkConfig = sinkConfig;
        this.connectConfig = connectConfig;
        this.taskConfig = taskConfig;
        this.filter = filter;
        this.transform = transform;
        this.cacheKey = RedisKeyEnum.SINK_TIME_CACHE + ":" + taskConfig.getId() + ":" + version;
        connection = ConnectionFactory.getConnection(connectConfig);
        dbType = DbTypeEnum.values()[this.connectConfig.getDb_type()];
        tableName = this.sinkConfig.getTable_name();
    }

    /**
     * 开始
     *
     * @param list 数据消息
     */
    @Override
    public void start(List<Map<String, Object>> list) {
        System.out.println("SinkAbstract start");
        setData(list);
    }

    /**
     * 保存数据
     *
     * @param list 数据消息
     */
    public void setData(List<Map<String, Object>> list) {
        //筛选
        list = filter.filter(list);

        //转换
        list = transform.convert(list);

        List<Map<String, Object>> ignoreList = new ArrayList<>();
        List<Map<String, Object>> addList = new ArrayList<>();
        List<Map<String, Object>> updateList = new ArrayList<>();
        try {
            String[] primaryField = this.sinkConfig.getPrimary_field().split(",");
            InsertTypeEnum insertType = InsertTypeEnum.values()[this.taskConfig.getInsert_type()];
            for (Map<String, Object> item : list) {
                //数据是否已存在
                boolean hasData = DbEx.hasData(connection, tableName, item, primaryField, dbType);
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
                updateData(updateList, Arrays.asList(primaryField));
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
            Set<String> fieldSet = list.get(0).keySet();
            StringBuilder fieldSql = new StringBuilder();
            StringBuilder valueSql = new StringBuilder();

            for (String field : fieldSet) {
                switch (dbType) {
                    case MySQL:
                        fieldSql.append("`").append(field).append("`,");
                        break;
                    case MsSQL:
                        fieldSql.append("[").append(field).append("],");
                        break;
                    default:
                        break;
                }
                valueSql.append("?,");
            }

            fieldSql.deleteCharAt(fieldSql.length() - 1);
            valueSql.deleteCharAt(valueSql.length() - 1);
            String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, fieldSql, valueSql);
            this.connection.setAutoCommit(false);
            pstm = connection.prepareStatement(sql);
            for (Map<String, Object> item : list) {
                int index = 1;
                for (String field : fieldSet) {
                    pstm.setObject(index, item.get(field));
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
     * @param list         数据集
     * @param primaryField 主键字段
     */
    private Boolean updateData(List<Map<String, Object>> list, List<String> primaryField) {
        PreparedStatement pstm = null;
        try {
            Set<String> fieldSet = list.get(0).keySet();
            StringBuilder fieldSql = new StringBuilder();
            StringBuilder whereSql = new StringBuilder();

            for (String field : fieldSet) {

                //主键
                if (primaryField.contains(field)) {
                    switch (dbType) {
                        case MySQL:
                            whereSql.append("`").append(field).append("`=? AND ");
                            break;
                        case MsSQL:
                            whereSql.append("[").append(field).append("]=? AND ");
                            break;
                        default:
                            break;
                    }

                }
                //非主键
                else {
                    switch (dbType) {
                        case MySQL:
                            fieldSql.append("`").append(field).append("`=?,");
                            break;
                        case MsSQL:
                            fieldSql.append("[").append(field).append("]=?,");
                            break;
                        default:
                            break;
                    }
                }
            }

            fieldSql.deleteCharAt(fieldSql.length() - 1);
            whereSql.delete(whereSql.length() - 4, whereSql.length());
            String sql = String.format("UPDATE %s SET %s WHERE %s", tableName, fieldSql, whereSql);
            this.connection.setAutoCommit(false);
            pstm = connection.prepareStatement(sql);
            for (Map<String, Object> item : list) {
                int index = 1;

                //这里设置数据下标有讲究，因为拼sql的时候非主键set数据在前，所以需要先设置非主键的数据，然后设置主键的数据
                for (String field : fieldSet) {
                    //非主键
                    if (!primaryField.contains(field)) {
                        pstm.setObject(index, item.get(field));
                        index++;
                    }
                }
                for (String field : fieldSet) {
                    //主键
                    if (primaryField.contains(field)) {
                        pstm.setObject(index, item.get(field));
                        index++;
                    }
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
