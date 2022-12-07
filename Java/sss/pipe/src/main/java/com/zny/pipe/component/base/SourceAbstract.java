package com.zny.pipe.component.base;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.gson.Gson;
import com.zny.common.enums.DbTypeEnum;
import com.zny.common.enums.RedisKeyEnum;
import com.zny.common.json.GsonEx;
import com.zny.common.utils.DateUtils;
import com.zny.common.utils.database.DbEx;
import com.zny.pipe.component.ConnectionFactory;
import com.zny.pipe.component.base.enums.TaskStatusEnum;
import com.zny.pipe.component.base.interfaces.SourceBase;
import com.zny.pipe.model.ConnectConfigModel;
import com.zny.pipe.model.MessageBodyModel;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

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
    public int rowCount;
    private String cacheKey;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final int BATCH_SIZE = 1000;
    public Map<String, String> sourceTime = new HashMap<>();
    private DbTypeEnum dbType;
    public int version;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 配置
     *
     * @param sourceConfig  数据源信息
     * @param connectConfig 链接信息
     * @param taskConfig    任务信息
     */
    @Override
    public void config(SourceConfigModel sourceConfig, ConnectConfigModel connectConfig, TaskConfigModel taskConfig, int version) {
        this.sourceConfig = sourceConfig;
        this.connectConfig = connectConfig;
        this.taskConfig = taskConfig;
        this.version = version;
        connection = ConnectionFactory.getConnection(connectConfig);
        this.cacheKey = RedisKeyEnum.SOURCE_TIME_CACHE + ":" + taskConfig.getId() + ":" + version;
        dbType = DbTypeEnum.values()[this.connectConfig.getDb_type()];
        if (connection != null) {
            setStatus(TaskStatusEnum.CREATE);
        }
    }

    /**
     * 开始
     */
    @Override
    public void start() {
        setStatus(TaskStatusEnum.RUNNING);
        System.out.println("SourceAbstract start");
        getData();
    }

    /**
     * 获取数据
     */
    private void getData() {
        PreparedStatement pstm = null;
        ResultSet result = null;
        try {
            String sql = getNextSql();
            rowCount = DbEx.getCount(connection, sql);
            redisTemplate.opsForHash().put(this.cacheKey, "ROW_COUNT", rowCount + "");
            pstm = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            pstm.setFetchSize(Integer.MIN_VALUE);
            result = pstm.executeQuery();
            List<Map<String, Object>> list = new ArrayList<>();
            Set<String> columnNameSet = DbEx.getColumnName(result).keySet();
            int currentIndex = 0;  //数据记录号
            while (result.next()) {
                Map<String, Object> rowData = new HashMap<>(columnNameSet.size());
                for (String x : columnNameSet) {
                    rowData.put(x, result.getObject(x));
                }
                list.add(rowData);
                currentIndex++;
                if (list.size() >= BATCH_SIZE) {
                    sendData(list, currentIndex);
                    list.clear();
                }
            }
            if (!list.isEmpty()) {
                sendData(list, currentIndex);
                list.clear();
            }
        } catch (SQLException e) {
            DbEx.release(connection, pstm, result);
            logger.error("SourceAbstract getData", e);
            System.out.println("SourceAbstract getData: " + e.getMessage());
        } finally {
            DbEx.release(pstm, result);
        }
        this.stop();
    }

    /**
     * 发送数据
     */
    public void sendData(List<Map<String, Object>> list, int currentIndex) {
        String exchange = "Pipe_Exchange";
        String routingKey = (DbTypeEnum.values()[taskConfig.getSink_type()]).toString() + "_RoutKey";
        Gson gson = GsonEx.getInstance();
        MessageBodyModel model = new MessageBodyModel();
        model.setTaskId(this.taskConfig.getId());
        model.setData(list);
        model.setBatch_size(BATCH_SIZE);
        model.setTotal(rowCount);
        model.setCurrent(currentIndex);
        model.setVersion(version);
        //如果数量相等或小于批次，设置为已完成
        if (currentIndex == rowCount || currentIndex < BATCH_SIZE) {
            model.setStatus(TaskStatusEnum.COMPLETE.ordinal());
        } else {
            model.setStatus(TaskStatusEnum.RUNNING.ordinal());
        }
        String json = gson.toJson(model);
        rabbitTemplate.convertAndSend(exchange, routingKey, json);
    }

    /**
     * 获取需要查询的SQL
     */
    private String getNextSql() {
        String sql = "";
        try {
            sql = "SELECT * FROM " + DbEx.convertName(sourceConfig.getTable_name(), dbType) + " WHERE 1=1 ";
            //如果是增量
            if (taskConfig.getAdd_type() == 0) {
                String startTime = getStartTime();
                String endTime = getEndTime(startTime);
                String pg_time;
                if (dbType == DbTypeEnum.PostgreSQL) {
                    pg_time = "::TIMESTAMP";
                }

                //TODO 未完成pgsql的时间处理

                //按wrtm获取
                if (sourceConfig.getGet_type() == 0) {
                    sql += " AND " + DbEx.convertName(sourceConfig.getWrtm_column(), dbType) + ">='" + startTime + "' AND " + DbEx.convertName(sourceConfig.getWrtm_column(), dbType) + "<='" + endTime + "' ";
                }
                //按数据时间获取
                else if (sourceConfig.getGet_type() == 1) {
                    sql += " AND " + DbEx.convertName(sourceConfig.getTime_column(), dbType) + ">='" + startTime + "' AND " + DbEx.convertName(sourceConfig.getTime_column(), dbType) + "<='" + endTime + "' ";
                }
            }

            //where条件
            if (StringUtils.isNotBlank(taskConfig.getWhere_param())) {
                sql += taskConfig.getWhere_param();
            }

            //如果排序字段不为空
            if (StringUtils.isNotBlank(sourceConfig.getOrder_column())) {
                sql += " ORDER BY " + DbEx.convertName(sourceConfig.getOrder_column(), dbType);
            }
            //如果排序字段为空
            else {
                //如果wrtm字段不为空，设置wrtm为排序字段
                if (StringUtils.isNotBlank(sourceConfig.getWrtm_column())) {
                    sql += " ORDER BY " + DbEx.convertName(sourceConfig.getWrtm_column(), dbType);
                }
                //否则设置数据时间字段为排序字段
                else {
                    sql += " ORDER BY " + DbEx.convertName(sourceConfig.getTime_column(), dbType);
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
            logger.error("SourceAbstract getNextSql", e);
            System.out.println("SourceAbstract getNextSql: " + e.getMessage());
        }
        return sql;
    }

    /**
     * 获取开始时间
     */
    private String getStartTime() {
        Object cache = redisTemplate.opsForHash().get(RedisKeyEnum.TASK_TIME_CACHE.toString(), taskConfig.getId());
        //如果没有缓存时间
        if (cache != null) {
            return cache.toString();
        } else {
            return taskConfig.getStart_time();
        }
    }

    /**
     * 获取结束时间
     *
     * @param startTime 开始时间
     */
    private String getEndTime(String startTime) {
        //如果配置了结束时间
        if (StringUtils.isNotBlank(taskConfig.getEnd_time())) {
            return taskConfig.getEnd_time();
        } else {
            return DateUtils.dateToStr(DateUtils.strToDate(startTime).plusMinutes(taskConfig.getTime_step().longValue()));
        }
    }

    /**
     * 设置下次开始时间
     */
    private void setNextTime() {
        String startTime = getStartTime();
        startTime = DateUtils.dateToStr(DateUtils.strToDate(startTime).plusMinutes(taskConfig.getTime_step().longValue()));
        redisTemplate.opsForHash().put(RedisKeyEnum.TASK_TIME_CACHE.toString(), taskConfig.getId(), startTime);
    }

    @Override
    public void stop() {
        setNextTime();
        setStatus(TaskStatusEnum.COMPLETE);
    }

    /**
     * 设置状态
     */
    public void setStatus(TaskStatusEnum e) {
        redisTemplate.opsForHash().put(this.cacheKey, e.toString(), DateUtils.dateToStr(LocalDateTime.now()));
    }
}
