package org.example;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {

        int total = 0;
        try {
            FileReader fr = new FileReader(new File("4无标题.sql"));
            LineNumberReader lnr = new LineNumberReader(fr);
            lnr.skip(Long.MAX_VALUE);
            total = lnr.getLineNumber() + 1;
            lnr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int count = 0;
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("4无标题.sql"), "utf-8"));
        String connectStr = "jdbc:mysql://127.0.0.1:3307/zhsw?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&rewriteBatchedStatements=true";
        try {
            long bTime1 = System.currentTimeMillis();
            MysqlDataSource mysqlDataSource = new MysqlDataSource();
            mysqlDataSource.setURL(connectStr);
            Connection connect = mysqlDataSource.getConnection("root", "zny123!@#");
            Statement st = connect.createStatement();
            String line;
            while ((line = br.readLine()) != null) {
                count++;
                st.addBatch(line);
                if (count % 1000 == 0 || count == total) {
                    st.executeBatch();
                    st.clearBatch();
                    System.out.println("插入" + count);
                }
            }
            long eTime1 = System.currentTimeMillis();
            System.out.println("插入" + count + "数据共耗时：" + (eTime1 - bTime1));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        br.close();
    }
}
