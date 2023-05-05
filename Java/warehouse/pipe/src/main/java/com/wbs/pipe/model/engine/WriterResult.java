package com.wbs.pipe.model.engine;

import com.wbs.common.database.base.DataTable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author WBS
 * @date 2023/3/11 15:05
 * @desciption WriterResult
 */
@Setter
@Getter
public class WriterResult implements Serializable {
    private String spend;
    private DataTable errorData;
    private DataTable existData;
    private int insertCount;
    private int updateCount;
    private int errorCount;
    private int exitsCount;
    private int ignoreCount;

    public void total(WriterResult insertResult, WriterResult updateResult) {
        if (this.getErrorData() == null) {
            this.setErrorData(new DataTable());
        }
        if (this.getExistData() == null) {
            this.setExistData(new DataTable());
        }

        if (insertResult != null) {
            this.setErrorData(this.getErrorData().addDt(insertResult.getErrorData()));
            this.setExistData(this.getExistData().addDt(insertResult.getExistData()));
            this.setInsertCount(insertResult.getInsertCount() + this.getInsertCount());
            this.setUpdateCount(insertResult.getUpdateCount() + this.getUpdateCount());
            this.setErrorCount(insertResult.getErrorCount() + this.getErrorCount());
            this.setExitsCount(insertResult.getExitsCount() + this.getExitsCount());
            this.setIgnoreCount(insertResult.getIgnoreCount() + +this.getIgnoreCount());
        }
        if (updateResult != null) {
            this.setErrorData(this.getErrorData().addDt(updateResult.getErrorData()));
            this.setExistData(this.getExistData().addDt(updateResult.getExistData()));
            this.setInsertCount(updateResult.getInsertCount() + this.getInsertCount());
            this.setUpdateCount(updateResult.getUpdateCount() + this.getUpdateCount());
            this.setErrorCount(updateResult.getErrorCount() + this.getErrorCount());
            this.setExitsCount(updateResult.getExitsCount() + this.getExitsCount());
            this.setIgnoreCount(updateResult.getIgnoreCount() + this.getIgnoreCount());
        }
    }
}
