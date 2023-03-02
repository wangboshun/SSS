package com.wbs.engine.core;

import com.wbs.common.database.DbUtils;
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

    @Override
    public void config(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Map<String, Object>> getData(String sql, Statement stmt, Map<String, String> columns) {
        ResultSet result = null;
        List<Map<String, Object>> resultData = new ArrayList<>();
        try {
            stmt = connection.createStatement();
            result = stmt.executeQuery(sql);
            resultData = buildResult(result, columns);
        } catch (Exception e) {
            logger.error("------ReaderAbstract getData error------", e);
        } finally {
            DbUtils.closeResultSet(result);
            DbUtils.closeStatement(stmt);
        }
        return resultData;
    }

    @Override
    public List<Map<String, Object>> getData(String sql, PreparedStatement pstmt, Map<String, String> columns) {
        ResultSet result = null;
        List<Map<String, Object>> resultData = new ArrayList<>();
        try {
            pstmt = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            result = pstmt.executeQuery();
            resultData = buildResult(result, columns);
        } catch (Exception e) {
            logger.error("------ReaderAbstract getData error------", e);
        } finally {
            DbUtils.closeResultSet(result);
            DbUtils.closeStatement(pstmt);
        }
        return resultData;
    }

    /**
     * 构建返回集合
     *
     * @param result
     * @param columns
     * @return
     */
    private List<Map<String, Object>> buildResult(ResultSet result, Map<String, String> columns) {
        List<Map<String, Object>> resultData = new ArrayList<>();
        try {
            while (result.next()) {
                Map<String, Object> rowData = new HashMap<>(columns.size());
                for (Map.Entry<String, String> entry : columns.entrySet()) {
                    String key = entry.getKey();
                    rowData.put(key, result.getObject(key));
                }
                resultData.add(rowData);
            }
        } catch (Exception e) {
            logger.error("------ReaderAbstract builderResult error------", e);
        }
        return resultData;
    }
}
