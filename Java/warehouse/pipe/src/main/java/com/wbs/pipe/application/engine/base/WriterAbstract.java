package com.wbs.pipe.application.engine.base;

import cn.hutool.core.text.CharSequenceUtil;
import com.wbs.common.database.DbUtils;
import com.wbs.common.database.base.DataRow;
import com.wbs.common.database.base.DataTable;
import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.pipe.model.engine.InsertResult;
import com.wbs.pipe.model.engine.UpdateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author WBS
 * @date 2023/3/2 15:46
 * @desciption WriterAbstract
 */
public class WriterAbstract implements IWriter {
    private String tableName;
    private String batchInsertSql;
    private String batchUpdateSql;
    private String singleInsertSql;

    protected DbTypeEnum dbType;
    private Connection connection;
    private Set<String> primarySet;
    private List<ColumnInfo> columnList;
    private Map<String, Integer> columnSort;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void config(String tableName, Connection connection, List<ColumnInfo> columnList) {
        this.tableName = tableName;
        this.connection = connection;
        this.columnList = columnList;
        this.primarySet = columnList.stream().filter(ColumnInfo::isPrimary).map(ColumnInfo::getName).collect(Collectors.toSet());
        this.columnSort = DbUtils.sortColumn(columnList, primarySet);
    }

    /**
     * 插入数据
     *
     * @param dt dt
     * @return 返回错误数据
     */
    @Override
    public InsertResult insertData(DataTable dt) {
        DataTable exitsData = new DataTable(dt);
        DataTable errorData = new DataTable(dt);
        DataTable exceptionData = new DataTable(dt);
        try {
            exceptionData.addTable(batchInsert(dt));
            errorData.addTable(findErrorData(exceptionData));
            exceptionData = exceptionData.deleteTable(errorData); // 去差集
            exitsData.addTable(exceptionData);
        } catch (Exception e) {
            logger.error("WriterAbstract insertData", e);
        }
        InsertResult result = builderResult(exitsData, errorData);
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
    public UpdateResult updateData(DataTable dt) {
        DataTable errorData = new DataTable(dt);
        DataTable updateData = new DataTable(dt);
        if (primarySet.isEmpty()) {
            throw new RuntimeException("该表没有主键，无法更新！");
        }
        errorData.addTable(batchUpdate(dt));
        UpdateResult result = builderResult(errorData);
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
        DataTable errorData = new DataTable(dt);
        try {
            String sql = "";
            if (CharSequenceUtil.isEmpty(batchInsertSql)) {
                batchInsertSql = DbUtils.buildInsertSql(tableName, columnList, dbType);
            }
            sql = batchInsertSql;
            this.connection.setAutoCommit(false);
            pstm = connection.prepareStatement(sql);
            for (DataRow row : dt.getRows()) {
                // 如果线程中断，停止写入
                if (Thread.currentThread().isInterrupted()) {
                    return errorData;
                }
                int paramIndex = 1;
                for (ColumnInfo col : this.columnList) {
                    DbUtils.setParam(pstm, paramIndex, row.getValue(col.getName()), col.getJavaType());
                    paramIndex++;
                }
                pstm.addBatch();
            }
            pstm.executeBatch();
            pstm.clearBatch();
            connection.commit();
        } catch (BatchUpdateException e1) {
            int[] updateCounts = e1.getUpdateCounts();
            for (int i = 0; i < updateCounts.length; i++) {
                // 如果插入失败
                if (updateCounts[i] == Statement.EXECUTE_FAILED) {
                    errorData.addRow(dt.getRow(i));
                }
            }
            batchInsert(dt.deleteTable(errorData));
        } catch (Exception e2) {
            logger.error("------WriterAbstract batchWrite error------", e2);
        } finally {
            DbUtils.closeStatement(pstm);
        }
        return errorData;
    }

    /**
     * 批量更新
     *
     * @param dt 数据集
     */
    private DataTable batchUpdate(DataTable dt) {
        PreparedStatement pstm = null;
        DataTable errorData = new DataTable(dt);
        try {
            String sql = "";
            if (CharSequenceUtil.isEmpty(batchUpdateSql)) {
                // 非主键，这里做了特殊处理，因为sql语句中非主键的参数在前面，所以先把非主键和参数先封装进去
                batchUpdateSql = DbUtils.buildUpdateSql(tableName, columnList, primarySet, dbType);
            }
            sql = batchUpdateSql;
            this.connection.setAutoCommit(false);
            pstm = connection.prepareStatement(sql);
            for (DataRow row : dt.getRows()) {
                // 如果线程中断，停止更新
                if (Thread.currentThread().isInterrupted()) {
                    return errorData;
                }
                for (ColumnInfo col : this.columnList) {
                    String columnName = col.getName();
                    Integer paramIndex = columnSort.get(columnName);
                    DbUtils.setParam(pstm, paramIndex, row.getValue(columnName), col.getJavaType());
                }
                pstm.addBatch();
            }
            pstm.executeBatch();
            pstm.clearBatch();
            connection.commit();
        } catch (BatchUpdateException e1) {
            int[] updateCounts = e1.getUpdateCounts();
            for (int i = 0; i < updateCounts.length; i++) {
                // 如果插入失败
                if (updateCounts[i] == Statement.EXECUTE_FAILED) {
                    errorData.addRow(dt.getRows().get(i));
                }
            }
            batchUpdate(dt.deleteTable(errorData));
        } catch (Exception e2) {
            logger.error("WriterAbstract batchUpdate", e2);
        } finally {
            DbUtils.closeStatement(pstm);
        }
        return errorData;
    }

    /**
     * 查找错误数据，单条插入或更新时还是报错即为错误数据
     *
     * @param dt 数据
     */
    private DataTable findErrorData(DataTable dt) {
        DataTable result = new DataTable(dt);
        dt.getRows().forEach(item -> {
            PreparedStatement pstm = null;
            try {
                String sql = "";
                if (CharSequenceUtil.isEmpty(singleInsertSql)) {
                    singleInsertSql = DbUtils.buildInsertSql(tableName, columnList, dbType);
                }
                sql = singleInsertSql;
                // this.connection.setAutoCommit(true);
                pstm = connection.prepareStatement(sql);
                int paramIndex = 1;
                for (ColumnInfo col : this.columnList) {
                    DbUtils.setParam(pstm, paramIndex, item.getValue(col.getName()), col.getJavaType());
                    paramIndex++;
                }
                pstm.execute();
            } catch (Exception e) {
                // 如果不是主键冲突错误，就当做是错误数据
                if (!e.getMessage().contains("Duplicate")) {
                    result.addRow(item);
                }
            } finally {
                DbUtils.closeStatement(pstm);
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
    private InsertResult builderResult(DataTable exitsData, DataTable errorData) {
        InsertResult result = new InsertResult();
        if (exitsData != null && !exitsData.getRows().isEmpty()) {
            result.setExistData(exitsData);
            result.setExitsCount(exitsData.size());
        }
        if (errorData != null && !errorData.getRows().isEmpty()) {
            result.setErrorData(errorData);
            result.setErrorCount(errorData.size());
        }
        return result;
    }

    /**
     * 构建返回值
     *
     * @param errorData 错误数据
     */
    private UpdateResult builderResult(DataTable errorData) {
        UpdateResult result = new UpdateResult();
        if (errorData != null && !errorData.getRows().isEmpty()) {
            result.setErrorData(errorData);
            result.setErrorCount(errorData.size());
        }
        return result;
    }
}
