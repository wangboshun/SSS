package com.wbs.pipe.application.engine.base.db;

import cn.hutool.extra.spring.SpringUtil;
import com.google.common.eventbus.AsyncEventBus;
import com.wbs.common.database.DbUtils;
import com.wbs.common.database.base.DataColumn;
import com.wbs.common.database.base.DataRow;
import com.wbs.common.database.base.DataTable;
import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.common.database.base.model.WhereInfo;
import com.wbs.common.utils.DataUtils;
import com.wbs.pipe.model.event.PipeEventModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author WBS
 * @date 2023/3/2 15:37
 * @desciption ReaderAbstract
 */
public abstract class DbReaderBase implements IDbReader {
    private String taskId;
    private String tableName;
    protected DbTypeEnum dbType;
    private Connection connection;
    private List<WhereInfo> whereList;
    private List<ColumnInfo> columnList;
    private AsyncEventBus asyncEventBus;
    private static final int BATCH_SIZE = 1000;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void config(String taskId, String tableName, Connection connection, List<ColumnInfo> columnList, List<WhereInfo> whereList) {
        this.taskId = taskId;
        this.tableName = tableName;
        this.connection = connection;
        this.columnList = columnList;
        this.whereList = whereList;
        asyncEventBus = SpringUtil.getBean("defaultEventBus");
    }

    @Override
    public void readData() {
        StringBuilder sql = new StringBuilder();
        String lastStr = " WHERE ";
        sql.append("SELECT  ");
        sql.append(columnList.stream().map(ColumnInfo::getName).collect(Collectors.joining(",")));
        sql.append(" FROM ");
        sql.append(DbUtils.convertName(tableName, connection));
        if (whereList == null || whereList.isEmpty()) {
            readData(sql.toString());
        }
        if (whereList != null && !whereList.isEmpty()) {
            sql.append(lastStr);
            for (WhereInfo item : whereList) {
                if (item.getValue() == null) {
                    continue;
                }
                sql.append(DbUtils.convertName(item.getColumn(), dbType)).append(" ");
                // in和not in
                if (item.getSymbol().toLowerCase().contains("in")) {
                    sql.append(item.getSymbol()).append("(");
                    List<Object> valueList = DataUtils.toList(item.getValue());
                    valueList.forEach(x -> {
                        sql.append("?");
                        sql.append(",");
                    });
                    sql.deleteCharAt(sql.length() - 1);
                    sql.append(")");
                }
                // like
                else if (item.getSymbol().toLowerCase().contains("like")) {
                    sql.append(item.getSymbol()).append(" ");
                    sql.append("?");
                } else {
                    sql.append(item.getSymbol()).append(" ").append("?");
                }
                lastStr = " " + item.getOperate() + " ";
                sql.append(lastStr);
            }
            sql.delete(sql.length() - lastStr.length(), sql.length());
            readData(sql.toString());
        }
    }

    @Override
    public void readData(String sql) {
        ResultSet result = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            if (dbType == DbTypeEnum.POSTGRESQL) {
                pstmt.setFetchSize(BATCH_SIZE);
            } else {
                pstmt.setFetchSize(Integer.MIN_VALUE);
            }

            if (whereList != null && !whereList.isEmpty()) {
                int index = 1;
                for (WhereInfo item : whereList) {
                    if (item.getValue() == null) {
                        continue;
                    }
                    String javaType = "";
                    ColumnInfo column = columnList.stream().filter(x -> item.getColumn().equals(x.getName())).findAny().orElse(null);
                    if (column != null) {
                        javaType = column.getJavaType();
                    }

                    // in和not in，多个参数
                    if (item.getSymbol().toLowerCase().contains("in")) {
                        List<Object> valueList = DataUtils.toList(item.getValue());
                        for (int i = 0; i < valueList.size(); i++) {
                            DbUtils.setParam(pstmt, index + i, valueList.get(i), valueList.get(i).getClass().getSimpleName());
                        }
                    } else {
                        DbUtils.setParam(pstmt, index, item.getValue(), javaType);
                    }
                    index++;
                }
            }

            result = pstmt.executeQuery();
            buildData(result);
        } catch (Exception e) {
            logger.error("------ReaderAbstract readData error------", e);
        } finally {
            DbUtils.closeResultSet(result);
            DbUtils.closeStatement(pstmt);
        }
    }

    /**
     * 构建返回集合
     *
     * @param resultSet 数据集
     */
    private void buildData(ResultSet resultSet) {
        try {
            DataTable dt = new DataTable(tableName);
            for (ColumnInfo col : this.columnList) {
                dt.addColumn(new DataColumn(col.getName(), col.getJavaType(), col.isPrimary()));
            }
            int batchIndex = 1;
            while (resultSet.next()) {
                // 如果线程中断，停止查询
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                DataRow row = new DataRow();
                for (ColumnInfo col : this.columnList) {
                    String columnName = col.getName();
                    row.setValue(columnName, resultSet.getObject(columnName));
                }
                dt.addRow(row);
                if (dt.getRows().size() >= BATCH_SIZE) {
                    sendEvent(dt.copy(), batchIndex, false);
                    dt.clear();
                    batchIndex++;
                }
            }
            if (!dt.getRows().isEmpty()) {
                sendEvent(dt.copy(), batchIndex, true);
                dt.clear();
            }
        } catch (Exception e) {
            logger.error("------ReaderAbstract builderResult error------", e);
        }
    }

    private void sendEvent(DataTable dt, int batchIndex, boolean isEnd) {
        PipeEventModel event = new PipeEventModel();
        event.setTaskId(taskId);
        event.setBatchSize(BATCH_SIZE);
        event.setTable(dt);
        event.setEnd(isEnd);
        event.setBatchIndex(batchIndex);
        asyncEventBus.post(event);
    }
}
