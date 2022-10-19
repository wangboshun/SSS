package com.zny.pipe.component;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.zny.pipe.model.ConnectConfigModel;

import java.sql.Connection;
import java.sql.SQLException;

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
                default:
                    return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
