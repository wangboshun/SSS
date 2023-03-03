package com.wbs.engine.core.base;

import com.wbs.common.database.DbTypeEnum;
import com.wbs.common.database.DbUtils;
import com.wbs.engine.model.DataRow;
import com.wbs.engine.model.DataTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * @date 2023/3/2 15:46
 * @desciption WriterAbstract
 */
@Component
public abstract class WriterAbstract implements IWriter {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Connection connection;
    private DbTypeEnum dbType;
    private String tableName;
    private Map<String, String> columns;
    private List<String> primaryColumns;

    @Override
    public void config(String tableName, Connection connection) {
        config(tableName, connection, DbUtils.getColumns(connection, tableName));
    }

    @Override
    public void config(String tableName, Connection connection, Map<String, String> columns) {
        this.connection = connection;
        this.tableName = tableName;
        this.dbType = DbUtils.getDbType(connection);
        this.columns = columns;
        this.primaryColumns = DbUtils.getPrimaryKey(connection, tableName);
    }

    @Override
    public boolean writeData(DataTable dt) {
        PreparedStatement pstm = null;
        try {
            StringBuilder columnSql = new StringBuilder();
            StringBuilder valueSql = new StringBuilder();
            for (Map.Entry<String, String> entry : this.columns.entrySet()) {
                String key = entry.getKey();
                columnSql.append(DbUtils.convertName(key, dbType)).append(",");
                valueSql.append("?,");
            }

            columnSql.deleteCharAt(columnSql.length() - 1);
            valueSql.deleteCharAt(valueSql.length() - 1);
            String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", DbUtils.convertName(tableName, dbType), columnSql, valueSql);
            this.connection.setAutoCommit(false);
            pstm = connection.prepareStatement(sql);
            for (DataRow row : dt) {
                int index = 1;
                for (Map.Entry<String, String> entry : this.columns.entrySet()) {
                    DbUtils.setParam(pstm, index, row.get(entry.getKey()), entry.getValue());
                    index++;
                }
                pstm.addBatch();
            }
            pstm.executeBatch();
            pstm.clearBatch();
            connection.commit();
        } catch (Exception e) {
            logger.error("------WriterAbstract writeData error------", e);
            return false;
        } finally {
            DbUtils.closeStatement(pstm);
        }
        return true;
    }

    @Override
    public boolean updateData(DataTable dt) {
        if (primaryColumns.isEmpty()) {
            throw new RuntimeException("该表没有主键，无法更新！");
        }
        PreparedStatement pstm = null;
        try {
            StringBuilder columnSql = new StringBuilder();
            StringBuilder whereSql = new StringBuilder();
            for (Map.Entry<String, String> entry : this.columns.entrySet()) {
                String column = entry.getKey();
                // 主键
                if (this.primaryColumns.contains(column)) {
                    whereSql.append(DbUtils.convertName(column, dbType)).append("=?");
                    whereSql.append(" AND ");
                }
                // 非主键
                else {
                    columnSql.append(DbUtils.convertName(column, dbType)).append("=?");
                    columnSql.append(",");
                }
            }
            columnSql.deleteCharAt(columnSql.length() - 1);
            whereSql.delete(whereSql.length() - 4, whereSql.length());
            String sql = String.format("UPDATE %s SET %s WHERE %s", DbUtils.convertName(tableName, dbType), columnSql, whereSql);
            this.connection.setAutoCommit(false);
            pstm = connection.prepareStatement(sql);
            for (DataRow row : dt) {
                int index = 1;
                // 这里做了特殊处理，因为sql语句中非主键的参数在前面，所以先把非主键和参数先封装进去
                for (Map.Entry<String, String> entry : this.columns.entrySet()) {
                    String column = entry.getKey();
                    String javaType = entry.getValue();
                    if (this.primaryColumns.contains(column)) {
                        continue;
                    }
                    DbUtils.setParam(pstm, index, row.get(column), javaType);
                    index++;
                }
                for (String column : primaryColumns) {
                    DbUtils.setParam(pstm, index, row.get(column), this.columns.get(column));
                    index++;
                }
                pstm.addBatch();
            }
            pstm.executeBatch();
            pstm.clearBatch();
            connection.commit();
        } catch (Exception e) {
            logger.error("SinkAbstract updateData", e);
        } finally {
            DbUtils.closeStatement(pstm);
        }
        return true;
    }

    /**
     * 查询数据是否存在
     *
     * @param row 数据
     */
    public boolean exists(DataRow row) {
        tableName = DbUtils.convertName(tableName, dbType);
        int number = 0;
        ResultSet resultSet = null;
        PreparedStatement pstm = null;
        try {
            String sql = "";
            StringBuilder whereSql = new StringBuilder(" WHERE ");
            for (String column : primaryColumns) {
                whereSql.append(DbUtils.convertName(column, dbType)).append("=?");
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
            for (String column : primaryColumns) {
                DbUtils.setParam(pstm, index, row.get(column), this.columns.get(column));
                index++;
            }

            resultSet = pstm.executeQuery();
            while (resultSet.next()) {
                number = resultSet.getInt("number");
            }

            if (number > 0) {
                return true;
            }
        } catch (Exception e) {
            logger.error("exist ", e);
        } finally {
            DbUtils.closeResultSet(resultSet);
            DbUtils.closeStatement(pstm);
        }
        return false;
    }
}
