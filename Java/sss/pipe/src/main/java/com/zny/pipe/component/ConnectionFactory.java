package com.zny.pipe.component;

import com.clickhouse.jdbc.ClickHouseDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.zny.pipe.model.ConnectConfigModel;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;

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
                    return mysqlDataSource.getConnection(connectConfig.getUsername(), connectConfig.getPassword());
                case 1:
                    connectStr = "jdbc:sqlserver://" + connectConfig.getHost() + ":" + connectConfig.getPort() + ";database=" + connectConfig.getDb_name() + ";integratedSecurity=false;encrypt=true;trustServerCertificate=true";
                    SQLServerDataSource sqlServerDataSource = new SQLServerDataSource();
                    sqlServerDataSource.setURL(connectStr);
                    return sqlServerDataSource.getConnection(connectConfig.getUsername(), connectConfig.getPassword());
                case 2:
                    connectStr = "jdbc:postgresql://" + connectConfig.getHost() + ":" + connectConfig.getPort() + "/" + connectConfig.getDb_name() + "?currentSchema=" + connectConfig.getDb_schema();
                    PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
                    pgSimpleDataSource.setURL(connectStr);
                    return pgSimpleDataSource.getConnection(connectConfig.getUsername(), connectConfig.getPassword());
                case 3:
                    connectStr = "jdbc:clickhouse://" + connectConfig.getHost() + ":" + connectConfig.getPort() + "/" + connectConfig.getDb_name();
                    ClickHouseDataSource clickHouseDataSource = new ClickHouseDataSource(connectStr);
                    return clickHouseDataSource.getConnection(connectConfig.getUsername(), connectConfig.getPassword());
                default:
                    return null;
            }
        } catch (SQLException e) {
            System.out.println("getConnection exception:" + e.getMessage());
            return null;
        }
    }
}
