package com.wbs.pipe.model.event;

import com.wbs.common.database.base.DataTable;
import com.wbs.pipe.model.sink.SinkInfoModel;
import com.wbs.pipe.model.task.TaskInfoModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author WBS
 * @date 2023/4/27 15:26
 * @desciption IEvent
 */
@Setter
@Getter
public class MessageEventModel implements Serializable {
    private TaskInfoModel taskInfo;
    private DataTable table;
    private boolean end;
    private int batchIndex;
    private int batchSize;
    private SinkInfoModel sinkInfo;
    private LocalDateTime sendTime;
}