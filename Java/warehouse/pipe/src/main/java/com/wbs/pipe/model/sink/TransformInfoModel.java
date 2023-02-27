package com.wbs.pipe.model.sink;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transform_info")
public class TransformInfoModel {
    private ObjectId id;
    private String name;
    private String connect_id;
    private String create_time;
    private int sink_status;
}
