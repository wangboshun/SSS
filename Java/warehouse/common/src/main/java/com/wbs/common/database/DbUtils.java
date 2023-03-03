package com.wbs.common.database;

import com.wbs.common.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * @date 2023/3/2 16:11
 * @desciption DbUtils
 */
public class DbUtils {
    private final static Logger logger = LoggerFactory.getLogger("DbUtils");

    /**
     * 获取列名和列类型
     *
     * @param connection
     * @param tableName
     * @return
     */
    public static Map<String, String> getColumns(Connection connection, String tableName) {
        Statement stmt = null;
        ResultSet result = null;
        Map<String, String> columnMap = new HashMap<>();
        try {
            stmt = connection.createStatement();
            result = stmt.executeQuery(String.format("SELECT * FROM %s WHERE 1=1 ", DbUtils.convertName(tableName, connection)));
            ResultSetMetaData meta = result.getMetaData();
            int columnCount = meta.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = meta.getColumnName(i);
                String[] className = meta.getColumnClassName(i).split("\\.");
                String type = className[className.length - 1];
                columnMap.put(columnName, type);
            }
            return columnMap;
        } catch (Exception e) {
            logger.error("------DbUtils getColumns error------", e);
        } finally {
            closeResultSet(result);
            closeStatement(stmt);
        }
        return columnMap;
    }

    public static List<String> getPrimaryKey(Connection connection, String tableName) {
        List<String> columnMap = new ArrayList<>();
        ResultSet result = null;
        try {
            DatabaseMetaData dbMeta = connection.getMetaData();
            result = dbMeta.getPrimaryKeys(null, null, tableName);
            while (result.next()) {
                columnMap.add(result.getString("COLUMN_NAME"));
            }

        } catch (SQLException e) {
            logger.error("------DbUtils getPrimaryKey error------", e);
        } finally {
            closeResultSet(result);
        }
        return columnMap;
    }

    /**
     * 统计
     *
     * @param connection
     * @param sql
     * @return
     */
    public static int getCount(Connection connection, String sql) {
        Statement stmt = null;
        ResultSet result = null;
        int count = 0;
        try {
            int index = sql.indexOf("ORDER BY");
            // 如果有排序字段，去掉排序之后的语句
            if (index > 0) {
                sql = sql.substring(0, index);
            }
            // 如果是select * from table,直接替换
            if (sql.contains("*")) {
                sql = sql.replace("*", " COUNT(0) ");
            }
            // 如果是select a,b,c from table，需要找到中间的字段语句然后替换
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
            logger.error("------ReaderAbstract getCount error------", e);
        } finally {
            DbUtils.closeResultSet(result);
            DbUtils.closeStatement(stmt);
        }
        return count;
    }

    public static void closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            logger.error("------DbUtils close resultSet error------", e);
        }
    }

    public static void closeStatement(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            logger.error("------DbUtils close statement error------", e);
        }
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.error("------DbUtils close connection error------", e);
        }
    }

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

    public static String convertName(String tableName, Connection connection) {
        DbTypeEnum type = getDbType(connection);
        return convertName(tableName, type);
    }

    public static DbTypeEnum getDbType(Connection connection) {
        try {
            String driverName = connection.getMetaData().getDriverName().toLowerCase();
            if (driverName.contains("mysql")) {
                return DbTypeEnum.MySql;
            } else if (driverName.contains("sqlserver")) {
                return DbTypeEnum.MsSql;
            } else if (driverName.contains("postgresql")) {
                return DbTypeEnum.PostgreSql;
            } else if (driverName.contains("clickhouse")) {
                return DbTypeEnum.ClickHouse;
            }
        } catch (SQLException e) {
            logger.error("------DbUtils getDbType error------", e);
        }
        return DbTypeEnum.None;
    }

    /**
     * 设置参数
     *
     * @param pstm  参数
     * @param index 序号
     * @param val   值
     * @param type  java类型
     */
    public static void setParam(PreparedStatement pstm, int index, Object val, String type) throws Exception {
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
        } catch (Exception e) {
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
    public static void setParam(PreparedStatement pstm, int index, String val, String type) throws Exception {
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
                    String format = DateUtils.DATE_FORMAT;
                    // 只有T
                    if (val.contains("T") && !val.contains("Z") && !val.contains(".")) {
                        format = "yyyy-MM-dd'T'HH:mm:ss";
                    }
                    // 有T、有Z
                    else if (val.contains("T") && val.contains("Z") && !val.contains(".")) {
                        format = "yyyy-MM-dd'T'HH:mm:ss'Z'";
                    }
                    // 有T、有毫秒
                    else if (val.contains("T") && !val.contains("Z") && val.contains(".")) {
                        format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
                    }
                    // 有T、有Z、有毫秒
                    else if (val.contains("T") && val.contains("Z") && val.contains(".")) {
                        format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
                    }
                    // 只有毫秒
                    else if (!val.contains("T") && !val.contains("Z") && val.contains(".")) {
                        format = "yyyy-MM-dd HH:mm:ss.SSS";
                    }
                    pstm.setObject(index, DateUtils.strToDate(val, format));
                    break;
                default:
                    pstm.setObject(index, val);
                    break;
            }
        } catch (Exception e) {
            throw new Exception("setParam exception：" + e.getMessage());
        }
    }
}
