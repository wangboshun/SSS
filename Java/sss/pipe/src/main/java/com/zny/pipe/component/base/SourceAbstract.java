package com.zny.pipe.component.base;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.gson.Gson;
import com.zny.common.enums.DbTypeEnum;
import com.zny.common.enums.RedisKeyEnum;
import com.zny.common.json.GsonEx;
import com.zny.common.utils.DateUtils;
import com.zny.common.utils.DbEx;
import com.zny.pipe.component.ConnectionFactory;
import com.zny.pipe.component.enums.TaskStatusEnum;
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

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
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
    public Integer rowCount;
    private final Logger logger = LoggerFactory.getLogger(getClass());

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
    public void config(SourceConfigModel sourceConfig, ConnectConfigModel connectConfig, TaskConfigModel taskConfig) {
        this.sourceConfig = sourceConfig;
        this.connectConfig = connectConfig;
        this.taskConfig = taskConfig;
        connection = ConnectionFactory.getConnection(connectConfig);
        if (connection != null) {
            this.sourceStatus = TaskStatusEnum.CREATE;
        } else {
            this.sourceStatus = TaskStatusEnum.CONNECT_FAIL;
        }
        setStatus();
    }

    /**
     * 开始
     */
    @Override
    public void start() {
        this.sourceStatus = TaskStatusEnum.RUNNING;
        setStatus();
        System.out.println("source start");
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
            queryCount(sql);
            pstm = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            pstm.setFetchSize(Integer.MIN_VALUE);
            result = pstm.executeQuery();
            List<Map<String, Object>> list = new ArrayList<>();
            List<String> filedList = DbEx.getField(result);
            int currentIndex = 0;  //数据记录号
            while (result.next()) {
                Map<String, Object> rowData = new HashMap<>(filedList.size());
                for (String x : filedList) {
                    rowData.put(x, result.getObject(x));
                }
                list.add(rowData);
                currentIndex++;
                if (list.size() >= 100) {
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
        model.setCount(rowCount);
        if (currentIndex == rowCount) {
            model.setStatus(TaskStatusEnum.COMPLETE.ordinal());
        } else {
            model.setStatus(this.sourceStatus.ordinal());
        }
        String json = gson.toJson(model);
        rabbitTemplate.convertAndSend(exchange, routingKey, json);
    }

    /**
     * 统计总条数
     *
     * @param sql 查询语句
     */
    private void queryCount(String sql) {
        Statement stmt = null;
        ResultSet result = null;
        try {
            int index = sql.indexOf("ORDER BY");
            sql = sql.substring(0, index);
            sql = sql.replace("*", "count(0)");
            stmt = connection.createStatement();
            result = stmt.executeQuery(sql);
            if (result.next()) {
                rowCount = result.getInt(1);
            }
        } catch (Exception e) {
            DbEx.release(connection, stmt, result);
            logger.error("SourceAbstract getCount", e);
            System.out.println("SourceAbstract getCount: " + e.getMessage());
        } finally {
            DbEx.release(stmt, result);
        }
    }

    /**
     * 获取需要查询的SQL
     */
    private String getNextSql() {
        String sql = "";
        try {
            sql = "SELECT * FROM " + sourceConfig.getTable_name() + " WHERE 1=1 ";

            //如果是增量
            if (taskConfig.getAdd_type() == 0) {
                String startTime = getStartTime();
                String endTime = getEndTime(startTime);

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
     * 获取开始时间
     */
    private String getStartTime() {
        Object cache = redisTemplate.opsForHash().get(RedisKeyEnum.PIPE_TIME_CACHE.toString(), taskConfig.getId());
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
        redisTemplate.opsForHash().put(RedisKeyEnum.PIPE_TIME_CACHE.toString(), taskConfig.getId(), startTime);
    }

    @Override
    public void stop() {
        setNextTime();
        this.sourceStatus = TaskStatusEnum.COMPLETE;
        setStatus();
    }

    /**
     * 设置状态
     */
    private void setStatus() {
        redisTemplate.opsForHash().put(RedisKeyEnum.SOURCE_STATUS_CACHE.toString(), taskConfig.getId(), this.sourceStatus.toString());
    }
}
