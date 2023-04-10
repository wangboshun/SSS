package com.wbs.common.database.factory;

import cn.hutool.db.GlobalDbConfig;
import com.clickhouse.jdbc.ClickHouseDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.wbs.common.database.base.DbTypeEnum;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author WBS
 * @date 2023/2/23 9:42
 * @desciption 数据源工厂
 */
@Component
public class DataSourceFactory {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ConcurrentHashMap<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

    /**
     * 获取所有数据源
     *
     * @return
     */
    public ConcurrentHashMap<String, DataSource> getAllDataSource() {
        return dataSourceMap;
    }

    /**
     * 移除数据源
     */
    public void removeDataSource(String dataSourceName) {
        dataSourceMap.remove(dataSourceName);
    }

    /**
     * 获取数据源
     *
     * @param dataSourceName
     * @return
     */
    public DataSource getDataSource(String dataSourceName) {
        return dataSourceMap.get(dataSourceName);
    }

    /**
     * 创建数据源
     *
     * @param dataSourceName
     * @param host
     * @param port
     * @param username
     * @param password
     * @param database
     * @param type
     * @return
     * @throws SQLException
     */
    public DataSource createDataSource(String dataSourceName, String host, int port, String username, String password, String database, DbTypeEnum type) throws SQLException {
        return createDataSource(dataSourceName, host, port, username, password, database, "public", type);
    }

    /**
     * 创建数据源
     *
     * @param dataSourceName
     * @param host
     * @param port
     * @param username
     * @param password
     * @param database
     * @param schema
     * @param type
     * @return
     * @throws SQLException
     */
    public DataSource createDataSource(String dataSourceName, String host, int port, String username, String password, String database, String schema, DbTypeEnum type) throws SQLException {
        DataSource dataSource = dataSourceMap.get(dataSourceName);
        if (dataSource != null) {
            return dataSource;
        }
        try {
            String url = "";
            switch (type) {
                case MYSQL:
                    url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&rewriteBatchedStatements=true&useInformationSchema=true&characterEncoding=UTF-8";
                    dataSource = getMySqlDataSource(url, username, password);
                    break;
                case SQLSERVER:
                    url = "jdbc:sqlserver://" + host + ":" + port + ";database=" + database + ";integratedSecurity=false;encrypt=true;trustServerCertificate=true";
                    dataSource = getSqlServerDataSource(url, username, password);
                    break;
                case POSTGRESQL:
                    url = "jdbc:postgresql://" + host + ":" + port + "/" + database + "?currentSchema=" + schema;
                    dataSource = getPostgreSqlDataSource(url, username, password);
                    break;
                case CLICKHOUSE:
                    url = "jdbc:clickhouse://" + host + ":" + port + "/" + database;
                    dataSource = getClickHouseDataSource(url, username, password);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            logger.error("------createDataSource error------", e);
            throw new SQLException("数据源错误");
        }
        return cacheDataSource(dataSourceName, dataSource);
    }

    public DataSource createDataSource(String dataSourceName, String url, String username, String password) throws SQLException {
        DataSource dataSource = dataSourceMap.get(dataSourceName);
        if (dataSource != null) {
            return dataSource;
        }
        try {
            String[] array = url.split(":");
            String type = array[1].toLowerCase();
            switch (type) {
                case "mysql":
                    dataSource = getMySqlDataSource(url, username, password);
                    break;
                case "sqlserver":
                    dataSource = getSqlServerDataSource(url, username, password);
                    break;
                case "postgresql":
                    dataSource = getPostgreSqlDataSource(url, username, password);
                    break;
                case "clickhouse":
                    dataSource = getClickHouseDataSource(url, username, password);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            logger.error("------createDataSource error------", e);
            throw new SQLException("数据源错误");
        }
        return cacheDataSource(dataSourceName, dataSource);
    }

    private DataSource cacheDataSource(String dataSourceName, DataSource dataSource) throws SQLException {
        Connection connection = null;
        try {
            if (dataSource == null) {
                logger.error("------cacheDataSource dataSource is null------");
            } else {
                // 测试数据源是否能打开
                connection = dataSource.getConnection();
                dataSourceMap.put(dataSourceName, dataSource);
            }
        } catch (SQLException e) {
            logger.error("------cacheDataSource error------", e);
            throw new SQLException("数据源错误");
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return dataSource;
    }

    public DataSource getMySqlDataSource(String url, String username, String password) {
        GlobalDbConfig.setReturnGeneratedKey(true);
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setURL(url);
        mysqlDataSource.setUser(username);
        mysqlDataSource.setPassword(password);
        return mysqlDataSource;
    }

    public DataSource getSqlServerDataSource(String url, String username, String password) {
        GlobalDbConfig.setReturnGeneratedKey(true);
        SQLServerDataSource sqlServerDataSource = new SQLServerDataSource();
        sqlServerDataSource.setURL(url);
        sqlServerDataSource.setUser(username);
        sqlServerDataSource.setPassword(password);
        return sqlServerDataSource;
    }

    public DataSource getPostgreSqlDataSource(String url, String username, String password) {
        GlobalDbConfig.setReturnGeneratedKey(true);
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setURL(url);
        pgSimpleDataSource.setUser(username);
        pgSimpleDataSource.setPassword(password);
        return pgSimpleDataSource;
    }

    public DataSource getClickHouseDataSource(String url, String username, String password) {
        try {
            GlobalDbConfig.setReturnGeneratedKey(false);
            Properties properties = new Properties();
            properties.setProperty("user", username);
            properties.setProperty("password", password);
            return new ClickHouseDataSource(url, properties);
        } catch (Exception e) {
            return null;
        }
    }
}
