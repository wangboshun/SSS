package com.wbs.pipe.application;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.wbs.pipe.model.sink.SinkInfoModel;
import org.bson.Document;
import org.springframework.stereotype.Service;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption SinkApplication
 */
@Service
public class SinkApplication {
    private final MongoDatabase defaultMongoDatabase;

    public SinkApplication(MongoDatabase defaultMongoDatabase) {
        this.defaultMongoDatabase = defaultMongoDatabase;
    }

    public void query() {
        MongoCollection<Document> collection =defaultMongoDatabase.getCollection("sink_info");
        FindIterable<Document> documents = collection.find();
        for (Document document : documents) {
            System.out.println(document.toString());
        }
    }

    public void add() {

        SinkInfoModel model = new SinkInfoModel();
        model.setName("test");
        model.setConnect_id("123");
        model.setSink_status(1);
        model.setCreate_time("2022-01-01 22:22:22");

    }

    public void remove() {

    }

    public void update() {

    }
}
