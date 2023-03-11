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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
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
    @Autowired
    private ThreadPoolTaskExecutor customExecutor;
    private static int BATCH_SIZE = 10000;

    @Override
    public void config(String tableName, Connection connection) {
        String url = ((ConnectionImpl) connection).getURL();
        String md5 = SecureUtil.md5(url + "_" + tableName);
        List<ColumnInfo> columns = fifoCache.get(md5);
        if (columnList == null) {
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
        LocalDateTime start = LocalDateTime.now();
        if (dt.size() < BATCH_SIZE) {
            return batchInsert(dt);
        } else {
            List<List<DataRow>> partition = dt.split(10);// 分成10个线程
            CompletableFuture[] array = partition.stream().map(item -> CompletableFuture.runAsync(() -> batchInsert(item), customExecutor).exceptionally(error -> {
                System.out.println(error);
                saveErrorData(item, 1);
                return null;
            })).toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(array).join();
        }
        LocalDateTime end = LocalDateTime.now();
        float tm = Duration.between(start, end).toMillis() / 1000f;
        System.out.println("插入耗时：" + tm);
        return true;
    }

    @Override
    public boolean updateData(DataTable dt) {
        LocalDateTime start = LocalDateTime.now();
        if (primarySet.isEmpty()) {
            throw new RuntimeException("该表没有主键，无法更新！");
        }
        if (dt.size() < BATCH_SIZE) {
            return batchUpdate(dt);
        } else {
            List<List<DataRow>> partition = dt.split(10);// 分成10个线程
            CompletableFuture[] array = partition.stream().map(item -> CompletableFuture.runAsync(() -> batchUpdate(item), customExecutor).exceptionally(error -> {
                System.out.println(error);
                saveErrorData(item, 2);
                return null;
            })).toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(array).join();
        }
        LocalDateTime end = LocalDateTime.now();
        float tm = Duration.between(start, end).toMillis() / 1000f;
        System.out.println("更新耗时：" + tm);
        return true;
    }

    /**
     * 保存错误数据
     *
     * @param rows 数据
     * @param type 1为插入、2为更新
     */
    private void saveErrorData(List<DataRow> rows, int type) {
        DataTable dt = new DataTable();
        rows.forEach(item -> {
            if (type == 1) {
                if (!singleInsert(item)) {
                    dt.add(item);
                }
            } else if (type == 2) {
                if (!singleUpdate(item)) {
                    dt.add(item);
                }
            }
        });
        System.out.println(dt);
    }

    /**
     * 构建插入语句
     *
     * @return
     */
    private String insertSql() {
        StringBuilder columnSql = new StringBuilder();
        StringBuilder valueSql = new StringBuilder();
        for (ColumnInfo col : this.columnList) {
            columnSql.append(DbUtils.convertName(col.getName(), dbType)).append(",");
            valueSql.append("?,");
        }
        columnSql.deleteCharAt(columnSql.length() - 1);
        valueSql.deleteCharAt(valueSql.length() - 1);
        return String.format("INSERT INTO %s (%s) VALUES (%s)", DbUtils.convertName(tableName, dbType), columnSql, valueSql);
    }

    /**
     * 构建更新语句
     *
     * @return
     */
    private String updateSql() {
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
        return String.format("UPDATE %s SET %s WHERE %s", DbUtils.convertName(tableName, dbType), columnSql, whereSql);
    }

    /**
     * 分批写入
     *
     * @param rows
     */
    private boolean batchInsert(List<DataRow> rows) {
        PreparedStatement pstm = null;
        try {
            String sql = insertSql();
            this.connection.setAutoCommit(false);
            pstm = connection.prepareStatement(sql);
            int rowIndex = 0;
            for (DataRow row : rows) {
                int paramIndex = 1;
                for (ColumnInfo col : this.columnList) {
                    DbUtils.setParam(pstm, paramIndex, row.get(col.getName()), col.getJavaType());
                    paramIndex++;
                }
                pstm.addBatch();
                if (rowIndex > 0 && rowIndex % BATCH_SIZE == 0) {
                    pstm.executeBatch();
                    pstm.clearBatch();
                    System.out.println("插入一批:" + rowIndex);
                }
                rowIndex++;
            }
            pstm.executeBatch();
            pstm.clearBatch();
            connection.commit();
        } catch (Exception e) {
            logger.error("------WriterAbstract batchWrite error------", e);
            throw new RuntimeException("插入失败");
        } finally {
            DbUtils.closeStatement(pstm);
        }
        return true;
    }

    /**
     * 单条写入
     *
     * @param row
     * @return
     */
    private boolean singleInsert(DataRow row) {
        PreparedStatement pstm = null;
        try {
            String sql = insertSql();
            pstm = connection.prepareStatement(sql);
            int paramIndex = 1;
            for (ColumnInfo col : this.columnList) {
                DbUtils.setParam(pstm, paramIndex, row.get(col.getName()), col.getJavaType());
                paramIndex++;
            }
            pstm.execute();
        } catch (Exception e) {
            logger.error("------WriterAbstract singleWrite error------", e);
            return false;
        } finally {
            DbUtils.closeStatement(pstm);
        }
        return true;
    }


    /**
     * 分批更新
     *
     * @param rows
     */
    private boolean batchUpdate(List<DataRow> rows) {
        PreparedStatement pstm = null;
        try {
            String sql = updateSql();
            this.connection.setAutoCommit(false);
            pstm = connection.prepareStatement(sql);
            int rowIndex = 0;
            for (DataRow row : rows) {
                int paramIndex = 1;
                // 非主键，这里做了特殊处理，因为sql语句中非主键的参数在前面，所以先把非主键和参数先封装进去
                for (ColumnInfo col : this.columnList) {
                    String columnName = col.getName();
                    if (this.primarySet.contains(columnName)) {
                        continue;
                    }
                    DbUtils.setParam(pstm, paramIndex, row.get(columnName), col.getJavaType());
                    paramIndex++;
                }
                // 主键
                for (ColumnInfo col : this.columnList) {
                    String columnName = col.getName();
                    if (this.primarySet.contains(columnName)) {
                        DbUtils.setParam(pstm, paramIndex, row.get(columnName), col.getJavaType());
                        paramIndex++;
                    }
                }
                pstm.addBatch();
                if (rowIndex > 0 && rowIndex % BATCH_SIZE == 0) {
                    pstm.executeBatch();
                    pstm.clearBatch();
                    System.out.println("更新一批:" + rowIndex);
                }
                rowIndex++;
            }
            pstm.executeBatch();
            pstm.clearBatch();
            connection.commit();
        } catch (Exception e) {
            logger.error("WriterAbstract batchUpdate", e);
            throw new RuntimeException("更新失败");
        } finally {
            DbUtils.closeStatement(pstm);
        }
        return true;
    }

    /**
     * 单条更新
     *
     * @param row
     * @return
     */
    private boolean singleUpdate(DataRow row) {
        PreparedStatement pstm = null;
        try {
            String sql = updateSql();
            this.connection.setAutoCommit(false);
            pstm = connection.prepareStatement(sql);
            int paramIndex = 1;
            // 这里做了特殊处理，因为sql语句中非主键的参数在前面，所以先把非主键和参数先封装进去
            for (ColumnInfo col : this.columnList) {
                String columnName = col.getName();
                if (this.primarySet.contains(columnName)) {
                    continue;
                }
                DbUtils.setParam(pstm, paramIndex, row.get(columnName), col.getJavaType());
                paramIndex++;
            }

            for (ColumnInfo col : this.columnList) {
                String columnName = col.getName();
                if (this.primarySet.contains(columnName)) {
                    DbUtils.setParam(pstm, paramIndex, row.get(columnName), col.getJavaType());
                    paramIndex++;
                }
            }
            pstm.execute();
        } catch (Exception e) {
            logger.error("WriterAbstract singleUpdate", e);
            throw new RuntimeException("更新失败");
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
