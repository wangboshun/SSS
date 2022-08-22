package sync;

import java.sql.*;

/**
 * @author WBS
 * Date:2022/6/15
 */

public class sync {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        jdbc();

//        String stcd="MS1068111198088884";
//
//        MysqlDataSource mysqlDataSource = new MysqlDataSource();
//        mysqlDataSource.setUrl("jdbc:mysql://10.230.30.90:15240/zhsw?useSSL=false&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false");
//        mysqlDataSource.setUser("root");
//        mysqlDataSource.setPassword("zny123!@#");
//
//        Connection connect = mysqlDataSource.getConnection();
//        Statement stmt = connect.createStatement();
//        ResultSet result = stmt.executeQuery("SELECT c.POSSIMAX as maxs,c.POSSIMIN as mins FROM\n" +
//                "\t                    ST_STBPRP_B AS a\n" +
//                "\t                    INNER JOIN ST_EQUIPMENT_B AS b ON a.STCD = b.STCD\n" +
//                "\t                    INNER JOIN ST_EQEL_B AS c ON c.EQID = b.EQID\n" +
//                "\t                    INNER JOIN ST_ELEMENT_B AS d ON ( d.STTP = a.STTP AND d.SYMBOL = c.SYMBOL )  \n" +
//                "                    where    (d.FIELDNM LIKE concat('%','RZ','%'))  and " +
//                " b.EQTP='SENSOR' and  a.STCD='"+stcd+"'");
//
//        while (result.next()) {
//            System.out.println(result.getString(1));
//            System.out.println(result.getString(2));
//        }

    }

    public static void jdbc() throws ClassNotFoundException, SQLException {

        //1.加载驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.获得连接
        String username = "root";
        String Pwd = "zny123!@#";

//        String username = "root";
//       String Pwd = "Slw123456.";
        String url = "jdbc:mysql://10.230.30.90:15240/zhsw";
//        String url = "jdbc:mysql://47.118.58.79:3306/test1";
        Connection connection = DriverManager.getConnection(url,username,Pwd);
        //3 定义sql  创建状态通道(进行sql语句的发送)
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from ST_RSVR_R limit 10");
        //4 取出结果集
        while (resultSet.next()){//判断是否有下一条数据
            //取出数据
            System.out.println("学号"+resultSet.getInt("stuId")+ "\t"+"姓名:"+resultSet.getString("stuname"));
        }
    }


    public static String[] getStcd() {
        String sql = "select STCD from ST_RSVR_R GROUP BY STCD ";

        return null;
    }

}
