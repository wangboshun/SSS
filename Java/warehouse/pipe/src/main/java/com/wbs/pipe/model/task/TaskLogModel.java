package com.wbs.pipe.model.task;

import com.wbs.pipe.model.engine.InsertResult;
import com.wbs.pipe.model.engine.UpdateResult;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author WBS
 * @date 2023/4/7 15:54
 * @desciption TaskLogModel
 */
@Setter
@Getter
@ToString
public class TaskLogModel implements Serializable {
    private String id;
    private String task_id;
    private String status;
    private InsertResult insert;
    private UpdateResult update;
    private LocalDateTime ct;
    private LocalDateTime st;
    private LocalDateTime et;
}
