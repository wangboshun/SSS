package com.zny.common.utils;

import com.zny.common.enums.DbTypeEnum;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/10/12
 * 数据库操作帮助类
 */

public class DbEx {

    /**
     * 获取当前连接库下的所有表
     *
     * @param connection 连接
     */
    public static List<Map<String, String>> getTables(Connection connection) {
        List<Map<String, String>> list = new ArrayList<>();
        ResultSet rs = null;
        try {
            DatabaseMetaData dbMeta = connection.getMetaData();
            String driverName = connection.getMetaData().getDriverName().toUpperCase();
            //如果是mysql或者sqlserver，不需要schema
            if (driverName.contains("MYSQL") || driverName.contains("SQL SERVER")) {
                rs = dbMeta.getTables(connection.getCatalog(), null, null, new String[]{"TABLE"});
            }
            //如果是pgsql，需要指定schema，默认为public
            else if (driverName.contains("POSTGRESQL")) {
                rs = dbMeta.getTables(connection.getCatalog(), connection.getSchema(), null, new String[]{"TABLE"});
            }
            while (rs.next()) {
                Map<String, String> map = new HashMap<>();
                map.put("name", rs.getString("TABLE_NAME"));
                map.put("remark", rs.getString("REMARKS"));
                list.add(map);
            }
        } catch (SQLException e) {
            System.out.println("getTables : " + e.getMessage());
        } finally {
            DbEx.release(rs);
        }
        return list;
    }

    /**
     * 获取当前连接库下的所有库
     *
     * @param connection 连接
     */
    public static List<String> getDataBases(Connection connection) {
        List<String> list = new ArrayList<>();
        ResultSet rs = null;
        try {
            DatabaseMetaData dbMeta = connection.getMetaData();
            rs = dbMeta.getCatalogs();
            while (rs.next()) {
                list.add(rs.getString("TABLE_CAT"));
            }
        } catch (SQLException e) {
            System.out.println("getDataBases : " + e.getMessage());
        } finally {
            DbEx.release(rs);
        }
        return list;
    }

    /**
     * 获取当前连接库下的所有模式
     *
     * @param connection 连接
     */
    public static List<String> getSchemas(Connection connection) {
        List<String> list = new ArrayList<>();
        ResultSet rs = null;
        try {
            DatabaseMetaData dbMeta = connection.getMetaData();
            rs = dbMeta.getSchemas();
            while (rs.next()) {
                list.add(rs.getString("table_schem"));
            }
        } catch (SQLException e) {
            System.out.println("getSchemas : " + e.getMessage());
        } finally {
            DbEx.release(rs);
        }
        return list;
    }

    /**
     * 获取表的主键
     *
     * @param connection 连接
     * @param tableName  表名
     */
    public static List<String> getPrimaryKey(Connection connection, String tableName) {
        List<String> primaryKey = new ArrayList<>();
        ResultSet rs = null;
        try {
            DatabaseMetaData dbMeta = connection.getMetaData();
            rs = dbMeta.getPrimaryKeys(null, null, tableName);
            while (rs.next()) {
                primaryKey.add(rs.getString("COLUMN_NAME"));
            }

        } catch (SQLException e) {
            System.out.println("getPrimaryKey : " + e.getMessage());
        } finally {
            DbEx.release(rs);
        }
        return primaryKey;
    }

    /**
     * 获取表的所有列名
     */
    public static List<String> getColumnName(ResultSet result) {
        List<String> columnList = new ArrayList<>();
        try {
            ResultSetMetaData meta = result.getMetaData();
            int columnCount = meta.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                columnList.add(meta.getColumnName(i));
            }
        } catch (SQLException e) {
            System.out.println("getField error:" + e.getMessage());
        }
        return columnList;
    }


    /**
     * 查询数据是否存在
     *
     * @param connection    链接
     * @param tableName     表名
     * @param data          数据
     * @param primaryColumn 主键
     * @param dbType        数据类型
     */
    public static boolean hasData(Connection connection, String tableName, Map<String, Object> data, String[] primaryColumn, DbTypeEnum dbType) {
        int number = 0;
        ResultSet result = null;
        PreparedStatement pstm = null;
        try {
            String sql = "";
            StringBuilder whereSql = new StringBuilder(" WHERE ");

            for (String column : primaryColumn) {
                switch (dbType) {
                    case MySQL:
                        whereSql.append(" `").append(column).append("`=? ");
                        break;
                    case MsSQL:
                        whereSql.append(" [").append(column).append("]=? ");
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
            for (String column : primaryColumn) {
                pstm.setObject(index, data.get(column));
                index++;
            }

            result = pstm.executeQuery();
            while (result.next()) {
                number = result.getInt("number");
            }

            if (number > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("hasData: " + e.getMessage());
        } finally {
            release(pstm, result);
        }
        return false;
    }

    /**
     * 根据sql查询记录条数
     *
     * @param connection 连接
     * @param sql        sql
     */
    public static int getCount(Connection connection, String sql) {
        Statement stmt = null;
        ResultSet result = null;
        int count = 0;
        try {
            int index = sql.indexOf("ORDER BY");
            sql = sql.substring(0, index);
            if (sql.contains("*")) {
                sql = sql.replace("*", " count(0) ");
            } else {
                int selectIndex = sql.indexOf("select");
                if (selectIndex < 0) {
                    selectIndex = sql.indexOf("SELECT");
                }
                int fromIndex = sql.indexOf("from");
                if (fromIndex < 0) {
                    fromIndex = sql.indexOf("FROM");
                }
                String str = sql.substring(selectIndex + 6, fromIndex);
                sql = sql.replace(str, " count(0) ");
            }
            stmt = connection.createStatement();
            result = stmt.executeQuery(sql);
            if (result.next()) {
                count = result.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("SourceAbstract getCount: " + e.getMessage());
        } finally {
            release(stmt, result);
        }
        return count;
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

    /**
     * 释放资源
     *
     * @param rs 结果集
     */
    public static void release(ResultSet rs) {
        release(null, null, rs);
    }
}
