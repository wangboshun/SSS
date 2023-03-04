package com.wbs.common.database;

import com.clickhouse.jdbc.ClickHouseDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.postgresql.ds.PGSimpleDataSource;
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
     * @param dataSourceName
     * @return
     */
    public DataSource getDataSource(String dataSourceName) {
       return dataSourceMap.get(dataSourceName);
    }

    /**
     * 创建数据源
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
        return createDataSource(dataSourceName, host, port, username, password, database, "", type);
    }

    /**
     * 创建数据源
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
        Connection connection = null;
        if (dataSource != null) {
            return dataSource;
        }
        try {
            String connectStr = "";
            switch (type) {
                case MySql:
                    connectStr = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&rewriteBatchedStatements=true&useInformationSchema=true";
                    MysqlDataSource mysqlDataSource = new MysqlDataSource();
                    mysqlDataSource.setURL(connectStr);
                    mysqlDataSource.setUser(username);
                    mysqlDataSource.setPassword(password);
                    dataSource = mysqlDataSource;
                    break;
                case SqlServer:
                    connectStr = "jdbc:sqlserver://" + host + ":" + port + ";database=" + database + ";integratedSecurity=false;encrypt=true;trustServerCertificate=true";
                    SQLServerDataSource sqlServerDataSource = new SQLServerDataSource();
                    sqlServerDataSource.setURL(connectStr);
                    sqlServerDataSource.setUser(username);
                    sqlServerDataSource.setPassword(password);
                    dataSource = sqlServerDataSource;
                    break;
                case PostgreSql:
                    connectStr = "jdbc:postgresql://" + host + ":" + port + "/" + database + "?currentSchema=" + schema;
                    PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
                    pgSimpleDataSource.setURL(connectStr);
                    pgSimpleDataSource.setUser(username);
                    pgSimpleDataSource.setPassword(password);
                    dataSource = pgSimpleDataSource;
                    break;
                case ClickHouse:
                    connectStr = "jdbc:clickhouse://" + host + ":" + port + "/" + database;
                    Properties properties = new Properties();
                    properties.setProperty("user", username);
                    properties.setProperty("password", password);
                    ClickHouseDataSource clickHouseDataSource = new ClickHouseDataSource(connectStr, properties);
                    dataSource = clickHouseDataSource;
                    break;
                default:
                    break;
            }
            //测试数据源是否能打开
            connection = dataSource.getConnection();
            dataSourceMap.put(dataSourceName, dataSource);
        } catch (Exception e) {
            System.out.println(e);
            throw new SQLException("数据源错误");
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return dataSource;
    }
}
