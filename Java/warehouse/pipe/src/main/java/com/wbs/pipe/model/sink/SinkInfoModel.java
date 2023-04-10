package com.wbs.pipe.model.sink;


import com.wbs.common.extend.BaseStatusModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption SinkInfoModel
 */
@Setter
@Getter
public class SinkInfoModel extends BaseStatusModel {
    private String name;
    private String table_name;
    private String type;
    private String desc;
    private String connect_id;
}
