package com.wbs.engine.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * @date 2023/3/2 15:29
 * @desciption IReader
 */
public interface IReader {

    public void config(Connection connection);
    public List<Map<String, Object>> getData(String sql, Statement stmt, Map<String, String> columns);

    public List<Map<String, Object>> getData(String sql, PreparedStatement pstmt, Map<String, String> columns);
}
