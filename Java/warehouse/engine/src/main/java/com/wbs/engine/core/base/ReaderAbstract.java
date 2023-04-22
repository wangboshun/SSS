package com.wbs.engine.core.base;

import com.wbs.common.database.DbUtils;
import com.wbs.common.database.base.DataRow;
import com.wbs.common.database.base.DataTable;
import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.common.database.base.model.WhereInfo;
import com.wbs.common.utils.DataUtils;
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
public abstract class ReaderAbstract implements IReader {
    private String tableName;
    protected DbTypeEnum dbType;
    private Connection connection;
    private List<WhereInfo> whereList;
    private List<ColumnInfo> columnList;
    private final static int BATCH_SIZE = 1000;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void config(String tableName, Connection connection, List<ColumnInfo> columnList, List<WhereInfo> whereList) {
        this.tableName = tableName;
        this.connection = connection;
        this.columnList = columnList;
        this.whereList = whereList;
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

    @Override
    public void readData(String sql) {
        ResultSet result = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            if (dbType == DbTypeEnum.POSTGRESQL) {
                pstmt.setFetchSize(10000);
            } else {
                pstmt.setFetchSize(Integer.MIN_VALUE);
            }

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
            DataTable dt = new DataTable();
            while (resultSet.next()) {
                // 如果线程中断，停止查询
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                DataRow dr = new DataRow(this.columnList.size());
                for (ColumnInfo col : this.columnList) {
                    String columnName = col.getName();
                    dr.put(columnName, resultSet.getObject(columnName));
                }
                dt.add(dr);
                if (dt.size() > BATCH_SIZE) {
                    // send
                    dt.clear();
                }
            }
            if (!dt.isEmpty()) {
                // send
            }
        } catch (Exception e) {
            logger.error("------ReaderAbstract builderResult error------", e);
        }
    }
}
