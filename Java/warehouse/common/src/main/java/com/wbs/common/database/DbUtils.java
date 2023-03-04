package com.wbs.common.database;

import com.wbs.common.database.model.ColumnInfo;
import com.wbs.common.database.model.TableInfo;
import com.wbs.common.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * @author WBS
 * @date 2023/3/2 16:11
 * @desciption DbUtils
 */
public class DbUtils {
    private final static Logger logger = LoggerFactory.getLogger("DbUtils");

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
            String driverName = connection.getMetaData().getDriverName().toUpperCase();
            if (driverName.contains("SQL SERVER")) {
                db = connection.getCatalog();
                sql = "SELECT a.name, b.[value] AS comment, c.rows as total FROM sys.tables AS a LEFT JOIN sys.extended_properties AS b ON a.object_id = b.major_id  AND b.minor_id= 0 INNER JOIN sysindexes AS c ON c.id= OBJECT_ID( a.name ) WHERE c.indid < 2";
            } else if (driverName.contains("MYSQL")) {
                db = connection.getCatalog();
                sql = "SELECT TABLE_NAME as name, TABLE_COMMENT as comment, TABLE_ROWS as total FROM information_schema.TABLES  WHERE TABLE_SCHEMA = '" + db + "' ;";
            } else if (driverName.contains("POSTGRESQL")) {
                db = connection.getCatalog() + "." + connection.getSchema();
                sql = "SELECT relname as name,n_live_tup as total, cast( obj_description ( relid, 'pg_class' ) AS VARCHAR ) AS comment FROM pg_stat_user_tables where schemaname='" + connection.getSchema() + "'  ";
            } else if (driverName.contains("CLICKHOUSE")) {
                db = connection.getSchema();
                sql = "select name,comment,total_rows as total from `system`.tables  where database = '" + db + "'";
            }
            stmt = connection.createStatement();
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

    public static List<ColumnInfo> getColumn(Connection connection, String tableName) {
        String sql = "select name,type,comment,is_in_primary_key as primary from system.columns where table='users'\n";
        List<ColumnInfo> list = new ArrayList<>();
        List<String> pgsqlPrimarys = new ArrayList<>();
        Statement stmt = null;
        ResultSet resultSet = null;
        try {
            String driverName = connection.getMetaData().getDriverName().toUpperCase();
            if (driverName.contains("SQL SERVER")) {
                sql = "SELECT name=A.NAME, [primary]=CASE   WHEN EXISTS (  SELECT   1   FROM   SYSOBJECTS   WHERE   XTYPE = 'PK'    AND PARENT_OBJ = A.ID    AND NAME IN ( SELECT NAME FROM SYSINDEXES WHERE INDID IN ( SELECT INDID FROM SYSINDEXKEYS WHERE ID = A.ID AND COLID = A.COLID ) )    ) THEN   1 ELSE 0   END,  type = B.NAME,  comment = ISNULL( G.[VALUE], '' )  FROM  SYSCOLUMNS A  LEFT JOIN SYSTYPES B ON A.XUSERTYPE= B.XUSERTYPE  INNER JOIN SYSOBJECTS D ON A.ID= D.ID     LEFT JOIN SYSCOMMENTS E ON A.CDEFAULT= E.ID  LEFT JOIN sys.extended_properties G ON A.ID= G.major_id   AND A.COLID= G.minor_id  WHERE D.NAME= '" + tableName + "'";
                // TODO
            } else if (driverName.contains("MYSQL")) {
                sql = "SELECT COLUMN_COMMENT AS 'comment',CASE  COLUMN_KEY   WHEN 'PRI' THEN  1 ELSE 0  END AS 'primary', COLUMN_NAME AS NAME, DATA_TYPE AS type FROM information_schema.COLUMNS WHERE table_name = 'iot_data'";
            } else if (driverName.contains("POSTGRESQL")) {
                pgsqlPrimarys = getPGSQLPrimary(connection, tableName);
                sql = "SELECT A.attname AS name, T.typname AS type, b.description AS comment  FROM  pg_namespace n  LEFT JOIN pg_class C ON n.OID = C.relnamespace  LEFT JOIN pg_attribute A ON A.attrelid = C.  OID LEFT JOIN pg_description b ON A.attrelid = b.objoid   AND A.attnum = b.objsubid  LEFT JOIN pg_type T ON A.atttypid = T.OID WHERE  n.nspname = 'public'   AND C.relname = '" + tableName + "'   AND A.attnum > 0";
            } else if (driverName.contains("CLICKHOUSE")) {
                sql = "select name,type,comment,is_in_primary_key as primary from system.columns where table='" + tableName + "'";
            }
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                ColumnInfo model = new ColumnInfo();
                model.setName(resultSet.getString("name"));
                model.setComment(resultSet.getString("comment"));
                model.setTable(tableName);
                model.setType(resultSet.getString("type"));
                if (driverName.contains("POSTGRESQL")) {
                    if (pgsqlPrimarys.contains(model.getName())) {
                        model.setPrimary(1);
                    } else {
                        model.setPrimary(0);
                    }
                } else {
                    model.setPrimary(resultSet.getInt("primary"));
                }
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
     * 获取pgsql的主键，特殊处理
     *
     * @return
     */
    private static List<String> getPGSQLPrimary(Connection connection, String tableName) {
        List<String> list = new ArrayList<>();
        Statement stmt = null;
        ResultSet resultSet = null;
        try {
            String sql = "SELECT   t3.attname as primary FROM   pg_constraint t1   INNER JOIN pg_class t2 ON t1.conrelid = t2.   OID INNER JOIN pg_attribute t3 ON t3.attrelid = t2.OID    AND array_position ( t1.conkey, t3.attnum )   IS NOT NULL INNER JOIN pg_tables t4 ON t4.tablename = t2.relname WHERE   t1.contype = 'p'    AND t2.OID = '" + tableName + "' :: REGCLASS;";
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                list.add(resultSet.getString("primary"));
            }
        } catch (SQLException e) {
            logger.error("------DbUtils getPGSQLPrimary error------", e);
        } finally {
            closeResultSet(resultSet);
            closeStatement(stmt);
        }
        return list;
    }

    /**
     * 获取列名和列类型
     *
     * @param connection
     * @param tableName
     * @return
     */
    public static LinkedHashMap<String, String> getColumns(Connection connection, String tableName) {
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

    public static Set<String> getPrimaryKey(Connection connection, String tableName) {
        Set<String> columnMap = new HashSet<>();
        ResultSet resultSet = null;
        try {
            DatabaseMetaData dbMeta = connection.getMetaData();
            resultSet = dbMeta.getPrimaryKeys(null, null, tableName);
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
