package org.example;

import java.sql.*;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        String connectStr = "jdbc:mysql://10.230.30.90:15240/zhsw_bak?useSSL=true&connectTimeout=6000&socketTimeout=6000";
        String driver = "com.mysql.cj.jdbc.Driver";

        Class.forName(driver);

        Connection connection= DriverManager.getConnection(connectStr,"root","zny123!@#");

//        MysqlDataSource mysqlDataSource = new MysqlDataSource();
//        mysqlDataSource.setURL(connectStr);
//        mysqlDataSource.setUser("root");
//        mysqlDataSource.setPassword("zny123!@#");
//        Connection connection = mysqlDataSource.getConnection();

        PreparedStatement pstm = null;
        ResultSet result = null;
        pstm = connection.prepareStatement("SELECT STCD,PTCD,TM,WRTM FROM ST_GATE_R WHERE TM>='2022-11-07 00:00:00' and TM<='2022-11-08 00:00:00'  GROUP BY STCD,PTCD,TM HAVING count(*)>1;", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        result = pstm.executeQuery();
        while (result.next()) {
            System.out.println(result.getString("STCD") + " " + result.getString("PTCD") + " " + result.getString("TM") + " " + result.getString("WRTM"));
        }
        System.out.println("Hello World!");
    }
}
