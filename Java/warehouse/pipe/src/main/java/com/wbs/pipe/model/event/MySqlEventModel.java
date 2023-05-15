package com.wbs.pipe.model.event;

import lombok.Getter;
import lombok.Setter;

/**
 * @author WBS
 * @date 2023/4/27 15:24
 * @desciption MySqlEvent
 */
@Setter
@Getter
public class MySqlEventModel extends EventAbstractModel {
    @Override
    public String toString() {
        return "taskId:" + getTaskInfo().getName() + ",sinkName:" + getSinkInfo().getName() + ",dt:" + getTable().size() + ",batchIndex:" + getBatchIndex() + ",batchSize:" + getBatchSize() + ",isEnd:" + isEnd();
    }
}
