package com.zny.pipe.component.source;

import com.google.gson.Gson;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
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
 * Date:2022/10/14
 * mssql源端服务类
 */

@Component
public class MsSqlSource implements SourceBase {
    private Connection connection;

    private final RabbitTemplate rabbitTemplate;

    public MsSqlSource(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

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

    /**
     * 结束
     */
    @Override
    public void start() {
        try {
            configDataSource();
            getData();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     * 开始
     */
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
        rabbitTemplate.convertAndSend("Pipe_Exchange", "MsSql_RoutKey", json);
    }

    /**
     * 获取数据
     */
    private void getData() {
        try {
            Statement stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MAX_VALUE);
            ResultSet result = stmt.executeQuery("select top 1000 * from Test1 ");
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
