package com.wbs.pipe.application;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.wbs.pipe.model.sink.SinkInfoModel;
import org.bson.Document;
import org.bson.conversions.Bson;
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
        MongoCollection<Document> collection = defaultMongoDatabase.getCollection("sink_info");
//      FindIterable<Document> documents = collection.find(Filters.eq("name", "test"));
        FindIterable<Document> documents = collection.find();
        for (Document document : documents) {
            System.out.println(document.toString());
        }

        MongoCollection<SinkInfoModel> list = defaultMongoDatabase.getCollection("sink_info", SinkInfoModel.class);
        FindIterable<SinkInfoModel> models = list.find();
        for (SinkInfoModel model : models) {
            System.out.println(model.toString());
        }
    }

    public void add() {
        SinkInfoModel model = new SinkInfoModel();
        model.setName("test");
        model.setConnect_id("112233");
        model.setSink_status(2);
        model.setCreate_time("2022-01-01 22:22:22");

        MongoCollection<SinkInfoModel> list = defaultMongoDatabase.getCollection("sink_info", SinkInfoModel.class);
        InsertOneResult insertOneResult = list.insertOne(model);
    }

    public void remove() {
        MongoCollection<Document> collection = defaultMongoDatabase.getCollection("sink_info");
        Bson query = Filters.eq("name", "12222");
        DeleteResult deleteResult = collection.deleteOne(query);
    }

    public void update() {
        MongoCollection<Document> collection = defaultMongoDatabase.getCollection("sink_info");
        Bson query = Filters.eq("name", "test");
        Bson update = Updates.set("sink_status", 20);
        collection.updateMany(query, update);
    }
}
