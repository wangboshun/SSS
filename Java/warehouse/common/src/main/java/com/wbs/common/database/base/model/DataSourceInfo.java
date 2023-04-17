package com.wbs.common.database.base.model;

import com.wbs.common.database.base.DbTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author WBS
 * @date 2023/4/15 12:12
 * @desciption DataSourceInfo
 */

@Setter
@Getter
public class DataSourceInfo {
    private String name;
    private String url;
    private int port;
    private String host;
    private String username;
    private String password;
    private String database;
    private String schema;
    private DbTypeEnum dbType;
}
