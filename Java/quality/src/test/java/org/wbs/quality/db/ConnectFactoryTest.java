package org.wbs.quality.db;

import cn.hutool.core.util.ClassUtil;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.wbs.quality.business.check.CheckInvoker;
import org.wbs.quality.business.check.DataCheckBase;
import org.wbs.quality.business.check.enums.CompareEnum;
import org.wbs.quality.infra.db.DbFactory;
import org.wbs.quality.infra.db.enums.SqlEnum;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

class ConnectFactoryTest {

    /**
     * mysql测试
     */
    @Test
    void mysql() {
        Connection connect = new DbFactory(SqlEnum.MYSQL, "127.0.0.1", 3306, "test1", "root", "123456").getConnection();
        try {

            Statement stmt = connect.createStatement();
            ResultSet result = stmt.executeQuery("select * from test1 limit 100 ");
            ResultSetMetaData metaData = result.getMetaData();
            List<Map<String, Object>> list = new ArrayList<>();

            while (result.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String field = metaData.getColumnName(i);
//                    map.put(field, DbUtils.getData(result, field));
                }
                System.out.println(map);
                list.add(map);
            }
            System.out.println(new Gson().toJson(list));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * mssql测试
     */
    @Test
    void mssql() {
        Connection connect = new DbFactory(SqlEnum.MSSQL, "127.0.0.1", 1433, "test1", "sa", "123456").getConnection();
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


    void datacheckdata(BigDecimal value) {
        Set<Class<?>> list = ClassUtil.scanPackageBySuper(null, DataCheckBase.class);
        Set<DataCheckBase> l = new HashSet<>();

        for (Class<?> c : list) {
            try {
                l.add((DataCheckBase) Class.forName(c.getName()).getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        CheckInvoker invoker = new CheckInvoker(l);
        invoker.action(value, CompareEnum.GREATER);
    }
}