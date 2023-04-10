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
import com.wbs.common.database.base.model.WhereInfo;
import com.wbs.common.utils.DataUtils;
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
    protected List<ColumnInfo> columnList;
    private Set<String> primarySet;
    Cache<String, List<ColumnInfo>> fifoCache = CacheUtil.newFIFOCache(100);

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
    public DataTable readData(List<WhereInfo> whereList) {
        DataTable dt = new DataTable();
        ResultSet result = null;
        PreparedStatement pstmt = null;
        try {
            StringBuilder sql = new StringBuilder();
            String lastStr = " WHERE ";
            sql.append("SELECT  ");
            sql.append(columnList.stream().map(ColumnInfo::getName).collect(Collectors.joining(",")));
            sql.append(" FROM ");
            sql.append(DbUtils.convertName(tableName, connection));
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
            pstmt = connection.prepareStatement(sql.toString(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

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

                // in和not in
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
            if (dbType == DbTypeEnum.POSTGRESQL) {
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
