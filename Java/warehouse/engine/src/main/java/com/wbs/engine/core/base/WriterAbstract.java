package com.wbs.engine.core.base;

import com.wbs.common.database.DbUtils;
import com.wbs.common.database.base.DataRow;
import com.wbs.common.database.base.DataTable;
import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.engine.model.WriterResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author WBS
 * @date 2023/3/2 15:46
 * @desciption WriterAbstract
 */
public class WriterAbstract implements IWriter {
    private String tableName;
    protected DbTypeEnum dbType;
    private Connection connection;
    private Set<String> primarySet;
    private List<ColumnInfo> columnList;
    private static final int BATCH_SIZE = 10000;
    private static final int THREAD_SIZE = 10;
    private final ExecutorService executor = Executors.newFixedThreadPool(THREAD_SIZE, new CustomizableThreadFactory("writerThread---"));
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void config(String tableName, Connection connection, List<ColumnInfo> columnList) {
        this.tableName = tableName;
        this.connection = connection;
        this.columnList = columnList;
        this.primarySet = columnList.stream().filter(x -> x.getPrimary() == 1).map(ColumnInfo::getName).collect(Collectors.toSet());
    }

    /**
     * 插入数据
     *
     * @param dt dt
     * @return 返回错误数据
     */
    @Override
    public WriterResult insertData(DataTable dt) {
        DataTable exceptionData = new DataTable();
        LocalDateTime start = LocalDateTime.now();
        DataTable exitsData = new DataTable();
        DataTable errorData = new DataTable();
        // 如果小于设置批大小
        if (dt.size() < BATCH_SIZE) {
            try {
                batchInsert(dt);
            } catch (Exception e) {
                exitsData.addAll(findExitsData(dt));
                errorData.addAll(findErrorData(dt, 1));
                // exceptionData.addAll(dt);
            }
        } else {
            List<DataTable> partition = dt.split(THREAD_SIZE);// 分成10个线程
            CompletableFuture[] array = partition.stream().map(item -> CompletableFuture.runAsync(() -> batchInsert(item), executor).exceptionally(error -> {
                // exceptionData.addAll(item);
                exitsData.addAll(findExitsData(item));
                errorData.addAll(findErrorData(item, 1));
                return null;
            })).toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(array).join();
            executor.shutdown();
        }
        LocalDateTime end = LocalDateTime.now();
        float tm = Duration.between(start, end).toMillis() / 1000f;
        WriterResult result = builderResult(exceptionData, 1);
        result.setSpend(String.format("%.2f", tm));
        result.setInsertCount(dt.size() - result.getExitsCount() - result.getErrorCount());
        return result;
    }

    /**
     * 更新数据
     *
     * @param dt 数据集
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
            List<DataTable> partition = dt.split(THREAD_SIZE);// 分成10个线程
            CompletableFuture[] array = partition.stream().map(item -> CompletableFuture.runAsync(() -> batchUpdate(item), executor).exceptionally(error -> {
                exceptionData.addAll(item);
                return null;
            })).toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(array).join();
            executor.shutdown();
        }
        LocalDateTime end = LocalDateTime.now();
        float tm = Duration.between(start, end).toMillis() / 1000f;
        WriterResult result = builderResult(exceptionData, 2);
        result.setSpend(String.format("%.2f", tm));
        result.setUpdateCount(dt.size() - result.getErrorCount());
        return result;
    }


    /**
     * 批量写入
     *
     * @param rows 数据集
     */
    private void batchInsert(List<DataRow> rows) {
        PreparedStatement pstm = null;
        try {
            String sql = DbUtils.buildInsertSql(tableName, columnList, dbType);
            this.connection.setAutoCommit(false);
            pstm = connection.prepareStatement(sql);
            int rowIndex = 0;
            for (DataRow row : rows) {
                // 如果线程中断，停止写入
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
                int paramIndex = 1;
                for (ColumnInfo col : this.columnList) {
                    DbUtils.setParam(pstm, paramIndex, row.get(col.getName()), col.getJavaType());
                    paramIndex++;
                }
                pstm.addBatch();
                if (rowIndex > 0 && rowIndex % BATCH_SIZE == 0) {
                    pstm.executeBatch();
                    pstm.clearBatch();
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
     * @param row 数据
     */
    private boolean singleInsert(DataRow row) {
        PreparedStatement pstm = null;
        try {
            String sql = DbUtils.buildInsertSql(tableName, columnList, dbType);
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
     * @param dt 数据集
     */
    private void batchUpdate(DataTable dt) {
        PreparedStatement pstm = null;
        try {
            // 非主键，这里做了特殊处理，因为sql语句中非主键的参数在前面，所以先把非主键和参数先封装进去
            String sql = DbUtils.buildUpdateSql(tableName, columnList, primarySet, dbType);
            this.connection.setAutoCommit(false);
            pstm = connection.prepareStatement(sql);
            int rowIndex = 0;
            Map<String, Integer> columnSort = DbUtils.buildColumnSql(columnList, primarySet);
            for (DataRow row : dt) {
                // 如果线程中断，停止更新
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
                for (ColumnInfo col : this.columnList) {
                    String columnName = col.getName();
                    Integer paramIndex = columnSort.get(columnName);
                    DbUtils.setParam(pstm, paramIndex, row.get(columnName), col.getJavaType());
                }
                pstm.addBatch();
                if (rowIndex > 0 && rowIndex % BATCH_SIZE == 0) {
                    pstm.executeBatch();
                    pstm.clearBatch();
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
     * @param row 数据
     */
    private boolean singleUpdate(DataRow row) {
        PreparedStatement pstm = null;
        try {
            String sql = DbUtils.buildUpdateSql(tableName, columnList, primarySet, dbType);
            this.connection.setAutoCommit(true);
            pstm = connection.prepareStatement(sql);
            Map<String, Integer> columnSort = DbUtils.buildColumnSql(columnList, primarySet);
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
     * @param dt 数据
     */
    private DataTable findExitsData(DataTable dt) {
        DataTable exitsData = new DataTable();
        for (DataRow row : dt) {
            if (exists(row)) {
                exitsData.add(row);
            }
        }
        return exitsData;
    }

    /**
     * 查找错误数据，单条插入或更新时还是报错即为错误数据
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
        return result;
    }

    /**
     * 构建返回值
     *
     * @param exceptionData 错误数据
     * @param type          1为插入，2为更新
     */
    private WriterResult builderResult(DataTable exceptionData, int type) {
        WriterResult result = new WriterResult();
        if (exceptionData.isEmpty()) {
            return result;
        }
        // 插入情况才进行存在数据处理
        if (type == 1) {
            DataTable exitsData = findExitsData(exceptionData);  // 查找已存在数据
            // 如果有已存在数据，保存已存在数据
            if (!exitsData.isEmpty()) {
                result.setExistData(exitsData);
                result.setExitsCount(exitsData.size());
                exceptionData.removeAll(exitsData); // 并去差集
            }
        }
        // 处理错误数据
        if (!exceptionData.isEmpty()) {
            DataTable errorData = findErrorData(exceptionData, type);
            if (!errorData.isEmpty()) {
                result.setErrorData(errorData);
                result.setErrorCount(errorData.size());
            }
        }
        return result;
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
            if (dbType == DbTypeEnum.SQLSERVER) {
                sql = String.format("SELECT TOP 1 1 as number FROM %s%s", tableNameConvert, whereSql);
            } else {
                sql = String.format("select 1 as number from %s%s  limit  1 ", tableNameConvert, whereSql);
            }
            pstm = connection.prepareStatement(sql);
            int index = 1;
            for (String columnName : primarySet) {
                Optional<ColumnInfo> first = this.columnList.stream().filter(x -> x.getName().equals(columnName)).findFirst();
                if (first.isPresent()) {
                    String javaType = first.get().getJavaType();
                    DbUtils.setParam(pstm, index, row.get(columnName), javaType);
                    index++;
                }
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
