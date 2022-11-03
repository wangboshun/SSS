package com.zny.pipe.component.base;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.gson.Gson;
import com.zny.common.enums.DbTypeEnum;
import com.zny.common.enums.RedisKeyEnum;
import com.zny.common.json.GsonEx;
import com.zny.common.utils.DateUtils;
import com.zny.pipe.component.ConnectionFactory;
import com.zny.pipe.component.enums.TaskStatusEnum;
import com.zny.pipe.model.ConnectConfigModel;
import com.zny.pipe.model.SourceConfigModel;
import com.zny.pipe.model.TaskConfigModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/10/19
 * Source抽象基类
 */
@Component
public class SourceAbstract implements SourceBase {

    public SourceConfigModel sourceConfig;
    public ConnectConfigModel connectConfig;
    public TaskConfigModel taskConfig;
    public Connection connection;
    public TaskStatusEnum sourceStatus;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void config(SourceConfigModel sourceConfig, ConnectConfigModel connectConfig, TaskConfigModel taskConfig) {
        this.sourceConfig = sourceConfig;
        this.connectConfig = connectConfig;
        this.taskConfig = taskConfig;
        connection = ConnectionFactory.getConnection(connectConfig);
        this.sourceStatus = TaskStatusEnum.Create;
    }

    /**
     * 检查数据库链接
     */
    public void checkConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = ConnectionFactory.getConnection(connectConfig);
            }
        } catch (Exception e) {
            logger.error("SourceAbstract checkConnection", e);
            System.out.println("SourceAbstract checkConnection " + e.getMessage());
        }
    }

    /**
     * 发送数据
     */
    public void sendData(List<Map<String, Object>> list) {
        String exchange = "Pipe_Exchange";
        String routingKey = (DbTypeEnum.values()[taskConfig.getSink_type()]).toString() + "_RoutKey";
        Gson gson = GsonEx.getInstance();
        String json = gson.toJson(list);
        rabbitTemplate.convertAndSend(exchange, routingKey, json);
    }

    @Override
    public void start() {
        this.sourceStatus = TaskStatusEnum.Running;
        System.out.println("source start");
    }

    @Override
    public void stop() {
        this.sourceStatus = TaskStatusEnum.Complete;
    }

    /**
     * 获取需要查询的SQL
     */
    public String getNextSql() {
        String sql = "";
        try {
            sql = "SELECT * FROM " + sourceConfig.getTable_name() + " WHERE 1=1 ";

            //如果是增量
            if (taskConfig.getAdd_type() == 0) {
                String startTime = "";
                String endTime = "";
                Object cache = redisTemplate.opsForHash().get(RedisKeyEnum.PipeTimeCache.toString(), taskConfig.getId());
                //如果没有缓存时间
                if (cache != null) {
                    startTime = cache.toString();
                } else {
                    startTime = taskConfig.getStart_time();
                }

                //如果配置了结束时间
                if (StringUtils.isNotBlank(taskConfig.getEnd_time())) {
                    endTime = taskConfig.getEnd_time();
                } else {
                    endTime = DateUtils.dateToStr(DateUtils.strToDate(startTime).plusMinutes(taskConfig.getTime_step().longValue()));
                }

                //按wrtm获取
                if (sourceConfig.getGet_type() == 0) {
                    sql += " AND " + sourceConfig.getWrtm_field() + ">='" + startTime + "' AND " + sourceConfig.getWrtm_field() + "<='" + endTime + "' ";
                }
                //按数据时间获取
                else if (sourceConfig.getGet_type() == 1) {
                    sql += " AND " + sourceConfig.getTime_field() + ">='" + startTime + "' AND " + sourceConfig.getTime_field() + "<='" + endTime + "' ";
                }
            }

            //where条件
            if (StringUtils.isNotBlank(taskConfig.getWhere_param())) {
                sql += taskConfig.getWhere_param();
            }

            //如果排序字段不为空
            if (StringUtils.isNotBlank(sourceConfig.getOrder_field())) {
                sql += " ORDER BY " + sourceConfig.getOrder_field();
            }
            //如果排序字段为空
            else {
                //如果wrtm字段不为空，设置wrtm为排序字段
                if (StringUtils.isNotBlank(sourceConfig.getWrtm_field())) {
                    sql += " ORDER BY " + sourceConfig.getWrtm_field();
                }
                //否则设置数据时间字段为排序字段
                else {
                    sql += " ORDER BY " + sourceConfig.getTime_field();
                }
            }

            //如果排序方式不为空
            if (sourceConfig.getOrder_type() != null) {
                //如果是顺序
                if (sourceConfig.getOrder_type() == 0) {
                    sql += " ASC ";
                }
                //如果是倒序
                else if (sourceConfig.getOrder_type() == 1) {
                    sql += " DESC ";
                }
            }
            //后置设置为倒序
            else {
                sql += " DESC ";
            }
        } catch (Exception e) {
            logger.error("source getNextSql", e);
            System.out.println("source getNextSql: " + e.getMessage());
        }
        return sql;
    }

    /**
     * 释放资源
     *
     * @param connection 数据库链接
     * @param pstm       声明
     */
    public void release(Connection connection, PreparedStatement pstm) {
        try {
            if (pstm != null) {
                pstm.close();
            }
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.error("SourceAbstract release", e);
            System.out.println("SourceAbstract release: " + e.getMessage());
        }
    }

    /**
     * 释放资源
     *
     * @param pstm 声明
     */
    public void release(PreparedStatement pstm) {
        try {
            if (pstm != null) {
                pstm.close();
            }
        } catch (SQLException e) {
            logger.error("SourceAbstract release", e);
            System.out.println("SourceAbstract release: " + e.getMessage());
        }
    }
}
