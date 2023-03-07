package com.wbs.engine.core.base;

import com.wbs.common.database.DbUtils;
import com.wbs.common.database.base.DataRow;
import com.wbs.common.database.base.DataTable;
import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.common.database.base.model.WhereInfo;
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
 * @date 2023/3/2 15:37
 * @desciption ReaderAbstract
 */
@Component
public abstract class ReaderAbstract implements IReader {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    protected DbTypeEnum dbType;
    private Connection connection;
    private String tableName;
    private List<ColumnInfo> columnList;
    private Set<String> primarySet;

    @Override
    public void config(String tableName, Connection connection) {
        config(tableName, connection, DbUtils.getColumns(connection, tableName));
    }

    @Override
    public void config(String tableName, Connection connection, List<ColumnInfo> columnList) {
        this.connection = connection;
        this.tableName = tableName;
        this.columnList = columnList;
        this.primarySet = this.columnList.stream().filter(x -> x.getPrimary() == 1).map(ColumnInfo::getName).collect(Collectors.toSet());
    }

    @Override
    public DataTable readData(List<WhereInfo> whereList) {
        DataTable dt = new DataTable();
        ResultSet result = null;
        PreparedStatement pstmt = null;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT  ");
            sb.append(columnList.stream().map(ColumnInfo::getName).collect(Collectors.joining(",")));
            sb.append(" FROM ");
            sb.append(DbUtils.convertName(tableName, connection));
            sb.append(" WHERE ");

            for (WhereInfo item : whereList) {
                sb.append(DbUtils.convertName(item.getColumn(), dbType));
                sb.append(item.getOperate()).append("?");
                sb.append("  AND  ");
            }
            sb.delete(sb.length() - 6, sb.length());
            pstmt = connection.prepareStatement(sb.toString(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

            int index = 1;
            for (WhereInfo item : whereList) {
                String javaType = DbUtils.getColumnJavaType(columnList, item.getColumn());
                DbUtils.setParam(pstmt, index, item.getValue(), javaType);
                index++;
            }
            result = pstmt.executeQuery();
            dt = buildData(result);
        } catch (Exception e) {
            logger.error("------ReaderAbstract readData error------", e);
        } finally {
            DbUtils.closeStatement(pstmt);
        }
        return dt;
    }

    @Override
    public DataTable readData(String sql) {
        ResultSet result = null;
        PreparedStatement pstmt = null;
        DataTable dt = new DataTable();
        try {
            pstmt = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            if (dbType == DbTypeEnum.PostgreSql) {
                pstmt.setFetchSize(10000);
            } else {
                pstmt.setFetchSize(Integer.MIN_VALUE);
            }
            result = pstmt.executeQuery();
            dt = buildData(result);
        } catch (Exception e) {
            logger.error("------ReaderAbstract readData error------", e);
        } finally {
            DbUtils.closeResultSet(result);
            DbUtils.closeStatement(pstmt);
        }
        return dt;
    }

    /**
     * 构建返回集合
     *
     * @param resultSet
     * @return
     */
    private DataTable buildData(ResultSet resultSet) {
        DataTable dt = new DataTable();
        try {
            while (resultSet.next()) {
                DataRow dr = new DataRow(this.columnList.size());
                for (ColumnInfo col : this.columnList) {
                    String columnName = col.getName();
                    dr.put(columnName, resultSet.getObject(columnName));
                }
                dt.add(dr);
            }
        } catch (Exception e) {
            logger.error("------ReaderAbstract builderResult error------", e);
        }
        return dt;
    }
}
