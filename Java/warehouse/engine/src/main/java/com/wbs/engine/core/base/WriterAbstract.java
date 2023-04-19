package com.wbs.engine.core.base;

import cn.hutool.core.text.CharSequenceUtil;
import com.wbs.common.database.DbUtils;
import com.wbs.common.database.base.DataRow;
import com.wbs.common.database.base.DataTable;
import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.engine.model.WriterResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.sql.*;
import java.time.Duration;
import java.time.Instant;
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
    private String existsSql;
    private String batchInsertSql;
    private String batchUpdateSql;
    private String singleInsertSql;
    private String singleUpdateSql;

    protected DbTypeEnum dbType;
    private Connection connection;
    private Set<String> primarySet;
    private List<ColumnInfo> columnList;

    /**
     * 字段顺序，主键排在后面
     */
    private Map<String, Integer> columnSort;

    /**
     * 批处理数
     */
    private static final int BATCH_SIZE = 1000;

    /**
     * 线程数
     */
    private static final int THREAD_SIZE = 20;
    private final ExecutorService executor = Executors.newFixedThreadPool(THREAD_SIZE, new CustomizableThreadFactory("writerThread---"));
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void config(String tableName, Connection connection, List<ColumnInfo> columnList) {
        this.tableName = tableName;
        this.connection = connection;
        this.columnList = columnList;
        this.primarySet = columnList.stream().filter(x -> x.getPrimary() == 1).map(ColumnInfo::getName).collect(Collectors.toSet());
        this.columnSort = DbUtils.sortColumn(columnList, primarySet);
    }

    /**
     * 插入数据
     *
     * @param dt dt
     * @return 返回错误数据
     */
    @Override
    public WriterResult insertData(DataTable dt) {
        DataTable exitsData = new DataTable();
        DataTable errorData = new DataTable();
        DataTable exceptionData = new DataTable();
        Instant start = Instant.now();
        try {
            // 如果小于设置批大小
            if (dt.size() < BATCH_SIZE) {
                exceptionData.addAll(batchInsert(dt));
            } else {
                List<DataTable> partition = dt.splitBatch(BATCH_SIZE);
                int count = partition.size() / THREAD_SIZE;
                for (int i = 0; i <= count; i++) {
                    List<DataTable> page = DataTable.getPage(partition, i, THREAD_SIZE);
                    CompletableFuture[] array = page.stream().map(item -> CompletableFuture.runAsync(() -> {
                        exceptionData.addAll(batchInsert(item));
                    }, executor)).toArray(CompletableFuture[]::new);
                    CompletableFuture.allOf(array).join();
                }
            }
            this.connection.setAutoCommit(true);
            errorData.addAll(findErrorData(exceptionData, 1));
            exceptionData.removeAll(errorData); // 并去差集
            exitsData.addAll(exceptionData);
        } catch (Exception e) {
            System.out.println();
        }
        executor.shutdown();
        Instant end = Instant.now();
        float tm = Duration.between(start, end).toMillis() / 1000f;
        WriterResult result = builderResult(exitsData, errorData);
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
        DataTable errorData = new DataTable();
        Instant start = Instant.now();
        if (primarySet.isEmpty()) {
            throw new RuntimeException("该表没有主键，无法更新！");
        }
        try {
            if (dt.size() < BATCH_SIZE) {
                errorData.addAll(batchUpdate(dt));
            } else {
                List<DataTable> partition = dt.splitBatch(BATCH_SIZE);
                int count = partition.size() / THREAD_SIZE;
                for (int i = 0; i <= count; i++) {
                    List<DataTable> page = DataTable.getPage(partition, i, THREAD_SIZE);
                    CompletableFuture[] array = page.stream().map(item -> CompletableFuture.runAsync(() -> {
                        errorData.addAll(batchUpdate(item));
                    }, executor)).toArray(CompletableFuture[]::new);
                    CompletableFuture.allOf(array).join();
                }
            }
            this.connection.setAutoCommit(true);
        } catch (Exception e) {
            System.out.println();
        }
        executor.shutdown();
        Instant end = Instant.now();
        float tm = Duration.between(start, end).toMillis() / 1000f;
        WriterResult result = builderResult(null, errorData);
        result.setSpend(String.format("%.2f", tm));
        result.setUpdateCount(dt.size() - result.getErrorCount());
        return result;
    }

    /**
     * 批量写入
     *
     * @param dt 数据集
     */
    private DataTable batchInsert(DataTable dt) {
        PreparedStatement pstm = null;
        DataTable errorData = new DataTable();
        try {
            String sql = "";
            if (CharSequenceUtil.isEmpty(batchInsertSql)) {
                batchInsertSql = DbUtils.buildInsertSql(tableName, columnList, dbType);
            }
            sql = batchInsertSql;
            this.connection.setAutoCommit(false);
            pstm = connection.prepareStatement(sql);
            int rowIndex = 0;
            for (DataRow row : dt) {
                // 如果线程中断，停止写入
                if (Thread.currentThread().isInterrupted()) {
                    return errorData;
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
        } catch (BatchUpdateException e1) {
            int[] updateCounts = e1.getUpdateCounts();
            for (int i = 0; i < updateCounts.length; i++) {
                // 如果插入失败
                if (updateCounts[i] == Statement.EXECUTE_FAILED) {
                    errorData.add(dt.get(i));
                }
            }
        } catch (Exception e2) {
            logger.error("------WriterAbstract batchWrite error------", e2);
        } finally {
            DbUtils.closeStatement(pstm);
        }

        return errorData;
    }

    /**
     * 单条写入
     *
     * @param row 数据
     */
    private boolean singleInsert(DataRow row) {
        PreparedStatement pstm = null;
        try {
            String sql = "";
            if (CharSequenceUtil.isEmpty(singleInsertSql)) {
                singleInsertSql = DbUtils.buildInsertSql(tableName, columnList, dbType);
            }
            sql = singleInsertSql;
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
            if (e.getMessage().contains("Duplicate")) {
                throw new RuntimeException("Duplicate");
            }
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
    private DataTable batchUpdate(DataTable dt) {
        PreparedStatement pstm = null;
        DataTable errorData = new DataTable();
        try {
            String sql = "";
            if (CharSequenceUtil.isEmpty(batchUpdateSql)) {
                // 非主键，这里做了特殊处理，因为sql语句中非主键的参数在前面，所以先把非主键和参数先封装进去
                batchUpdateSql = DbUtils.buildUpdateSql(tableName, columnList, primarySet, dbType);
            }
            sql = batchUpdateSql;
            this.connection.setAutoCommit(false);
            pstm = connection.prepareStatement(sql);
            int rowIndex = 0;
            for (DataRow row : dt) {
                // 如果线程中断，停止更新
                if (Thread.currentThread().isInterrupted()) {
                    return errorData;
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
        } catch (BatchUpdateException e1) {
            int[] updateCounts = e1.getUpdateCounts();
            for (int i = 0; i < updateCounts.length; i++) {
                // 如果插入失败
                if (updateCounts[i] == Statement.EXECUTE_FAILED) {
                    errorData.add(dt.get(i));
                }
            }
        } catch (Exception e2) {
            logger.error("WriterAbstract batchUpdate", e2);
        } finally {
            DbUtils.closeStatement(pstm);
        }
        return errorData;
    }

    /**
     * 单条更新
     *
     * @param row 数据
     */
    private boolean singleUpdate(DataRow row) {
        PreparedStatement pstm = null;
        try {
            String sql = "";
            if (CharSequenceUtil.isEmpty(singleUpdateSql)) {
                singleUpdateSql = DbUtils.buildUpdateSql(tableName, columnList, primarySet, dbType);
            }
            sql = singleUpdateSql;
            this.connection.setAutoCommit(true);
            pstm = connection.prepareStatement(sql);
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
                try {
                    boolean b = !singleInsert(item);
                    // 只有错误才保存，如果是重复数据，不保存
                    if (!b) {
                        result.add(item);
                    }
                } catch (Exception e) {
                }
            } else if (type == 2 && !singleUpdate(item)) {
                result.add(item);
            }
        });
        return result;
    }

    /**
     * 构建返回值
     *
     * @param exitsData 重复数据
     * @param errorData 错误数据
     */
    private WriterResult builderResult(DataTable exitsData, DataTable errorData) {
        WriterResult result = new WriterResult();
        if (exitsData != null && !exitsData.isEmpty()) {
            result.setExistData(exitsData);
            result.setExitsCount(exitsData.size());
        }
        if (errorData != null && !errorData.isEmpty()) {
            result.setErrorData(errorData);
            result.setErrorCount(errorData.size());
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
            if (CharSequenceUtil.isEmpty(existsSql)) {
                StringBuilder whereSql = new StringBuilder(" WHERE ");
                for (String columnName : primarySet) {
                    whereSql.append(DbUtils.convertName(columnName, dbType)).append("=?");
                    whereSql.append(" AND ");
                }
                whereSql.delete(whereSql.length() - 5, whereSql.length());
                if (dbType == DbTypeEnum.SQLSERVER) {
                    existsSql = String.format("SELECT TOP 1 1 as number FROM %s%s", tableNameConvert, whereSql);
                } else {
                    existsSql = String.format("select 1 as number from %s%s  limit  1 ", tableNameConvert, whereSql);
                }
            }
            sql = existsSql;
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
