package com.wbs.engine.core.base;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.crypto.SecureUtil;
import com.mysql.cj.jdbc.ConnectionImpl;
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
    protected List<ColumnInfo> columnList;
    private Set<String> primarySet;
    Cache<String, List<ColumnInfo>> fifoCache = CacheUtil.newFIFOCache(100);

    @Override
    public void config(String tableName, Connection connection) {
        String url = ((ConnectionImpl) connection).getURL();
        String md5 = SecureUtil.md5(url + "_" + tableName);
        List<ColumnInfo> columns = fifoCache.get(md5);
        if (columnList==null) {
            columns = DbUtils.getColumns(connection, tableName);
            fifoCache.put(md5, columns);
        }
        config(tableName, connection, columns);
    }

    @Override
    public void config(String tableName, Connection connection, List<ColumnInfo> columnList) {
        this.connection = connection;
        this.tableName = tableName;
        this.columnList = columnList;
        this.primarySet = this.columnList.stream().filter(x -> x.getPrimary() == 1).map(ColumnInfo::getName).collect(Collectors.toSet());
    }

    @Override
    public boolean writeData(DataTable dt) {
        PreparedStatement pstm = null;
        try {
            StringBuilder columnSql = new StringBuilder();
            StringBuilder valueSql = new StringBuilder();
            for (ColumnInfo col : this.columnList) {
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
                for (ColumnInfo col : this.columnList) {
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
        if (primarySet.isEmpty()) {
            throw new RuntimeException("该表没有主键，无法更新！");
        }
        PreparedStatement pstm = null;
        try {
            StringBuilder columnSql = new StringBuilder();
            StringBuilder whereSql = new StringBuilder();
            for (ColumnInfo col : this.columnList) {
                String columnName = col.getName();
                // 主键
                if (this.primarySet.contains(columnName)) {
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
                for (ColumnInfo col : this.columnList) {
                    String columnName = col.getName();
                    if (this.primarySet.contains(columnName)) {
                        continue;
                    }
                    DbUtils.setParam(pstm, index, row.get(columnName), col.getJavaType());
                    index++;
                }

                for (ColumnInfo col : this.columnList) {
                    String columnName = col.getName();
                    if (this.primarySet.contains(columnName)) {
                        DbUtils.setParam(pstm, index, row.get(columnName), col.getJavaType());
                        index++;
                    }
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
            for (String columnName : primarySet) {
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
            for (String columnName : primarySet) {
                String javaType = this.columnList.stream().filter(x -> x.getName().equals(columnName)).findFirst().get().getJavaType();
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
