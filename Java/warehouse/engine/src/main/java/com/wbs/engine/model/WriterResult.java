package com.wbs.engine.model;

import com.wbs.common.database.base.DataTable;

import java.io.Serializable;

/**
 * @author WBS
 * @date 2023/3/11 15:05
 * @desciption WriterResult
 */
public class WriterResult implements Serializable {
    private String spend;
    private DataTable errorData;
    private DataTable exitsData;
    private int insertCount;
    private int updateCount;
    private int errorCount;
    private int exitsCount;

    public int getExitsCount() {
        return exitsCount;
    }

    public void setExitsCount(int exitsCount) {
        this.exitsCount = exitsCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public String getSpend() {
        return spend;
    }

    public void setSpend(String spend) {
        this.spend = spend;
    }

    public DataTable getErrorData() {
        return errorData;
    }

    public void setErrorData(DataTable errorData) {
        this.errorData = errorData;
    }

    public DataTable getExitsData() {
        return exitsData;
    }

    public void setExitsData(DataTable exitsData) {
        this.exitsData = exitsData;
    }

    public int getInsertCount() {
        return insertCount;
    }

    public void setInsertCount(int insertCount) {
        this.insertCount = insertCount;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }
}
