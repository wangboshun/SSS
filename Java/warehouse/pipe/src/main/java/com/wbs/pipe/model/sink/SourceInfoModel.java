package com.wbs.pipe.model.sink;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "source_info")
public class SourceInfoModel {
    private ObjectId id;
    private String name;
    private String connect_id;
    private String create_time;
    private int sink_status;


    public static final class Builder {
        private ObjectId id;
        private String name;
        private String connect_id;
        private String create_time;
        private int sink_status;

        private Builder() {
        }

        public Builder id(ObjectId id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder connect_id(String connect_id) {
            this.connect_id = connect_id;
            return this;
        }

        public Builder create_time(String create_time) {
            this.create_time = create_time;
            return this;
        }

        public Builder sink_status(int sink_status) {
            this.sink_status = sink_status;
            return this;
        }

        public SourceInfoModel build() {
            SourceInfoModel sinkInfoModel = new SourceInfoModel();
            sinkInfoModel.connect_id = this.connect_id;
            sinkInfoModel.sink_status = this.sink_status;
            sinkInfoModel.id = this.id;
            sinkInfoModel.create_time = this.create_time;
            sinkInfoModel.name = this.name;
            return sinkInfoModel;
        }
    }
}
