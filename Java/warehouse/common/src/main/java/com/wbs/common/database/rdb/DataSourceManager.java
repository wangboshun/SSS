package com.wbs.common.database.rdb;

import com.wbs.common.database.base.model.DataSourceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author WBS
 * @date 2023/4/15 11:44
 * @desciption DataSourceManager
 */
@Component
public class DataSourceManager {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final ConcurrentHashMap<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Connection> connectionMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, DataSourceFactory> dataSourceFactorMap = new ConcurrentHashMap<>();


    /**
     * 添加数据源
     *
     * @param info 数据源配置
     * @return 数据源名称
     */
    public String addDataSource(DataSourceInfo info) {
        DataSourceFactory factory = null;
        switch (info.getDbType()) {
            case MYSQL:
                factory = new MySqlDataSourceFactory();
                break;
            case SQLSERVER:
                factory = new SqlServerDataSourceFactory();
                break;
            case POSTGRESQL:
                factory = new PostgreSqlDataSourceFactory();
                break;
            case CLICKHOUSE:
                factory = new ClickHouseDataSourceFactory();
                break;
            default:
                break;
        }
        if (factory != null) {
            factory.config(info);
            dataSourceFactorMap.put(info.getName(), factory);
            return info.getName();
        }
        return null;
    }

    /**
     * 获取数据源
     *
     * @param dataSourceName 名称
     */
    public DataSource getDataSource(String dataSourceName) {
        DataSource dataSource = dataSourceMap.get(dataSourceName);
        if (dataSource == null) {
            DataSourceFactory factory = dataSourceFactorMap.get(dataSourceName);
            if (factory != null) {
                dataSource = factory.createDataSource();
                dataSourceMap.put(dataSourceName, dataSource);
            }
        }
        return dataSource;
    }

    /**
     * 获取连接
     *
     * @param connectionName 名称
     */
    public Connection createConnection(String connectionName) {
        Connection connection = connectionMap.get(connectionName);
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            DataSource dataSource = getDataSource(connectionName);
            connection = dataSource.getConnection();
            connectionMap.put(connectionName, connection);
            return connection;
        } catch (SQLException e) {
            logger.error("createConnection exception", e);
        }
        return connection;
    }

    /**
     * 移除数据源
     */
    public void removeDataSource(String dataSourceName) {
        dataSourceFactorMap.remove(dataSourceName);
    }

    /**
     * 移除连接
     */
    public void removeConnect(String connectionName) {
        connectionMap.remove(connectionName);
    }

    /**
     * 获取所有连接
     */
    public ConcurrentHashMap<String, Connection> getAllConnection() {
        return connectionMap;
    }

    /**
     * 获取所有数据源
     */
    public ConcurrentHashMap<String, DataSource> getAllDataSource() {
        return dataSourceMap;
    }
}
