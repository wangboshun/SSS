package com.zny.pipe.component.sink;

import com.google.gson.Gson;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.zny.common.json.GsonEx;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.sql.Connection;
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
public class MySqlSink implements SinkBase {

    private Connection connection;

    /**
     * 配置数据源
     */
    private void configDataSource() {
        String connectStr = "jdbc:mysql://127.0.0.1:3306/test1?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&rewriteBatchedStatements=true";
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setURL(connectStr);
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("123456");
        try {
            connection = mysqlDataSource.getConnection();
        } catch (SQLException e) {
            System.out.println("Exception:" + e.getMessage());
        }
    }

    @RabbitHandler
    public void onMessage(String message) {
        System.out.println("MySql_Queue --->接收消息:\r\n" + message);
        Gson gson = GsonEx.getInstance();
        List<Map<String, Object>> list = new ArrayList<>();
        list = gson.fromJson(message, list.getClass());
        setData(list);
    }

    @Override
    public void start() {
        try {
            configDataSource();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    @Override
    public void stop() {
        try {

        } catch (Exception e) {

        }
    }

    private void setData(List<Map<String, Object>> list) {
        try {
            connection.setAutoCommit(false);
            Set<String> fieldSet = list.get(0).keySet();
            String fieldSql = "";
            String valueSql = "";

            for (String field : fieldSet) {
                fieldSql += "`" + field + "`,";
                valueSql += "?,";
            }

            fieldSql = fieldSql.substring(0, fieldSql.length() - 1);
            valueSql = valueSql.substring(0, valueSql.length() - 1);
            String sql = "INSERT INTO test2 (" + fieldSql + ") VALUES (" + valueSql + ")";
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
        }
    }
}
