package com.wbs.engine.core;

import com.wbs.common.database.DbTypeEnum;
import com.wbs.common.database.DbUtils;
import com.wbs.engine.model.DataRow;
import com.wbs.engine.model.DataTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

/**
 * @author WBS
 * @date 2023/3/2 15:37
 * @desciption ReaderAbstract
 */
@Component
public abstract class ReaderAbstract implements IReader {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Connection connection;
    private DbTypeEnum dbType;
    private String tableName;
    private Map<String, String> columns;

    @Override
    public void config(String tableName, Connection connection) {
        config(tableName, connection, DbUtils.getColumns(connection, this.tableName));
    }

    @Override
    public void config(String tableName, Connection connection, Map<String, String> columns) {
        this.connection = connection;
        this.tableName = tableName;
        dbType = DbUtils.getDbType(connection);
        this.columns = columns;
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
            logger.error("------ReaderAbstract getData error------", e);
        } finally {
            DbUtils.closeResultSet(result);
            DbUtils.closeStatement(pstmt);
        }
        return dt;
    }

    /**
     * 构建返回集合
     *
     * @param result
     * @return
     */
    private DataTable buildData(ResultSet result) {
        DataTable dt = new DataTable();
        try {
            while (result.next()) {
                DataRow dr = new DataRow(this.columns.size());
                for (Map.Entry<String, String> entry : this.columns.entrySet()) {
                    String key = entry.getKey();
                    dr.put(key, result.getObject(key));
                }
                dt.add(dr);
            }
        } catch (Exception e) {
            logger.error("------ReaderAbstract builderResult error------", e);
        }
        return dt;
    }
}
