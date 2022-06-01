package org.wbs.quality.db;

import org.wbs.quality.db.connection.MsSqlConnection;
import org.wbs.quality.db.connection.MySqlConnection;

import java.sql.Connection;

/**
 * 数据库工厂
 *
 * @author WBS
 */
public class DbFactory {
    private ConnectionStrategy strategy;

    private Connection connection;

    public DbFactory(SqlEnum type, String url) {
        initConnection(type);
        this.connection = strategy.getConnection(url);
    }

    public DbFactory(SqlEnum type, String url, String name, String password) {
        initConnection(type);
        this.connection = strategy.getConnection(url, name, password);
    }

    public DbFactory(SqlEnum type, String host, int port, String db, String name, String password) {
        initConnection(type);
        this.connection = strategy.getConnection(host, port, db, name, password);
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * 初始化连接
     */
    private void initConnection(SqlEnum type) {
        switch (type) {
            case MYSQL:
                strategy = new MySqlConnection();
                break;
            case MSSQL:
                strategy = new MsSqlConnection();
                break;
            default:
                System.out.println("没有数据源");
        }
    }
}
