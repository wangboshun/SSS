package com.wbs.common.database;

import com.clickhouse.jdbc.ClickHouseDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author WBS
 * @date 2023/2/23 9:42
 * @desciption 数据源工厂
 */
@Component
public class DataSourceFactory {

    private volatile Map<String, DataSource> dataSourceMap = new HashMap<>();

    /**
     * 获取所有数据源
     *
     * @return
     */
    public Map<String, DataSource> getAllDataSource() {
        return dataSourceMap;
    }

    /**
     * 移除数据源
     */
    public void removeDataSource(String dataSourceName) {
        dataSourceMap.remove(dataSourceName);
    }

    public DataSource createDataSource(String dataSourceName, String host, int port, String username, String password, String database, DbTypeEnum type) throws SQLException {
        return createDataSource(dataSourceName, host, port, username, password, database, "", type);
    }

    /**
     * 创建数据源
     */
    public DataSource createDataSource(String dataSourceName, String host, int port, String username, String password, String database, String schema, DbTypeEnum type) throws SQLException {
        DataSource dataSource = dataSourceMap.get(dataSourceName);
        if (dataSource != null) {
            return dataSource;
        }
        try {
            String connectStr = "";
            switch (type.ordinal()) {
                case 0:
                    connectStr = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&rewriteBatchedStatements=true";
                    MysqlDataSource mysqlDataSource = new MysqlDataSource();
                    mysqlDataSource.setURL(connectStr);
                    mysqlDataSource.setUser(username);
                    mysqlDataSource.setPassword(password);
                    dataSource = mysqlDataSource;
                    break;
                case 1:
                    connectStr = "jdbc:sqlserver://" + host + ":" + port + ";database=" + database + ";integratedSecurity=false;encrypt=true;trustServerCertificate=true";
                    SQLServerDataSource sqlServerDataSource = new SQLServerDataSource();
                    sqlServerDataSource.setURL(connectStr);
                    sqlServerDataSource.setUser(username);
                    sqlServerDataSource.setPassword(password);
                    dataSource = sqlServerDataSource;
                    break;
                case 2:
                    connectStr = "jdbc:postgresql://" + host + ":" + port + "/" + database + "?currentSchema=" + schema;
                    PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
                    pgSimpleDataSource.setURL(connectStr);
                    pgSimpleDataSource.setUser(username);
                    pgSimpleDataSource.setPassword(password);
                    dataSource = pgSimpleDataSource;
                    break;
                case 3:
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
        } catch (SQLException e) {
            System.out.println(e);
        }
        return dataSource;
    }
}
