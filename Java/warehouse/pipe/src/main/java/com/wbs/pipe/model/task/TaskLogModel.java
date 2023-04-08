package com.wbs.pipe.model.task;

import com.wbs.engine.model.WriterResult;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author WBS
 * @date 2023/4/7 15:54
 * @desciption TaskLogModel
 */
@Setter
@Getter
public class TaskLogModel implements Serializable {
    private String id;
    private String task_id;
    private String status;
    private WriterResult insert;
    private WriterResult update;
    private LocalDateTime ct;
    private LocalDateTime st;
    private LocalDateTime et;
}
