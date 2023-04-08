package com.wbs.pipe.model.connect;

import com.wbs.common.extend.BaseStatusModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author WBS
 * @date 2023/3/8 14:37
 * @desciption ConnectInfoModel
 */
@Setter
@Getter
public class ConnectInfoModel extends BaseStatusModel {

    private String name;
    private String host;
    private int port;
    private String username;
    private String password;
    private String database;
    private String schema;
    private int type;
    private String desc;
}
