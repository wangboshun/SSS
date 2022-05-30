package com.zny.quality.db;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * MySQL连接
 *
 * @author WBS
 */
public class MySqlConnection implements ConnectionStrategy {
    /**
     * 完善连接字符串
     *
     * @param url 连接
     * @return 连接
     */
    private String checkUrl(String url) {
        if (!url.contains("?")) {
            url += "?";
        }

        if (!url.contains("useSSL")) {
            url += "&useSSL=false";
        }

        if (!url.contains("allowPublicKeyRetrieval")) {
            url += "&allowPublicKeyRetrieval=true";
        }

        if (!url.contains("serverTimezone")) {
            url += "&serverTimezone=UTC";
        }

        if (!url.contains("rewriteBatchedStatements")) {
            url += "&rewriteBatchedStatements=true";
        }
        return url;
    }

    @Override
    public Connection getConnection(String url) {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        url = checkUrl(url);
        mysqlDataSource.setURL(url);
        try {
            return mysqlDataSource.getConnection();
        } catch (SQLException e) {
            System.out.println("连接错误：" + e.getMessage());
            return null;
        }
    }

    @Override
    public Connection getConnection(String url, String name, String password) {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        url = checkUrl(url);
        mysqlDataSource.setURL(url);
        try {
            return mysqlDataSource.getConnection(name, password);
        } catch (SQLException e) {
            System.out.println("连接错误：" + e.getMessage());
            return null;
        }
    }

    @Override
    public Connection getConnection(String host, int port, String db, String name, String password) {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + db;
        return getConnection(url, name, password);
    }
}
