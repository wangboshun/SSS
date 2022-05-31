package org.wbs.quality.db.connection;


import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import org.wbs.quality.db.ConnectionStrategy;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * MsSQL连接
 *
 * @author WBS
 */
public class MsSqlConnection implements ConnectionStrategy {

    /**
     * 完善连接字符串
     *
     * @param url 连接
     * @return 连接
     */
    private String checkUrl(String url) {

        if (!url.contains("integratedSecurity")) {
            url += ";integratedSecurity=true";
        }

        if (!url.contains("encrypt")) {
            url += ";encrypt=true";
        }

        if (!url.contains("trustServerCertificate")) {
            url += ";trustServerCertificate=true";
        }

        return url;
    }

    /**
     * 获取数据库连接
     */
    @Override
    public Connection getConnection(String url) {
        SQLServerDataSource sqlServerDataSource = new SQLServerDataSource();
        url = checkUrl(url);
        sqlServerDataSource.setURL(url);
        try {
            return sqlServerDataSource.getConnection();
        } catch (SQLException e) {
            System.out.println("连接错误：" + e.getMessage());
            return null;
        }
    }

    /**
     * 获取数据库连接
     */
    @Override
    public Connection getConnection(String url, String name, String password) {
        SQLServerDataSource sqlServerDataSource = new SQLServerDataSource();
        url = checkUrl(url);
        sqlServerDataSource.setURL(url);
        try {
            return sqlServerDataSource.getConnection(name, password);
        } catch (SQLException e) {
            System.out.println("连接错误：" + e.getMessage());
            return null;
        }
    }

    /**
     * 获取数据库连接
     */
    @Override
    public Connection getConnection(String host, int port, String db, String name, String password) {
        String url = "jdbc:sqlserver://" + host + ":" + port + ";database=" + db;
        return getConnection(url, name, password);
    }
}
