package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        getStreamData();
    }


    /**
     * 获取数据
     */
    public static void getStreamData() {
        String driver = "com.mysql.cj.jdbc.Driver";
        String connectStr = "jdbc:mysql://47.118.58.79:3306/test1?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        try {
            Class.forName(driver);
            Connection connect = DriverManager.getConnection(connectStr, "root", "Slw123456.");
            Statement stmt = connect.createStatement();
            ResultSet result = stmt.executeQuery("select * from Test1");
            List<Map<String, Object>> list = new ArrayList<>();
            List<String> filedList = getField(result);
            while (result.next()) {
                Map<String, Object> rowData = new HashMap<>(filedList.size());
                for (String x : filedList) {
                    rowData.put(x, result.getObject(x));
                }

                list.add(rowData);

                if (list.size() >= 1000) {
                    List<Map<String, Object>> tmp = new ArrayList<>(list);
                    list.clear();
                    CustomerData c = new CustomerData(tmp);
                    c.start();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param result 查询结果
     * @return 返回字段结果集
     */
    public static List<String> getField(ResultSet result) {
        List<String> fieldList = new ArrayList<>();
        try {
            ResultSetMetaData meta = result.getMetaData();
            int columnCount = meta.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                if ("WRTM".equals(meta.getColumnName(i))) {
                    continue;
                }
                fieldList.add(meta.getColumnName(i));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return fieldList;
    }
}

