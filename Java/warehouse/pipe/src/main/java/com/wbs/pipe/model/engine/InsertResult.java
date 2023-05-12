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
public class InsertResult implements Serializable {
    private String spend;
    private DataTable errorData;
    private DataTable existData;
    private int insertCount;
    private int errorCount;
    private int exitsCount;
    private int ignoreCount;

    public void insertTotal(InsertResult insertResult) {
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
            this.setErrorCount(insertResult.getErrorCount() + this.getErrorCount());
            this.setExitsCount(insertResult.getExitsCount() + this.getExitsCount());
            this.setIgnoreCount(insertResult.getIgnoreCount() + +this.getIgnoreCount());
        }
    }
}
