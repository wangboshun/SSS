package com.wbs.pipe.model.event;

import com.wbs.common.database.base.DataTable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author WBS
 * @date 2023/4/26 9:46
 * @desciption PipeEvent
 */
@Setter
@Getter
@ToString
public class PipeEventModel {
    private String taskId;
    private DataTable dt;
    private boolean end;
    private int batchIndex;
    private int batchSize;
}
