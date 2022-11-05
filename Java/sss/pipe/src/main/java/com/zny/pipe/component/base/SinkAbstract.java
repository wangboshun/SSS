package com.zny.pipe.component.base;

import com.google.gson.Gson;
import com.zny.common.enums.DbTypeEnum;
import com.zny.common.enums.InsertTypeEnum;
import com.zny.common.json.GsonEx;
import com.zny.common.utils.DbEx;
import com.zny.pipe.component.ConnectionFactory;
import com.zny.pipe.component.enums.TaskStatusEnum;
import com.zny.pipe.model.ConnectConfigModel;
import com.zny.pipe.model.SinkConfigModel;
import com.zny.pipe.model.TaskConfigModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
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

    public TaskStatusEnum sinkStatus;

    @Override
    public void config(SinkConfigModel sinkConfig, ConnectConfigModel connectConfig, TaskConfigModel taskConfig) {
        this.sinkConfig = sinkConfig;
        this.connectConfig = connectConfig;
        this.taskConfig = taskConfig;
        connection = ConnectionFactory.getConnection(connectConfig);
        this.sinkStatus = TaskStatusEnum.CREATE;
    }

    @Override
    public void start() {
        System.out.println("sink start");
        sinkStatus = TaskStatusEnum.RUNNING;
    }

    @Override
    public void stop() {
        sinkStatus = TaskStatusEnum.COMPLETE;
    }

    @Override
    public TaskStatusEnum getStatus() {
        return sinkStatus;
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
            logger.error("SinkAbstract checkConnection", e);
            System.out.println("SinkAbstract checkConnection " + e.getMessage());
        }
    }

    /**
     * 保存数据
     *
     * @param message 数据消息
     */
    public void setData(String message) {
        this.checkConnection();
        Gson gson = GsonEx.getInstance();
        List<Map<String, Object>> list = new ArrayList<>();
        list = gson.fromJson(message, list.getClass());
        PreparedStatement pstm = null;
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
            }
            pstm.executeBatch();
            pstm.clearBatch();
            connection.commit();
        } catch (SQLException e) {
            DbEx.release(connection, pstm);
            logger.error("SinkAbstract setData", e);
            System.out.println("SinkAbstract setData: " + e.getMessage());
        } finally {
            DbEx.release(pstm);
        }
    }
}
