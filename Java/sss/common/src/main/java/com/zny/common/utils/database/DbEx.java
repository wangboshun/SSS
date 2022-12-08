package com.zny.common.utils.database;

import com.zny.common.enums.DbTypeEnum;

import java.sql.*;
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
    public static List<Map<String, String>> getTables(Connection connection) {
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
     * 获取表字段信息
     *
     * @param connection 连接
     * @param tableName  表名
     */
    public static List<TableInfo> getTableInfo(Connection connection, String tableName) {
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

        } catch (Exception e) {
            System.out.println("getTableInfo : " + e.getMessage());
        } finally {
            DbEx.release(stmt, result);
        }
        return list;
    }

    /**
     * 获取表的主键
     *
     * @param connection 连接
     * @param tableName  表名
     */
    public static Map<String, String> getPrimaryKey(Connection connection, String tableName) {
        Map<String, String> map = new HashMap<>();
        ResultSet rs = null;
        try {
            DatabaseMetaData dbMeta = connection.getMetaData();
            rs = dbMeta.getPrimaryKeys(null, null, tableName);
            while (rs.next()) {
                map.put(rs.getString("COLUMN_NAME"), "");
            }

        } catch (SQLException e) {
            System.out.println("getPrimaryKey : " + e.getMessage());
        } finally {
            DbEx.release(rs);
        }
        return map;
    }

    /**
     * 获取表的主键
     *
     * @param tableInfo 表信息
     */
    public static Map<String, String> getPrimaryKey(List<TableInfo> tableInfo) {
        Map<String, String> map = new HashMap<>();
        for (TableInfo item : tableInfo) {
            if (item.getIs_primary() > 0) {
                map.put(item.getColumn_name(), item.getJava_type());
            }
        }
        return map;
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
    public static boolean hasData(Connection connection, String tableName, Map<String, Object> data, Map<String, String> primaryColumn, DbTypeEnum dbType) {
        tableName = convertName(tableName, dbType);
        int number = 0;
        ResultSet result = null;
        PreparedStatement pstm = null;
        try {
            String sql = "";
            StringBuilder whereSql = new StringBuilder(" WHERE ");

            for (Map.Entry<String, String> entry : primaryColumn.entrySet()) {
                whereSql.append(DbEx.convertName(entry.getKey(), dbType)).append("=?");
                //如果是PostgreSQL数据库，需要对日期格式特殊处理，在后面加【::TIMESTAMP】
                if (dbType == DbTypeEnum.PostgreSql && entry.getValue().toUpperCase().equals("TIMESTAMP")) {
                    whereSql.append("::TIMESTAMP ");
                }
                whereSql.append(" AND ");
            }
            whereSql.delete(whereSql.length() - 5, whereSql.length());
            switch (dbType) {
                case MySql:
                    sql = String.format("select 1 as number from %s%s  limit  1 ", tableName, whereSql);
                    break;
                case MsSql:
                    sql = String.format("SELECT TOP 1 1 as number FROM %s%s", tableName, whereSql);
                    break;
                case PostgreSql:
                    sql = String.format("select 1 as number from %s%s  limit  1 ", tableName, whereSql);
                    break;
                case ClickHouse:
                    sql = String.format("SELECT TOP 1 1 as number FROM %s%s", tableName, whereSql);
                    break;
                default:
                    sql = String.format("SELECT TOP 1 1 as number FROM %s%s", tableName, whereSql);
                    break;
            }
            pstm = connection.prepareStatement(sql);
            int index = 1;
            for (Map.Entry<String, String> entry : primaryColumn.entrySet()) {
                String column = entry.getKey();
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
     * 转换表名
     *
     * @param tableName 表名
     * @param dbType    数据库类型
     */
    public static String convertName(String tableName, DbTypeEnum dbType) {
        switch (dbType) {
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
     * 转换表名
     *
     * @param tableName  表名
     * @param connection 数据库连接
     */
    public static String convertName(String tableName, Connection connection) {
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
            System.out.println("convertTabName : " + e.getMessage());
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
