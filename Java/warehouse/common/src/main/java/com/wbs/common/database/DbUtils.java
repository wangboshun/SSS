package com.wbs.common.database;

import com.mysql.cj.jdbc.ConnectionImpl;
import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.common.database.base.model.ResultEnum;
import com.wbs.common.database.base.model.TableInfo;
import com.wbs.common.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author WBS
 * @date 2023/3/2 16:11
 * @desciption DbUtils
 */
@Component
public class DbUtils {
    private static final Logger logger = LoggerFactory.getLogger("DbUtils");
    @Autowired
    private ThreadPoolTaskExecutor defaultExecutor;
    private static ThreadPoolTaskExecutor threadPool;

    @PostConstruct
    public void init() {
        threadPool = this.defaultExecutor;
    }

    public static List<TableInfo> getTables(Connection connection) {
        List<TableInfo> list = new ArrayList<>();
        try {
            CompletableFuture<List<TableInfo>> future1 = CompletableFuture.supplyAsync(() -> getTableBaseInfo(connection), threadPool);
            CompletableFuture<LinkedHashMap<String, Integer>> future2 = CompletableFuture.supplyAsync(() -> getTableRows(connection), threadPool);
            CompletableFuture.allOf(future1, future2);
            list = future1.get();
            LinkedHashMap<String, Integer> rowsMap = future2.get();
            list.forEach(item -> item.setTotal(rowsMap.get(item.getName())));
        } catch (Exception e) {
            logger.error("------DbUtils getTables error------", e);
        }
        return list;
    }

    /**
     * 获取当前连接库下的所有表
     *
     * @param connection 连接
     */
    public static List<TableInfo> getTableBaseInfo(Connection connection) {
        List<TableInfo> list = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            String db = ((ConnectionImpl) connection).getDatabase();
            String schema = connection.getSchema();
            resultSet = connection.getMetaData().getTables(connection.getCatalog(), connection.getSchema(), null, new String[]{"TABLE"});
            while (resultSet.next()) {
                TableInfo model = new TableInfo();
                model.setName(resultSet.getString("TABLE_NAME"));
                model.setComment(resultSet.getString("REMARKS"));
                // sqlserver没有这个字段
                if (getDbType(connection) != DbTypeEnum.SQLSERVER) {
                    model.setType(resultSet.getString("TYPE_NAME"));
                }
                model.setSchema(schema);
                model.setDb(db);
                list.add(model);
            }
        } catch (SQLException e) {
            logger.error("------DbUtils getTableBaseInfo error------", e);
        } finally {
            closeResultSet(resultSet);
        }
        return list;
    }

    /**
     * 获取表记录数
     *
     * @param connection 连接
     */
    public static LinkedHashMap<String, Integer> getTableRows(Connection connection) {
        LinkedHashMap<String, Integer> rowMap = new LinkedHashMap<>();
        Statement stmt = null;
        ResultSet resultSet = null;
        String sql = "";
        try {
            stmt = connection.createStatement();
            String driverName = connection.getMetaData().getDriverName().toUpperCase();
            if (driverName.contains("SQL SERVER")) {
                sql = "SELECT a.name,b.rows AS total FROM sys.tables AS a INNER JOIN sysindexes AS b ON b.id= OBJECT_ID( a.name ) WHERE b.indid<2";
            } else if (driverName.contains("MYSQL")) {
                sql = "SET SESSION information_schema_stats_expiry=0"; // 临时设置实时刷新
                stmt.execute(sql);
                sql = "SELECT TABLE_NAME as name,TABLE_ROWS as total FROM information_schema.TABLES  WHERE TABLE_SCHEMA = '" + connection.getCatalog() + "' ;";
            } else if (driverName.contains("POSTGRESQL")) {
                sql = "SELECT  relname as name,n_live_tup as total FROM pg_stat_user_tables where schemaname='" + connection.getSchema() + "'  ";
            } else if (driverName.contains("CLICKHOUSE")) {
                sql = "select name,total_rows as total from `system`.tables  where database = '" + connection.getSchema() + "'";
            }

            resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                rowMap.put(resultSet.getString("name"), resultSet.getInt("total"));
            }
        } catch (SQLException e) {
            logger.error("------DbUtils getTables error------", e);
        } finally {
            closeResultSet(resultSet);
            closeStatement(stmt);
        }
        return rowMap;
    }

    /**
     * 获取字段类型，包括名称、备注、数据库类型、java类型
     *
     * @param connection
     * @param tableName
     * @return
     */
    public static List<ColumnInfo> getColumns(Connection connection, String tableName) {
        List<ColumnInfo> list = new ArrayList<>();
        try {
            CompletableFuture<LinkedHashMap<String, String>> future1 = CompletableFuture.supplyAsync(() -> getColumnJavaType(connection, tableName), threadPool);
            CompletableFuture<Set<String>> future2 = CompletableFuture.supplyAsync(() -> getPrimaryKey(connection, tableName), threadPool);
            CompletableFuture<List<ColumnInfo>> future3 = CompletableFuture.supplyAsync(() -> getColumnBaseInfo(connection, tableName), threadPool);
            CompletableFuture.allOf(future1, future2, future3);
            LinkedHashMap<String, String> javaTypeMap = future1.get();
            Set<String> primarySet = future2.get();
            list = future3.get();
            list.forEach(item -> {
                if (primarySet.contains(item.getName())) {
                    item.setPrimary(1);
                } else {
                    item.setPrimary(0);
                }
                item.setJavaType(javaTypeMap.get(item.getName()));
            });
        } catch (Exception e) {
            logger.error("------DbUtils getColumns error------", e);
        }
        return list;
    }

    /**
     * 获取表基础信息
     *
     * @param connection
     * @param tableName
     * @return
     */
    private static List<ColumnInfo> getColumnBaseInfo(Connection connection, String tableName) {
        List<ColumnInfo> list = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            resultSet = metaData.getColumns(connection.getCatalog(), connection.getSchema(), tableName, "%");
            while (resultSet.next()) {
                ColumnInfo model = new ColumnInfo();
                model.setName(resultSet.getString(ResultEnum.COLUMN_NAME.name()));
                model.setComment(resultSet.getString(ResultEnum.REMARKS.name()));
                model.setTable(tableName);
                model.setScale(resultSet.getInt(ResultEnum.DECIMAL_DIGITS.name()));
                model.setDbType(resultSet.getString(ResultEnum.TYPE_NAME.name()));
                model.setLenght(resultSet.getInt(ResultEnum.COLUMN_SIZE.name()));
                // 是否可空
                if (resultSet.getString(ResultEnum.IS_NULLABLE.name()).equals("YES")) {
                    model.setIsNullable(1);
                } else {
                    model.setIsNullable(0);
                }
                list.add(model);
            }
        } catch (Exception e) {
            logger.error("------DbUtils getColumnBaseInfo error------", e);
        } finally {
            closeResultSet(resultSet);
        }
        return list;
    }

    /**
     * 获取字段对应Java类型
     *
     * @param connection
     * @param tableName
     * @return
     */
    public static LinkedHashMap<String, String> getColumnJavaType(Connection connection, String tableName) {
        Statement stmt = null;
        ResultSet resultSet = null;
        LinkedHashMap<String, String> columnMap = new LinkedHashMap<>();
        try {
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery(String.format("SELECT * FROM %s WHERE 1=1 ", DbUtils.convertName(tableName, connection)));
            ResultSetMetaData meta = resultSet.getMetaData();
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
            closeResultSet(resultSet);
            closeStatement(stmt);
        }
        return columnMap;
    }

    /**
     * 获取表的主键
     *
     * @param connection
     * @param tableName
     * @return
     */
    public static Set<String> getPrimaryKey(Connection connection, String tableName) {
        Set<String> columnMap = new HashSet<>();
        ResultSet resultSet = null;
        try {
            DatabaseMetaData dbMeta = connection.getMetaData();
            resultSet = dbMeta.getPrimaryKeys(connection.getCatalog(), connection.getSchema(), tableName);
            while (resultSet.next()) {
                columnMap.add(resultSet.getString("COLUMN_NAME"));
            }

        } catch (SQLException e) {
            logger.error("------DbUtils getPrimaryKey error------", e);
        } finally {
            closeResultSet(resultSet);
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
        ResultSet resultSet = null;
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
            resultSet = stmt.executeQuery(sql);
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (Exception e) {
            logger.error("------ReaderAbstract getCount error------", e);
        } finally {
            DbUtils.closeResultSet(resultSet);
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
            case CLICKHOUSE:
            case MYSQL:
                return "`" + tableName + "`";
            case SQLSERVER:
                return "[" + tableName + "]";
            case POSTGRESQL:
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
            String driverName = DriverManager.getDriver(connection.getMetaData().getURL()).getClass().getName().toLowerCase();
            if (driverName.contains("mysql")) {
                return DbTypeEnum.MYSQL;
            } else if (driverName.contains("sqlserver")) {
                return DbTypeEnum.SQLSERVER;
            } else if (driverName.contains("postgresql")) {
                return DbTypeEnum.POSTGRESQL;
            } else if (driverName.contains("clickhouse")) {
                return DbTypeEnum.CLICKHOUSE;
            }
        } catch (SQLException e) {
            logger.error("------DbUtils getDbType error------", e);
        }
        return DbTypeEnum.NONE;
    }

    /**
     * 设置参数
     *
     * @param pstm     参数
     * @param index    序号
     * @param val      值
     * @param javaType java类型
     */
    public static void setParam(PreparedStatement pstm, int index, Object val, String javaType) throws Exception {
        try {
            javaType = javaType.toUpperCase();
            switch (javaType) {
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
                    setParam(pstm, index, val.toString(), javaType);
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
     * @param pstm     参数
     * @param index    序号
     * @param val      值
     * @param javaType java类型
     */
    public static void setParam(PreparedStatement pstm, int index, String val, String javaType) throws Exception {
        try {
            javaType = javaType.toUpperCase();
            switch (javaType) {
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
                    pstm.setTimestamp(index, TimeUtils.strToTimestamp(val, TimeUtils.getDateFormat(val)));
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
                    pstm.setObject(index, TimeUtils.strToDate(val, TimeUtils.getDateFormat(val)));
                    break;
                default:
                    pstm.setObject(index, val);
                    break;
            }
        } catch (Exception e) {
            throw new Exception("setParam exception：" + e.getMessage());
        }
    }

    /**
     * 构建插入语句
     */
    public static String buildInsertSql(String tableName, List<ColumnInfo> columnList, DbTypeEnum dbType) {
        StringBuilder columnSql = new StringBuilder();
        StringBuilder valueSql = new StringBuilder();
        for (ColumnInfo col : columnList) {
            columnSql.append(DbUtils.convertName(col.getName(), dbType)).append(",");
            valueSql.append("?,");
        }
        columnSql.deleteCharAt(columnSql.length() - 1);
        valueSql.deleteCharAt(valueSql.length() - 1);
        return String.format("INSERT INTO %s (%s) VALUES (%s)", DbUtils.convertName(tableName, dbType), columnSql, valueSql);
    }

    /**
     * 构建更新语句
     */
    public static String buildUpdateSql(String tableName, List<ColumnInfo> columnList, Set<String> primarySet, DbTypeEnum dbType) {
        StringBuilder columnSql = new StringBuilder();
        StringBuilder primarySql = new StringBuilder();
        for (ColumnInfo col : columnList) {
            String columnName = col.getName();
            // 主键
            if (primarySet.contains(columnName)) {
                primarySql.append(DbUtils.convertName(columnName, dbType)).append("=?");
                primarySql.append(" AND ");
            }
            // 非主键
            else {
                columnSql.append(DbUtils.convertName(columnName, dbType)).append("=?");
                columnSql.append(",");
            }
        }
        columnSql.deleteCharAt(columnSql.length() - 1);
        primarySql.delete(primarySql.length() - 4, primarySql.length());
        return String.format("UPDATE %s SET %s WHERE %s", DbUtils.convertName(tableName, dbType), columnSql, primarySql);
    }

    /**
     * 构建字段位置信息，用于更新用
     */
    public static Map<String, Integer> buildColumnSql(List<ColumnInfo> columnList, Set<String> primarySet) {
        Integer paramIndex = 1;
        Map<String, Integer> columnSort = new HashMap<String, Integer>();
        for (ColumnInfo col : columnList) {
            String columnName = col.getName();
            if (primarySet.contains(columnName)) {
                continue;
            }
            columnSort.put(columnName, paramIndex);
            paramIndex++;
        }

        for (ColumnInfo col : columnList) {
            String columnName = col.getName();
            if (primarySet.contains(columnName)) {
                columnSort.put(columnName, paramIndex);
                paramIndex++;
            }
        }
        return columnSort;
    }
}
