package com.zny.pipe.component;

import com.clickhouse.jdbc.ClickHouseDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.zny.pipe.model.ConnectConfigModel;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author WBS
 * Date:2022/11/17
 * 数据库连接工厂
 */

public class ConnectionFactory {
    public static Connection getConnection(ConnectConfigModel connectConfig) {
        try {
            String connectStr = "";
            switch (connectConfig.getDb_type()) {
                case 0:
                    connectStr = "jdbc:mysql://" + connectConfig.getHost() + ":" + connectConfig.getPort() + "/" + connectConfig.getDb_name() + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&rewriteBatchedStatements=true";
                    MysqlDataSource mysqlDataSource = new MysqlDataSource();
                    mysqlDataSource.setURL(connectStr);
                    mysqlDataSource.setUser(connectConfig.getUsername());
                    mysqlDataSource.setPassword(connectConfig.getPassword());
                    return mysqlDataSource.getConnection();
                case 1:
                    connectStr = "jdbc:sqlserver://" + connectConfig.getHost() + ":" + connectConfig.getPort() + ";database=" + connectConfig.getDb_name() + ";integratedSecurity=false;encrypt=true;trustServerCertificate=true";
                    SQLServerDataSource sqlServerDataSource = new SQLServerDataSource();
                    sqlServerDataSource.setURL(connectStr);
                    sqlServerDataSource.setUser(connectConfig.getUsername());
                    sqlServerDataSource.setPassword(connectConfig.getPassword());
                    return sqlServerDataSource.getConnection();
                case 2:
                    connectStr = "jdbc:postgresql://" + connectConfig.getHost() + ":" + connectConfig.getPort() + "/" + connectConfig.getDb_name();
                    PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
                    pgSimpleDataSource.setURL(connectStr);
                    pgSimpleDataSource.setCurrentSchema(connectConfig.getDb_schema());
                    pgSimpleDataSource.setUser(connectConfig.getUsername());
                    pgSimpleDataSource.setPassword(connectConfig.getPassword());
                    return pgSimpleDataSource.getConnection();
                case 3:
                    connectStr = "jdbc:clickhouse://" + connectConfig.getHost() + ":" + connectConfig.getPort() + "/" + connectConfig.getDb_name();
                    Properties properties = new Properties();
                    properties.setProperty("user", connectConfig.getUsername());
                    properties.setProperty("password", connectConfig.getPassword());
                    ClickHouseDataSource clickHouseDataSource = new ClickHouseDataSource(connectStr,properties);
                    return clickHouseDataSource.getConnection();
                default:
                    return null;
            }
        } catch (SQLException e) {
            System.out.println("getConnection exception:" + e.getMessage());
            return null;
        }
    }
}
