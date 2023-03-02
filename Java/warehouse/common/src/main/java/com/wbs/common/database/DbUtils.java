package com.wbs.common.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;
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
        try {
            String driverName = connection.getMetaData().getDriverName().toUpperCase();
            if ("MySql".contains(driverName)) {
                return "`" + tableName + "`";
            } else if ("SqlServer".contains(driverName)) {
                return "[" + tableName + "]";
            } else if ("ClickHouse".contains(driverName)) {

            } else if ("PostgreSql".contains(driverName)) {
                return "\"" + tableName + "\"";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tableName;
    }
}
