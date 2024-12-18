package com.zny.common.utils.database;

import com.zny.common.enums.DbTypeEnum;
import com.zny.common.utils.DateUtils;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.util.*;

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
    public static List<Map<String, String>> getTables(Connection connection) throws SQLException {
        List<Map<String, String>> list = new ArrayList<>();
        ResultSet rs = null;
        try {
            final String[] types = new String[]{"TABLE"};
            DatabaseMetaData dbMeta = connection.getMetaData();
            String driverName = connection.getMetaData().getDriverName().toUpperCase();
            //如果是mysql或者sqlserver，不需要schema
            if (driverName.contains("MYSQL") || driverName.contains("SQL SERVER")) {
                rs = dbMeta.getTables(connection.getCatalog(), null, null, types);
            }
            //如果是pgsql，需要指定schema，默认为public
            else if (driverName.contains("POSTGRESQL")) {
                rs = dbMeta.getTables(connection.getCatalog(), connection.getSchema(), null, types);
            } else if (driverName.contains("CLICKHOUSE")) {
                //根据clickhouse源码研究，由于clickhouse有多种表类型，所以这里传空
                rs = dbMeta.getTables(connection.getCatalog(), connection.getSchema(), null, null);
            }
            while (rs.next()) {
                Map<String, String> map = new HashMap<>();
                map.put("name", rs.getString("TABLE_NAME"));
                map.put("remark", rs.getString("REMARKS"));
                list.add(map);
            }
        } catch (SQLException e) {
            throw new SQLException("getTables exception：" + e.getMessage());
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
    public static List<String> getDataBases(Connection connection) throws SQLException {
        List<String> list = new ArrayList<>();
        ResultSet rs = null;
        try {
            DatabaseMetaData dbMeta = connection.getMetaData();
            rs = dbMeta.getCatalogs();
            while (rs.next()) {
                list.add(rs.getString("TABLE_CAT"));
            }
        } catch (SQLException e) {
            throw new SQLException("getDataBases exception：" + e.getMessage());
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
    public static List<String> getSchemas(Connection connection) throws SQLException {
        List<String> list = new ArrayList<>();
        ResultSet rs = null;
        try {
            DatabaseMetaData dbMeta = connection.getMetaData();
            rs = dbMeta.getSchemas();
            while (rs.next()) {
                list.add(rs.getString("table_schem"));
            }
        } catch (SQLException e) {
            throw new SQLException("getSchemas exception：" + e.getMessage());
        } finally {
            DbEx.release(rs);
        }
        return list;
    }

    /**
     * 获取表字段信息
     *
     * @param connection 连接
     * @param tableName  表名
     */
    public static List<TableInfo> getTableInfo(Connection connection, String tableName) throws SQLException {
        Statement stmt = null;
        ResultSet result = null;
        List<TableInfo> list = new ArrayList<>();
        try {
            stmt = connection.createStatement();
            result = stmt.executeQuery(String.format("SELECT * FROM %s WHERE 1=1 ", convertName(tableName, connection)));
            Set<String> primaryKeySet = DbEx.getPrimaryKey(connection, tableName).keySet();
            ResultSetMetaData meta = result.getMetaData();
            int columnCount = meta.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = meta.getColumnName(i);
                String[] className = meta.getColumnClassName(i).split("\\.");
                TableInfo model = new TableInfo();
                model.setColumn_name(columnName);
                model.setJava_type(className[className.length - 1]);
                model.setDb_type(meta.getColumnTypeName(i));
                model.setIs_null(meta.isNullable(i));
                if (primaryKeySet.contains(columnName)) {
                    model.setIs_primary(1);
                } else {
                    model.setIs_primary(0);
                }
                list.add(model);
            }
            return list;
        } catch (Exception e) {
            DbEx.release(stmt, result);
            throw new SQLException("getTableInfo exception：" + e.getMessage());
        } finally {
            DbEx.release(stmt, result);
        }
    }

    /**
     * 获取表的主键
     *
     * @param connection 连接
     * @param tableName  表名
     */
    public static Map<String, String> getPrimaryKey(Connection connection, String tableName) throws SQLException {
        Map<String, String> map = new HashMap<>();
        ResultSet rs = null;
        try {
            DatabaseMetaData dbMeta = connection.getMetaData();
            rs = dbMeta.getPrimaryKeys(null, null, tableName);
            while (rs.next()) {
                map.put(rs.getString("COLUMN_NAME"), "");
            }

        } catch (SQLException e) {
            throw new SQLException("getPrimaryKey exception：" + e.getMessage());
        } finally {
            DbEx.release(rs);
        }
        return map;
    }

    /**
     * 根据sql查询记录条数
     *
     * @param connection 连接
     * @param sql        sql
     */
    public static int getCount(Connection connection, String sql) throws SQLException {
        Statement stmt = null;
        ResultSet result = null;
        int count = 0;
        try {
            int index = sql.indexOf("ORDER BY");
            //如果有排序字段，去掉排序之后的语句
            if (index > 0) {
                sql = sql.substring(0, index);
            }
            //如果是select * from table,直接替换
            if (sql.contains("*")) {
                sql = sql.replace("*", " COUNT(0) ");
            }
            //如果是select a,b,c from table，需要找到中间的字段语句然后替换
            else {
                int selectIndex = sql.indexOf("SELECT");
                int fromIndex = sql.indexOf("FROM");
                String str = sql.substring(selectIndex + 6, fromIndex);
                sql = sql.replace(str, " COUNT(0) ");
            }
            stmt = connection.createStatement();
            result = stmt.executeQuery(sql);
            if (result.next()) {
                count = result.getInt(1);
            }
        } catch (Exception e) {
            throw new SQLException("getCount exception：" + e.getMessage());
        } finally {
            release(stmt, result);
        }
        return count;
    }

    /**
     * 根据数据库类型转换表名或字段名
     *
     * @param tableName 表名
     * @param dbType    数据库类型
     */
    public static String convertName(String tableName, DbTypeEnum dbType) {
        switch (dbType) {
            case ClickHouse:
            case MySql:
                return "`" + tableName + "`";
            case MsSql:
                return "[" + tableName + "]";
            case PostgreSql:
                return "\"" + tableName + "\"";
            default:
                return tableName;
        }
    }

    /**
     * 根据数据连接类型转换表名或字段名
     *
     * @param tableName  表名
     * @param connection 数据库连接
     */
    public static String convertName(String tableName, Connection connection) throws SQLException {
        try {
            String driverName = connection.getMetaData().getDriverName().toUpperCase();
            if (driverName.contains("MYSQL")) {
                return convertName(tableName, DbTypeEnum.MySql);
            }
            if (driverName.contains("SQL SERVER")) {
                return convertName(tableName, DbTypeEnum.MsSql);
            } else if (driverName.contains("POSTGRESQL")) {
                return convertName(tableName, DbTypeEnum.PostgreSql);
            } else if (driverName.contains("CLICKHOUSE")) {
                return convertName(tableName, DbTypeEnum.ClickHouse);
            }
        } catch (SQLException e) {
            throw new SQLException("convertName exception：" + e.getMessage());
        }
        return tableName;
    }

    /**
     * 释放资源
     *
     * @param connection 数据库链接
     * @param stmt       声明
     * @param rs         结果集
     */
    public static void release(Connection connection, Statement stmt, ResultSet rs) throws SQLException {
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
            throw new SQLException("release exception：" + e.getMessage());
        }
    }

    /**
     * 释放资源
     *
     * @param connection 数据库链接
     * @param stmt       声明
     */
    public static void release(Connection connection, Statement stmt) throws SQLException {
        release(connection, stmt, null);
    }

    /**
     * 释放资源
     *
     * @param connection 链接
     */
    public static void release(Connection connection) throws SQLException {
        release(connection, null, null);
    }

    /**
     * 释放资源
     *
     * @param stmt 声明
     */
    public static void release(Statement stmt) throws SQLException {
        release(null, stmt, null);
    }

    /**
     * 释放资源
     *
     * @param stmt 声明
     * @param rs   结果集
     */
    public static void release(Statement stmt, ResultSet rs) throws SQLException {
        release(null, stmt, rs);
    }

    /**
     * 释放资源
     *
     * @param rs 结果集
     */
    public static void release(ResultSet rs) throws SQLException {
        release(null, null, rs);
    }

    /**
     * 设置参数
     *
     * @param pstm  参数
     * @param index 序号
     * @param val   值
     * @param type  java类型
     */
    public static void setParam(PreparedStatement pstm, int index, Object val, String type) throws SQLException {
        try {
            type = type.toUpperCase();
            switch (type) {
                case "STRING":
                case "INT":
                case "LONG":
                case "INTEGER":
                case "DOUBLE":
                case "FLOAT":
                case "DATE":
                case "TIME":
                case "DATETIME":
                case "TIMESTAMP":
                case "BIGDECIMAL":
                case "LOCALDATETIME":
                    setParam(pstm, index, val.toString(), type);
                    break;
                default:
                    pstm.setObject(index, val);
                    break;
            }
        } catch (SQLException e) {
            throw new SQLException("setParam exception：" + e.getMessage());
        }
    }

    /**
     * 设置参数
     *
     * @param pstm  参数
     * @param index 序号
     * @param val   值
     * @param type  java类型
     */
    public static void setParam(PreparedStatement pstm, int index, String val, String type) throws SQLException {
        try {
            type = type.toUpperCase();
            switch (type) {
                case "INT":
                case "INTEGER":
                    pstm.setInt(index, Integer.parseInt(val));
                    break;
                case "LONG":
                    pstm.setLong(index, Long.parseLong(val));
                    break;
                case "STRING":
                    pstm.setString(index, val);
                    break;
                case "DOUBLE":
                    pstm.setDouble(index, Double.parseDouble(val));
                    break;
                case "FLOAT":
                    pstm.setFloat(index, Float.parseFloat(val));
                    break;
                case "DATETIME":
                case "TIMESTAMP":
                    pstm.setTimestamp(index, Timestamp.valueOf(val));
                    break;
                case "DATE":
                    pstm.setDate(index, Date.valueOf(val));
                    break;
                case "TIME":
                    pstm.setTime(index, Time.valueOf(val));
                    break;
                case "BIGDECIMAL":
                    pstm.setBigDecimal(index, new BigDecimal(val));
                    break;
                case "LOCALDATETIME":
                    pstm.setObject(index, DateUtils.strToDate(val));
                    break;
                default:
                    pstm.setObject(index, val);
                    break;
            }
        } catch (SQLException e) {
            throw new SQLException("setParam exception：" + e.getMessage());
        }
    }
}
