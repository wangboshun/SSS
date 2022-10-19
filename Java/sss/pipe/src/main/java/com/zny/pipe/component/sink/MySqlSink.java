package com.zny.pipe.component.sink;

import com.google.gson.Gson;
import com.zny.common.json.GsonEx;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author WBS
 * Date:2022/10/12
 * mysql目的服务类
 */

@Component
@RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "MySql_Queue", durable = "true"), exchange = @Exchange(value = "Pipe_Exchange"), key = "MySql_RoutKey")})
public class MySqlSink extends SinkAbstract {

    @RabbitHandler
    public void onMessage(String message) {
        System.out.println("MySql_Queue --->接收消息:\r\n" + message);
        Gson gson = GsonEx.getInstance();
        List<Map<String, Object>> list = new ArrayList<>();
        list = gson.fromJson(message, list.getClass());
        setData(list);
    }

    private void setData(List<Map<String, Object>> list) {
        try {
            this.connection.setAutoCommit(false);
            Set<String> fieldSet = list.get(0).keySet();
            String fieldSql = "";
            String valueSql = "";

            for (String field : fieldSet) {
                fieldSql += "`" + field + "`,";
                valueSql += "?,";
            }

            fieldSql = fieldSql.substring(0, fieldSql.length() - 1);
            valueSql = valueSql.substring(0, valueSql.length() - 1);
            String sql = "INSERT INTO " + this.sinkConfig.getTable_name() + " (" + fieldSql + ") VALUES (" + valueSql + ")";

            //存在即跳过
            if (taskConfig.getInsert_type() == 0) {
                sql = sql.replace("INSERT IGNORE INTO ", "");
            }

            PreparedStatement pstm = connection.prepareStatement(sql);

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
            System.out.println(e.getMessage());
        } finally {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {

            }
        }
    }
}
