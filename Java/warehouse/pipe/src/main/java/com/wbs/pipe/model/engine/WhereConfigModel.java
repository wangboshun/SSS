package com.wbs.pipe.model.engine;

import com.wbs.common.database.base.model.WhereInfo;
import com.wbs.common.extend.BaseStatusModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author WBS
 * @date 2023/3/9 10:51
 * @desciption WhereConfigModel
 */
@Setter
@Getter
public class WhereConfigModel extends BaseStatusModel {
    private String task_id;
    private List<WhereInfo> filter;
}
