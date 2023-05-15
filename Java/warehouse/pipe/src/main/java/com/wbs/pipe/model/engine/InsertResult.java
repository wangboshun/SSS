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
        if (insertResult == null) {
            return;
        }
        if (insertResult.getErrorData() != null) {
            if (this.errorData == null) {
                this.errorData = insertResult.getErrorData();
            } else {
                this.errorData = this.errorData.addTable(insertResult.getErrorData());
            }
        }
        if (insertResult.getExistData() != null) {
            if (this.existData == null) {
                this.existData = insertResult.getExistData();
            } else {
                this.existData = this.existData.addTable(insertResult.getExistData());
            }

        }
        this.insertCount = insertResult.getInsertCount() + this.insertCount;
        this.errorCount = insertResult.getErrorCount() + this.errorCount;
        this.exitsCount = insertResult.getExitsCount() + this.exitsCount;
        this.ignoreCount = insertResult.getIgnoreCount() + +this.ignoreCount;
    }
}
