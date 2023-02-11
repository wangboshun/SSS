package org.example.tdengine;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        try {
            test();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void test() throws ClassNotFoundException, SQLException {
        System.load("C://TDengine//driver//taos.dll");
        Class.forName("com.taosdata.jdbc.TSDBDriver");
        String jdbcUrl = "jdbc:TAOS://127.0.0.1:6030/test?user=root&password=taosdata";
        Connection conn = DriverManager.getConnection(jdbcUrl);
//        "create table table_test  (TM timestamp, STCD varchar(20),AA float, BB float,CC float,DD float,EE float,FF float,GG float,HH float,II float,JJ float)"
        String sql = "insert into table_test ( TM,STCD,AA,BB,CC,DD,EE,FF,GG,HH,II,JJ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = null;
        List<String> stcd_array = new ArrayList<>();
        List<LocalDateTime> tm_array = new ArrayList<>();
        for (int i = 10000000; i < 10010000; i++) {
            stcd_array.add(i + "");
        }
        LocalDateTime t = LocalDateTime.of(2023, 01, 01, 0, 0, 0);
        for (int i = 0; i < 10000; i++) {
            tm_array.add(t.plusMinutes(i * 5));
        }
        ps = conn.prepareStatement(sql);
        for (String stcd : stcd_array) {
            for (LocalDateTime tm : tm_array) {
                Timestamp timestamp = Timestamp.valueOf(tm);
                ps.setTimestamp(1, timestamp);
                ps.setObject(2, stcd);
                ps.setObject(3, 123);
                ps.setObject(4, 123);
                ps.setObject(5, 123);
                ps.setObject(6, 123);
                ps.setObject(7, 123);
                ps.setObject(8, 123);
                ps.setObject(9, 123);
                ps.setObject(10, 123);
                ps.setObject(11, 123);
                ps.setObject(12, 123);
                ps.execute();
            }
        }
    }
}
