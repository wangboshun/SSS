package com.wbs.common.database;

import com.clickhouse.jdbc.ClickHouseDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author WBS
 * Date:2023/02/22
 * 数据库链接工厂
 */
public class ConnectionFactory {
    public static Connection getConnection(String host, int port, String username, String password, String database, String schema, DbTypeEnum type) throws SQLException {
        try {
            String connectStr = "";
            switch (type.ordinal()) {
                case 0:
                    connectStr = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&rewriteBatchedStatements=true";
                    MysqlDataSource mysqlDataSource = new MysqlDataSource();
                    mysqlDataSource.setURL(connectStr);
                    return mysqlDataSource.getConnection(username, password);
                case 1:
                    connectStr = "jdbc:sqlserver://" + host + ":" + port + ";database=" + database + ";integratedSecurity=false;encrypt=true;trustServerCertificate=true";
                    SQLServerDataSource sqlServerDataSource = new SQLServerDataSource();
                    sqlServerDataSource.setURL(connectStr);
                    return sqlServerDataSource.getConnection(username, password);
                case 2:
                    connectStr = "jdbc:postgresql://" + host + ":" + port + "/" + database + "?currentSchema=" + schema;
                    PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
                    pgSimpleDataSource.setURL(connectStr);
                    return pgSimpleDataSource.getConnection(username, password);
                case 3:
                    connectStr = "jdbc:clickhouse://" + host + ":" + port + "/" + database;
                    ClickHouseDataSource clickHouseDataSource = new ClickHouseDataSource(connectStr);
                    return clickHouseDataSource.getConnection(username, password);
                default:
                    return null;
            }
        } catch (SQLException e) {
            throw new SQLException("getConnection exception：" + e.getMessage());
        }
    }
}
