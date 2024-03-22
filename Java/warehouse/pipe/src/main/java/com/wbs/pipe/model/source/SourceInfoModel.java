package com.wbs.pipe.model.source;


import com.wbs.common.extend.BaseStatusModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption SourceInfoModel
 */
@Setter
@Getter
public class SourceInfoModel extends BaseStatusModel {
    private String name;
    private String table_name;
    private String type;
    private String desc;
    private String connect_id;
}
