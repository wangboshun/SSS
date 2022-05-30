package com.zny.quality.db;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class ConnectFactoryTest {
    @Test
    void mysql() {
        Connection connect = new ConnectFactory("MySql").getConnection("127.0.0.1", 3306, "test1", "root", "123456");
        try {
            Statement stmt = connect.createStatement();
            ResultSet result = stmt.executeQuery("select * from test1 limit 100 ");
            while (result.next()) {
                System.out.println(result.getString(1) + "  " + result.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void mssql() {
        Connection connect = new ConnectFactory("MsSql").getConnection("127.0.0.1", 1433, "test1", "sa", "123456");
        try {
            Statement stmt = connect.createStatement();
            ResultSet result = stmt.executeQuery("select  top 100  * from Test1 ");
            while (result.next()) {
                System.out.println(result.getString(1) + "  " + result.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}