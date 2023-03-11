package com.wbs.common.database;

import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.common.database.base.model.ResultEnum;
import com.wbs.common.database.base.model.TableInfo;
import com.wbs.common.utils.DateUtils;
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
import java.util.concurrent.Future;

/**
 * @author WBS
 * @date 2023/3/2 16:11
 * @desciption DbUtils
 */
@Component
public class DbUtils {
    private final static Logger logger = LoggerFactory.getLogger("DbUtils");
    @Autowired
    private ThreadPoolTaskExecutor customExecutor;
    private static ThreadPoolTaskExecutor threadPool;

    @PostConstruct
    public void init() {
        threadPool = this.customExecutor;
    }

    /**
     * 获取字段的java类型
     *
     * @param columns    列集合
     * @param columnName 列名
     */
    public static String getColumnJavaType(List<ColumnInfo> columns, String columnName) {
        ColumnInfo column = columns.stream().filter(x -> columnName.equals(x.getName())).findAny().orElse(null);
        if (column != null) {
            return column.getJavaType();
        } else {
            return "";
        }
    }

    /**
     * 获取当前连接库下的所有表
     *
     * @param connection 连接
     */
    public static List<TableInfo> getTables(Connection connection) {
        List<TableInfo> list = new ArrayList<>();
        Statement stmt = null;
        ResultSet resultSet = null;
        String sql = "";
        String db = "";
        try {
            stmt = connection.createStatement();
            String driverName = connection.getMetaData().getDriverName().toUpperCase();
            if (driverName.contains("SQL SERVER")) {
                db = connection.getCatalog();
                sql = "SELECT a.name, b.[value] AS comment, c.rows as total FROM sys.tables AS a LEFT JOIN sys.extended_properties AS b ON a.object_id=b.major_id  AND b.minor_id=0 INNER JOIN sysindexes AS c ON c.id=OBJECT_ID( a.name ) WHERE c.indid<2";
            } else if (driverName.contains("MYSQL")) {
                db = connection.getCatalog();
                sql = "SET SESSION information_schema_stats_expiry=0"; // 临时设置实时刷新
                stmt.execute(sql);
                sql = "SELECT TABLE_NAME as name, TABLE_COMMENT as comment, TABLE_ROWS as total FROM information_schema.TABLES  WHERE TABLE_SCHEMA = '" + db + "' ;";
            } else if (driverName.contains("POSTGRESQL")) {
                db = connection.getCatalog() + "." + connection.getSchema();
                sql = "SELECT relname as name,n_live_tup as total, cast( obj_description ( relid, 'pg_class' ) AS VARCHAR ) AS comment FROM pg_stat_user_tables where schemaname='" + connection.getSchema() + "'  ";
            } else if (driverName.contains("CLICKHOUSE")) {
                db = connection.getSchema();
                sql = "select name,comment,total_rows as total from `system`.tables  where database = '" + db + "'";
            }
            resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                TableInfo model = new TableInfo();
                model.setName(resultSet.getString("name"));
                model.setComment(resultSet.getString("comment"));
                model.setDb(db);
                model.setTotal(resultSet.getInt("total"));
                list.add(model);
            }
        } catch (SQLException e) {
            logger.error("------DbUtils getTables error------", e);
        } finally {
            closeResultSet(resultSet);
            closeStatement(stmt);
        }
        return list;
    }

    /**
     * 获取字段类型，包括名称、备注、数据库类型、java类型
     *
     * @param connection
     * @param tableName
     * @return
     */
    public static List<ColumnInfo> getColumns(Connection connection, String tableName) {
        Future<LinkedHashMap<String, String>> submit1 = threadPool.submit(() -> getColumnJavaType(connection, tableName));
        Future<Set<String>> submit2 = threadPool.submit(() -> getPrimaryKey(connection, tableName));
        List<ColumnInfo> list = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            LinkedHashMap<String, String> javaTypeMap = submit1.get();
            Set<String> primarySet = submit2.get();
            DatabaseMetaData metaData = connection.getMetaData();
            resultSet = metaData.getColumns(connection.getCatalog(), connection.getSchema(), tableName, "%");
            while (resultSet.next()) {
                ColumnInfo model = new ColumnInfo();
                model.setName(resultSet.getString(ResultEnum.COLUMN_NAME.name()));
                model.setComment(resultSet.getString(ResultEnum.REMARKS.name()));
                model.setTable(tableName);
                model.setPoint(resultSet.getInt(ResultEnum.DECIMAL_DIGITS.name()));
                model.setDbType(resultSet.getString(ResultEnum.TYPE_NAME.name()));
                model.setJavaType(javaTypeMap.get(model.getName()));
                // 是否可空
                if (resultSet.getString(ResultEnum.IS_NULLABLE.name()).equals("YES")) {
                    model.setIsNullAble(1);
                } else {
                    model.setIsNullAble(0);
                }
                // 是否为主键
                if (primarySet.contains(model.getName())) {
                    model.setPrimary(1);
                } else {
                    model.setPrimary(0);
                }
                list.add(model);
            }
        } catch (Exception e) {
            logger.error("------DbUtils getTables error------", e);
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
    public  static LinkedHashMap<String, String> getColumnJavaType(Connection connection, String tableName) {
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
            case ClickHouse:
            case MySql:
                return "`" + tableName + "`";
            case SqlServer:
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
                return DbTypeEnum.SqlServer;
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
                    pstm.setTimestamp(index, DateUtils.strToTimestamp(val, DateUtils.getDateFormat(val)));
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
                    pstm.setObject(index, DateUtils.strToDate(val, DateUtils.getDateFormat(val)));
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
