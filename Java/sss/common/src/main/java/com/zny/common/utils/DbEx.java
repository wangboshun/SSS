package com.zny.common.utils;

import com.zny.common.enums.DbTypeEnum;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/10/12
 * 数据库操作帮助类
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

    /**
     * 查询数据是否存在
     *
     * @param connection   链接
     * @param tableName    表名
     * @param data         数据
     * @param primaryField 主键
     * @param dbType       数据类型
     */
    public static boolean hasData(Connection connection, String tableName, Map<String, Object> data, String[] primaryField, DbTypeEnum dbType) {
        int number = 0;
        ResultSet resultSet = null;
        PreparedStatement pstm = null;
        try {
            String sql = "";
            StringBuilder whereSql = new StringBuilder(" WHERE ");

            for (String field : primaryField) {
                switch (dbType) {
                    case MySQL:
                        whereSql.append(" `").append(field).append("`=? ");
                        break;
                    case MsSQL:
                        whereSql.append(" [").append(field).append("]=? ");
                        break;
                    default:
                        break;
                }
                whereSql.append(" AND ");
            }
            whereSql.delete(whereSql.length() - 5, whereSql.length());
            switch (dbType) {
                case MySQL:
                    sql = String.format("select 1 as number from %s%s  limit  1 ", tableName, whereSql);
                    break;
                case MsSQL:
                    sql = String.format("SELECT TOP 1 1 as number FROM %s%s", whereSql, tableName);
                    break;
                default:
                    break;
            }
            pstm = connection.prepareStatement(sql);
            int index = 1;
            for (String field : primaryField) {
                pstm.setObject(index, data.get(field));
                index++;
            }

            resultSet = pstm.executeQuery();
            while (resultSet.next()) {
                number = resultSet.getInt("number");
            }

            if (number > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("hasData: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (pstm != null) {
                    pstm.close();
                }
            } catch (SQLException e) {
                System.out.println("hasData: " + e.getMessage());
            }
        }
        return false;
    }

    /**
     * 释放资源
     *
     * @param connection 数据库链接
     * @param stmt       声明
     * @param rs         结果集
     */
    public static void release(Connection connection, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("DbEx release: " + e.getMessage());
        }
    }

    /**
     * 释放资源
     *
     * @param connection 数据库链接
     * @param stmt       声明
     */
    public static void release(Connection connection, Statement stmt) {
        release(connection, stmt, null);
    }

    /**
     * 释放资源
     *
     * @param connection 链接
     */
    public static void release(Connection connection) {
        release(connection, null, null);
    }

    /**
     * 释放资源
     *
     * @param stmt 声明
     */
    public static void release(Statement stmt) {
        release(null, stmt, null);
    }

    /**
     * 释放资源
     *
     * @param stmt 声明
     * @param rs   结果集
     */
    public static void release(Statement stmt, ResultSet rs) {
        release(null, stmt, rs);
    }

}
