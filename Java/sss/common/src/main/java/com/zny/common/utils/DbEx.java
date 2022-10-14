package com.zny.common.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WBS
 * Date:2022/10/12
 */
public class DbEx {

    /**
     * 获取表的所有列
     */
    public static List<String> getField(ResultSet result) {
        List<String> fieldList = new ArrayList<>();
        try {
            ResultSetMetaData meta = result.getMetaData();
            int columnCount = meta.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                fieldList.add(meta.getColumnName(i));
            }
        } catch (SQLException e) {
            System.out.println("getField error:" + e.getMessage());
        }
        return fieldList;
    }
}
