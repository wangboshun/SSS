package com.wbs.engine.core.base;

import com.wbs.common.database.DbUtils;
import com.wbs.common.database.base.DataRow;
import com.wbs.common.database.base.DataTable;
import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.database.base.model.ColumnInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author WBS
 * @date 2023/3/2 15:46
 * @desciption WriterAbstract
 */
@Component
public abstract class WriterAbstract implements IWriter {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    protected DbTypeEnum dbType;
    private Connection connection;
    private String tableName;
    private List<ColumnInfo> columns;
    private Set<String> primaryColumns;

    @Override
    public void config(String tableName, Connection connection) {
        config(tableName, connection, DbUtils.getColumns(connection, tableName));
    }

    @Override
    public void config(String tableName, Connection connection, List<ColumnInfo> columns) {
        this.connection = connection;
        this.tableName = tableName;
        this.columns = columns;
        this.primaryColumns = this.columns.stream().filter(x -> x.getPrimary() == 1).map(ColumnInfo::getName).collect(Collectors.toSet());
    }

    @Override
    public boolean writeData(DataTable dt) {
        PreparedStatement pstm = null;
        try {
            StringBuilder columnSql = new StringBuilder();
            StringBuilder valueSql = new StringBuilder();
            for (ColumnInfo col : this.columns) {
                columnSql.append(DbUtils.convertName(col.getName(), dbType)).append(",");
                valueSql.append("?,");
            }

            columnSql.deleteCharAt(columnSql.length() - 1);
            valueSql.deleteCharAt(valueSql.length() - 1);
            String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", DbUtils.convertName(tableName, dbType), columnSql, valueSql);
            this.connection.setAutoCommit(false);
            pstm = connection.prepareStatement(sql);
            for (DataRow row : dt) {
                int index = 1;
                for (ColumnInfo col : this.columns) {
                    DbUtils.setParam(pstm, index, row.get(col.getName()), col.getJavaType());
                    index++;
                }
                pstm.addBatch();
                if (index % 1000 == 0) {
                    pstm.executeBatch();
                    pstm.clearBatch();
                }
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
            for (ColumnInfo col : this.columns) {
                String columnName = col.getName();
                // 主键
                if (this.primaryColumns.contains(columnName)) {
                    whereSql.append(DbUtils.convertName(columnName, dbType)).append("=?");
                    whereSql.append(" AND ");
                }
                // 非主键
                else {
                    columnSql.append(DbUtils.convertName(columnName, dbType)).append("=?");
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
                for (ColumnInfo col : this.columns) {
                    String columnName = col.getName();
                    if (this.primaryColumns.contains(columnName)) {
                        continue;
                    }
                    DbUtils.setParam(pstm, index, row.get(columnName), col.getJavaType());
                    index++;
                }
                for (String columnName : primaryColumns) {
                    String javaType = this.columns.stream().filter(x -> x.getName().equals(columnName)).findFirst().get().getJavaType();
                    DbUtils.setParam(pstm, index, row.get(columnName), javaType);
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
            for (String columnName : primaryColumns) {
                whereSql.append(DbUtils.convertName(columnName, dbType)).append("=?");
                whereSql.append(" AND ");
            }
            whereSql.delete(whereSql.length() - 5, whereSql.length());
            switch (dbType) {
                case MySql:
                    sql = String.format("select 1 as number from %s%s  limit  1 ", tableName, whereSql);
                    break;
                case SqlServer:
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
            for (String columnName : primaryColumns) {
                String javaType = this.columns.stream().filter(x -> x.getName().equals(columnName)).findFirst().get().getJavaType();
                DbUtils.setParam(pstm, index, row.get(columnName), javaType);
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
