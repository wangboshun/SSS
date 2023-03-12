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
import com.wbs.engine.model.WriterResult;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    /**
     * 插入数据
     *
     * @param dt
     * @return 返回错误数据
     */
    @Override
    public WriterResult insertData(DataTable dt) {
        DataTable exceptionData = new DataTable();
        LocalDateTime start = LocalDateTime.now();
        if (dt.size() < BATCH_SIZE) {
            try {
                batchInsert(dt);
            } catch (Exception e) {
                exceptionData.addAll(dt);
            }
        } else {
            List<List<DataRow>> partition = dt.split(10);// 分成10个线程
            CompletableFuture[] array = partition.stream().map(item -> CompletableFuture.runAsync(() -> batchInsert(item), customExecutor).exceptionally(error -> {
                exceptionData.addAll(item);
                return null;
            })).toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(array).join();
        }
        LocalDateTime end = LocalDateTime.now();
        float tm = Duration.between(start, end).toMillis() / 1000f;
        System.out.println("插入耗时：" + tm);
        WriterResult result = builderResult(exceptionData, 1);
        result.setSpend(tm);
        result.setInsertCount(dt.size() - exceptionData.size());
        return result;
    }


    /**
     * 更新数据
     *
     * @param dt
     * @return 返回错误数据
     */
    @Override
    public WriterResult updateData(DataTable dt) {
        DataTable exceptionData = new DataTable();
        LocalDateTime start = LocalDateTime.now();
        if (primarySet.isEmpty()) {
            throw new RuntimeException("该表没有主键，无法更新！");
        }
        if (dt.size() < BATCH_SIZE) {
            try {
                batchUpdate(dt);
            } catch (Exception e) {
                exceptionData.addAll(dt);
            }
        } else {
            List<List<DataRow>> partition = dt.split(10);// 分成10个线程
            CompletableFuture[] array = partition.stream().map(item -> CompletableFuture.runAsync(() -> batchUpdate(item), customExecutor).exceptionally(error -> {
                exceptionData.addAll(item);
                return null;
            })).toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(array).join();
        }
        LocalDateTime end = LocalDateTime.now();
        float tm = Duration.between(start, end).toMillis() / 1000f;
        System.out.println("更新耗时：" + tm);
        WriterResult result = builderResult(exceptionData, 2);
        result.setSpend(tm);
        result.setUpdateCount(dt.size() - exceptionData.size());
        return result;
    }


    /**
     * 批量写入
     *
     * @param rows
     */
    private void batchInsert(List<DataRow> rows) {
        PreparedStatement pstm = null;
        try {
            String sql = buildInsertSql();
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
            String sql = buildInsertSql();
            this.connection.setAutoCommit(true);
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
     * 批量更新
     *
     * @param rows
     */
    private void batchUpdate(List<DataRow> rows) {
        PreparedStatement pstm = null;
        try {
            // 非主键，这里做了特殊处理，因为sql语句中非主键的参数在前面，所以先把非主键和参数先封装进去
            String sql = buildUpdateSql();
            this.connection.setAutoCommit(false);
            pstm = connection.prepareStatement(sql);
            int rowIndex = 0;
            Map<String, Integer> columnSort = buildColumnSort();
            for (DataRow row : rows) {
                for (ColumnInfo col : this.columnList) {
                    String columnName = col.getName();
                    Integer paramIndex = columnSort.get(columnName);
                    DbUtils.setParam(pstm, paramIndex, row.get(columnName), col.getJavaType());
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
            String sql = buildUpdateSql();
            this.connection.setAutoCommit(true);
            pstm = connection.prepareStatement(sql);
            Map<String, Integer> columnSort = buildColumnSort();
            for (ColumnInfo col : this.columnList) {
                String columnName = col.getName();
                Integer paramIndex = columnSort.get(columnName);
                DbUtils.setParam(pstm, paramIndex, row.get(columnName), col.getJavaType());
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
     * 查找已存在数据
     *
     * @param dt
     * @return
     */
    private DataTable findExitsData(DataTable dt) {
        DataTable exitsData = new DataTable();
        for (DataRow row : dt) {
            boolean b = exists(row);
            if (b) {
                exitsData.add(row);
            }
        }
        return exitsData;
    }

    /**
     * 保存错误数据
     *
     * @param dt   数据
     * @param type 1为插入、2为更新
     */
    private DataTable findErrorData(DataTable dt, int type) {
        DataTable result = new DataTable();
        dt.forEach(item -> {
            if (type == 1) {
                if (!singleInsert(item)) {
                    result.add(item);
                }
            } else if (type == 2) {
                if (!singleUpdate(item)) {
                    result.add(item);
                }
            }
        });
        System.out.println(result);
        return result;
    }

    /**
     * 构建返回值
     *
     * @param exceptionData
     * @param type          1为插入，2为更新
     */
    private WriterResult builderResult(DataTable exceptionData, int type) {
        WriterResult result = new WriterResult();
        if (exceptionData.size() < 1) {
            return result;
        }
        DataTable exitsData = null;
        DataTable errorData = null;

        // 查找已存在数据
        exitsData = findExitsData(exceptionData);

        // 如果有已存在数据，去差集
        if (exitsData.size() > 0) {
            result.setExitsData(exitsData);
            exceptionData.removeAll(exitsData);
        }
        if (exceptionData.size() > 0) {
            errorData = findErrorData(exceptionData, type);
            if (errorData.size() > 0) {
                result.setErrorData(errorData);
            }
        }
        return result;
    }

    /**
     * 构建字段位置信息，用于更新用
     *
     * @return
     */
    private Map<String, Integer> buildColumnSort() {
        Integer paramIndex = 1;
        Map<String, Integer> columnSort = new HashMap<String, Integer>();
        for (ColumnInfo col : this.columnList) {
            String columnName = col.getName();
            if (this.primarySet.contains(columnName)) {
                continue;
            }
            columnSort.put(columnName, paramIndex);
            paramIndex++;
        }

        for (ColumnInfo col : this.columnList) {
            String columnName = col.getName();
            if (this.primarySet.contains(columnName)) {
                columnSort.put(columnName, paramIndex);
                paramIndex++;
            }
        }
        return columnSort;
    }

    /**
     * 构建插入语句
     *
     * @return
     */
    private String buildInsertSql() {
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
    private String buildUpdateSql() {
        StringBuilder columnSql = new StringBuilder();
        StringBuilder primarySql = new StringBuilder();
        for (ColumnInfo col : this.columnList) {
            String columnName = col.getName();
            // 主键
            if (this.primarySet.contains(columnName)) {
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
     * 查询数据是否存在
     *
     * @param row 数据
     */
    public boolean exists(DataRow row) {
        String tableNameConvert = DbUtils.convertName(tableName, dbType);
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
                case PostgreSql:
                    sql = String.format("select 1 as number from %s%s  limit  1 ", tableNameConvert, whereSql);
                    break;
                case SqlServer:
                case ClickHouse:
                default:
                    sql = String.format("SELECT TOP 1 1 as number FROM %s%s", tableNameConvert, whereSql);
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
