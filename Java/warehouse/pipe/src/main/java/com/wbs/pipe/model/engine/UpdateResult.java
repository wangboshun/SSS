package com.wbs.pipe.model.engine;

import com.wbs.common.database.base.DataTable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author WBS
 * @date 2023/3/11 15:05
 * @desciption UpdateResult
 */
@Setter
@Getter
public class UpdateResult implements Serializable {
    private String spend;
    private DataTable errorData;
    private int updateCount;
    private int errorCount;

    public void updateTotal(UpdateResult updateResult) {
        if (this.getErrorData() == null) {
            this.setErrorData(new DataTable());
        }
        if (updateResult != null) {
            this.setErrorData(this.getErrorData().addDt(updateResult.getErrorData()));
            this.setUpdateCount(updateResult.getUpdateCount() + this.getUpdateCount());
            this.setErrorCount(updateResult.getErrorCount() + this.getErrorCount());
        }
    }
}
