package com.wbs.pipe.model.sink;


import org.bson.types.ObjectId;

public class SinkInfoModel {
    private ObjectId id;
    private String name;
    private String connect_id;
    private String create_time;
    private int sink_status;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConnect_id() {
        return connect_id;
    }

    public void setConnect_id(String connect_id) {
        this.connect_id = connect_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getSink_status() {
        return sink_status;
    }

    public void setSink_status(int sink_status) {
        this.sink_status = sink_status;
    }
}
