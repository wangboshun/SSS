package com.zny.pipe.source;

import com.google.gson.Gson;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.zny.common.json.GsonEx;
import com.zny.common.utils.DbEx;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/10/12
 * mysql源端服务类
 */

@Component
public class MySqlSource implements SourceBase {
    private MysqlDataSource mysqlDataSource;
    private Connection connection;

    private final RabbitTemplate rabbitTemplate;

    public MySqlSource(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    private void configDataSource() {
        String connectStr = "jdbc:mysql://127.0.0.1:3306/test1?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setURL(connectStr);
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("123456");
        try {
            connection = mysqlDataSource.getConnection();
        } catch (SQLException e) {
            System.out.println("Exception:" + e.getMessage());
        }
    }

    @Override
    public void start() {
        try {
            configDataSource();
            getData();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    @Override
    public void stop() {
        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 发送数据
     *
     * @param list 数据
     */
    private void sendData(List<Map<String, Object>> list) {
        Gson gson = GsonEx.getInstance();
        String json = gson.toJson(list);
        rabbitTemplate.convertAndSend("Pipe_Exchange", "MySql_RoutKey", json);
    }

    /**
     * 获取数据
     */
    private void getData() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery("select * from Test1 limit 100");
            List<Map<String, Object>> list = new ArrayList<>();
            List<String> filedList = DbEx.getField(result);
            while (result.next()) {
                Map<String, Object> rowData = new HashMap<>(filedList.size());
                for (String x : filedList) {
                    rowData.put(x, result.getObject(x));
                }
                list.add(rowData);
                if (list.size() >= 10) {
                    List<Map<String, Object>> tmp = new ArrayList<>(list);
                    list.clear();
                    sendData(tmp);
                }
            }
            if (list.size() > 0) {
                sendData(list);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {

            }
        }
    }
}
