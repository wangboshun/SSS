package com.wbs.pipe.model.task;

import com.wbs.common.extend.BaseStatusModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author WBS
 * @date 2023/3/9 11:04
 * @desciption TaskInfoModel
 */
@Setter
@Getter
public class TaskInfoModel extends BaseStatusModel {
    private String name;
    private int type;
    private String desc;
    private String sink_id;
    private String source_id;
}
