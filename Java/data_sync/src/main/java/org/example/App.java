package org.example;

import com.domain.Test1;
import com.mapper.Test1Mapper;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");
        Mybatis();
    }


    /**
     * 获取数据  mysql
     */
    public static void getStreamData1() {
        String connectStr = "jdbc:mysql://127.0.0.1:3306/test1?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        try {

            MysqlDataSource mysqlDataSource = new MysqlDataSource();
            mysqlDataSource.setURL(connectStr);
            Connection connect = mysqlDataSource.getConnection("root", "123456");

            /* JDBC直连
             Class.forName("com.mysql.cj.jdbc.Driver");
             Connection connect = DriverManager.getConnection(connectStr, "root", "123456");
            */

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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取数据  sqlserver
     */
    public static void getStreamData2() {
        String connectStr = "jdbc:sqlserver://127.0.0.1:1433;database=test1;integratedSecurity=true;encrypt=true;trustServerCertificate=true";
        try {
            SQLServerDataSource sqlServerDataSource = new SQLServerDataSource();
            sqlServerDataSource.setURL(connectStr);
            Connection connect = sqlServerDataSource.getConnection("sa", "123456");
            List<Map<String, Object>> list = new ArrayList<>();
            PreparedStatement ps = connect.prepareStatement("select   *  from Test1 ", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ps.setFetchSize(10000);
            ResultSet result = ps.executeQuery();
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取数据  mysql  大数据流获取
     */
    public static void getStreamData3() {
        String connectStr = "jdbc:mysql://127.0.0.1:3306/test1?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        try {
            MysqlDataSource mysqlDataSource = new MysqlDataSource();
            mysqlDataSource.setURL(connectStr);
            Connection connect = mysqlDataSource.getConnection("root", "123456");
            Statement stmt = connect.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            ResultSet result = stmt.executeQuery("select * from Test1");
            int num = 0;
            while (result.next()) {
                num += 1;
                System.out.println("num:" + num);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void Mybatis() throws IOException {
        InputStream is = Resources.getResourceAsStream("mybatis.xml");
        //获取SqlSessionFactoryBuilder
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        //获取SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
        //获取SqlSession。设置为true时，会自动提交事务
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper接口对象
        Test1Mapper mapper = sqlSession.getMapper(Test1Mapper.class);
        //测试功能
        //调用mapper接口的方法
        Test1 result = mapper.selectByPrimaryKey("100");
        System.out.println( result.toString());
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

