package com.wbs.pipe.model.engine;

import com.wbs.common.extend.BaseStatusModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author WBS
 * @date 2023/3/9 10:51
 * @desciption ColumnConfigModel
 */
@Setter
@Getter
public class ColumnConfigModel extends BaseStatusModel {
    private String task_id;
    private Map<String, String> mapper;
}
