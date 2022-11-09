package com.zny.pipe.component.base;

import com.zny.common.enums.DbTypeEnum;
import com.zny.common.enums.InsertTypeEnum;
import com.zny.common.enums.RedisKeyEnum;
import com.zny.common.utils.DateUtils;
import com.zny.common.utils.DbEx;
import com.zny.pipe.component.ConnectionFactory;
import com.zny.pipe.component.enums.TaskStatusEnum;
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
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public void config(SinkConfigModel sinkConfig, ConnectConfigModel connectConfig, TaskConfigModel taskConfig, Integer version) {
        this.sinkConfig = sinkConfig;
        this.connectConfig = connectConfig;
        this.taskConfig = taskConfig;
        this.cacheKey = RedisKeyEnum.SINK_TIME_CACHE + ":" + taskConfig.getId() + ":" + version;
        connection = ConnectionFactory.getConnection(connectConfig);
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
        PreparedStatement pstm = null;
        Integer addCount = 0;
        Integer ignoreCount = 0;
        try {
            String[] primaryField = this.sinkConfig.getPrimary_field().split(",");
            String tableName = this.sinkConfig.getTable_name();
            DbTypeEnum dbType = DbTypeEnum.values()[this.connectConfig.getDb_type()];
            InsertTypeEnum insertType = InsertTypeEnum.values()[this.taskConfig.getInsert_type()];

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
                switch (insertType) {
                    case IGNORE:
                        //数据是否已存在
                        if (DbEx.hasData(connection, tableName, item, primaryField, dbType)) {
                            ignoreCount++;
                            continue;
                        }
                        break;
                    case UPDATE:

                        break;
                    default:
                        break;
                }

                int index = 1;
                for (String field : fieldSet) {
                    pstm.setObject(index, item.get(field));
                    index++;
                }
                pstm.addBatch();
                addCount++;
            }
            pstm.executeBatch();
            pstm.clearBatch();
            connection.commit();
        } catch (SQLException e) {
            DbEx.release(connection, pstm);
            logger.error("SinkAbstract setData", e);
            System.out.println("SinkAbstract setData: " + e.getMessage());
        } finally {
            DbEx.release(connection, pstm);
        }
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
