package org.wbs.quality.db;

import cn.hutool.core.util.ClassUtil;
import org.junit.jupiter.api.Test;
import org.wbs.quality.check.AbstractDataCheck;
import org.wbs.quality.check.CheckInvoker;
import org.wbs.quality.check.CompareEnum;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

class ConnectFactoryTest {

    /**
     * mysql测试
     */
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

    /**
     * mssql测试
     */
    @Test
    void mssql() {
        Connection connect = new ConnectFactory("MsSql").getConnection("127.0.0.1", 1433, "test1", "sa", "123456");
        try {
            Statement stmt = connect.createStatement();
            ResultSet result = stmt.executeQuery("select  top 100  * from Test1 ");
            while (result.next()) {
                datacheckdata(result.getBigDecimal(3));
//                System.out.println(result.getString(1) + "  " + result.getString(2)+ "  " + result.getBigDecimal(3));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    void datacheckdata(BigDecimal value){
        Set<Class<?>> list = ClassUtil.scanPackageBySuper(null, AbstractDataCheck.class);
        Set<AbstractDataCheck> l = new HashSet<>();

        for (Class<?> c : list) {
            try {
                l.add((AbstractDataCheck) Class.forName(c.getName()).getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        CheckInvoker invoker = new CheckInvoker(l);
        invoker.action(value, CompareEnum.GREATER);
    }
}