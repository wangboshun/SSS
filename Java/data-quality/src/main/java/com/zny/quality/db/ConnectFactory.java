package com.zny.quality.db;

import java.sql.Connection;

/**
 * 连接工厂
 * @author WBS
 */
public class ConnectFactory {
    private ConnectionStrategy strategy;

    public ConnectFactory(String type) {
        switch (type) {
            case "MySql":
                strategy = new MySqlConnection();
                break;
            case "MsSql":
                strategy = new MsSqlConnection();
                break;
            default:
                System.out.println("没有数据源");
                break;
        }
    }

    public Connection getConnection(String url) {
        return strategy.getConnection(url);
    }

    public Connection getConnection(String url, String name, String password) {
        return strategy.getConnection(url, name, password);
    }

    public Connection getConnection(String host, int port,String db, String name, String password) {
        return strategy.getConnection(host, port,db, name, password);
    }
}
