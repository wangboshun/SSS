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
        if (updateResult == null) {
            return;
        }
        if (updateResult.getErrorData() != null) {
            if (this.errorData == null) {
                this.errorData = updateResult.getErrorData();
            } else {
                this.errorData = this.errorData.addTable(updateResult.getErrorData());
            }
        }
        this.updateCount = updateResult.getUpdateCount() + this.updateCount;
        this.errorCount = updateResult.getErrorCount() + this.errorCount;
    }
}
