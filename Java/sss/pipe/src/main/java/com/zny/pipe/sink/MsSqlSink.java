package com.zny.pipe.sink;

import com.google.gson.Gson;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.zny.common.json.GsonEx;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/10/12
 * mssql目的服务类
 */

@Component
@RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "MsSql_Queue", durable = "true"), exchange = @Exchange(value = "Pipe_Exchange"), key = "MsSql_RoutKey")})
public class MsSqlSink implements SinkBase {

    private Connection connection;

    /**
     * 配置数据源
     */
    private void configDataSource() {
        String connectStr = "jdbc:sqlserver://127.0.0.1:1433;database=test1;integratedSecurity=false;encrypt=true;trustServerCertificate=true";
        SQLServerDataSource sqlServerDataSource = new SQLServerDataSource();
        sqlServerDataSource.setURL(connectStr);
        sqlServerDataSource.setUser("sa");
        sqlServerDataSource.setPassword("123456");
        try {
            connection = sqlServerDataSource.getConnection();
        } catch (SQLException e) {
            System.out.println("Exception:" + e.getMessage());
        }
    }

    @RabbitHandler
    public void onMessage(String message) {
        System.out.println("MsSql_Queue --->接收消息:\r\n" + message);
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

    }
}
