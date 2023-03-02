package com.wbs.engine.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * @date 2023/3/2 15:46
 * @desciption WriterAbstract
 */
@Component
public abstract class WriterAbstract implements IWriter {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Connection connection;

    @Override
    public void config(Connection connection){
        this.connection = connection;
    }

    @Override
    public int writeData(Map<String, Object> data, Statement stmt) {
        return 0;
    }

    @Override
    public int writeData(Map<String, Object> data, PreparedStatement pstmt) {
        return 0;
    }

    @Override
    public int writeData(List<Map<String, Object>> list, Statement stmt) {
        return 0;
    }

    @Override
    public int writeData(List<Map<String, Object>> list, PreparedStatement pstmt) {
        return 0;
    }

    @Override
    public boolean havaDate(Map<String, Object> data) {
        return false;
    }
}
