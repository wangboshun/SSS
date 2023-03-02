package com.wbs.engine.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * @date 2023/3/2 15:29
 * @desciption IWriter
 */
public interface IWriter {
    public void config(Connection connection);

    public int writeData(Map<String, Object> data, Statement stmt);

    public int writeData(Map<String, Object> data, PreparedStatement pstmt);

    public int writeData(List<Map<String, Object>> list, Statement stmt);

    public int writeData(List<Map<String, Object>> list, PreparedStatement pstmt);

    public boolean havaDate(Map<String, Object> data);
}
