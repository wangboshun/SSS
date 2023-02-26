package com.wbs.common.database;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WBS
 * @date 2023/2/23 9:42
 * @desciption 连接工厂
 */
@Component
public class ConnectionFactory {
    private volatile Map<String, Connection> connectionMap = new HashMap<>();

    /**
     * 获取所有连接
     *
     * @return
     */
    public Map<String, Connection> getAllConnection() {
        return connectionMap;
    }

    /**
     * 移除连接
     */
    public void removeConnect(String connectionName) {
        connectionMap.remove(connectionName);
    }

    /**
     * 创建连接
     *
     * @param connectionName
     * @param dataSource
     * @return
     */
    public Connection createConnection(String connectionName, DataSource dataSource) {
        Connection connection = connectionMap.get(connectionName);
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            connection = dataSource.getConnection();
            connectionMap.put(connectionName, connection);
            return connection;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return connection;
    }
}
