package com.wbs.pipe.model.sink;


import org.bson.types.ObjectId;

import java.io.Serializable;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption SourceInfoModel
 */
public class SourceInfoModel implements Serializable {
    private ObjectId id;
    private String name;
    private String connect_id;
    private String create_time;
    private int sink_status;
}
