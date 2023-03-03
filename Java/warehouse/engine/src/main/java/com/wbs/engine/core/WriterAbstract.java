package com.wbs.engine.core;

import com.wbs.common.database.DbTypeEnum;
import com.wbs.common.database.DbUtils;
import com.wbs.engine.model.DataRow;
import com.wbs.engine.model.DataTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
    public int writeData(DataRow row) {
        PreparedStatement pstm = null;
        try {
            StringBuilder columnSql = new StringBuilder();
            StringBuilder valueSql = new StringBuilder();
            for (Map.Entry<String, String> entry : this.columns.entrySet()) {
                String key = entry.getKey();
                columnSql.append(DbUtils.convertName(key, dbType)).append(",");
                valueSql.append("?,");
            }

            columnSql.deleteCharAt(columnSql.length() - 1);
            valueSql.deleteCharAt(valueSql.length() - 1);
            String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", DbUtils.convertName(tableName, dbType), columnSql, valueSql);
            pstm = connection.prepareStatement(sql);
            int index = 1;
            for (Map.Entry<String, String> entry : this.columns.entrySet()) {
                DbUtils.setParam(pstm, index, row.get(entry.getKey()), entry.getValue());
                index++;
            }
            pstm.execute();
        } catch (Exception e) {
            logger.error("------WriterAbstract writeData error------", e);

        } finally {
            DbUtils.closeStatement(pstm);
        }
        return 0;
    }

    @Override
    public int writeData(DataTable dt) {
        PreparedStatement pstm = null;
        try {
            StringBuilder columnSql = new StringBuilder();
            StringBuilder valueSql = new StringBuilder();
            for (Map.Entry<String, String> entry : this.columns.entrySet()) {
                String key = entry.getKey();
                columnSql.append(DbUtils.convertName(key, dbType)).append(",");
                valueSql.append("?,");
            }

            columnSql.deleteCharAt(columnSql.length() - 1);
            valueSql.deleteCharAt(valueSql.length() - 1);
            String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", DbUtils.convertName(tableName, dbType), columnSql, valueSql);
            this.connection.setAutoCommit(false);
            pstm = connection.prepareStatement(sql);
            for (DataRow row : dt) {
                int index = 1;
                for (Map.Entry<String, String> entry : this.columns.entrySet()) {
                    DbUtils.setParam(pstm, index, row.get(entry.getKey()), entry.getValue());
                    index++;
                }
                pstm.addBatch();
            }
            pstm.executeBatch();
            pstm.clearBatch();
            connection.commit();
        } catch (Exception e) {
            logger.error("------WriterAbstract writeData error------", e);
        } finally {
            DbUtils.closeStatement(pstm);
        }
        return 0;
    }


    @Override
    public boolean havaDate(Map<String, Object> data) {
        return false;
    }
}
