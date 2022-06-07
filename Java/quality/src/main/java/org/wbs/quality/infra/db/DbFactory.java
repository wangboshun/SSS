package org.wbs.quality.infra.db;

import org.wbs.quality.infra.db.connection.ConnectionStrategy;
import org.wbs.quality.infra.db.connection.MsSqlConnection;
import org.wbs.quality.infra.db.connection.MySqlConnection;
import org.wbs.quality.infra.db.enums.SqlEnum;

import java.sql.Connection;

/**
 * 数据库工厂
 *
 * @author WBS
 */
public class DbFactory {
    private final Connection connection;
    private ConnectionStrategy strategy;

    public DbFactory(SqlEnum type, String host, int port, String db, String name, String password) {
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
        this.connection = strategy.getConnection(host, port, db, name, password);
    }

    public Connection getConnection() {
        return connection;
    }

}
