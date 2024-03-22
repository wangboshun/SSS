package org.example.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertManyResult;
import org.bson.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class Test {
    public static void main(String[] args) {
//        Test_TS();
        Test();
//        CRUDE();
    }

    public static void CRUDE() {
        String uri = "mongodb://admin:123456@127.0.0.1:27017/?maxPoolSize=20&w=majority";
//        String uri = "mongodb://admin:mima123456mima@123.60.141.63:10010/?maxPoolSize=20&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("test_db");
            MongoCollection<Document> collection = database.getCollection("sensor_test_2");
            Query(collection);
        }
    }

    public static void Query(MongoCollection<Document> collection) {
//        FindIterable<Document> documents = collection.find();
//        for (Document doc : documents) {
//            System.out.println(doc.toJson());
//        }
        System.out.println(LocalDateTime.now());

        try {
            Instant instant = Instant.parse("2005-01-01T01:35:00.000Z");
            Date timestamp = Date.from(instant);
            Document document = collection.find(eq("TM", timestamp)).first();
            System.out.println(LocalDateTime.now());
            System.out.println("指定查询：" + document.toJson());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void Update(MongoCollection<Document> collection) {
        Document updateDoc = new Document();
        updateDoc.append("abc", "112233");
        updateDoc.append("efg", "445566");
        collection.updateOne(eq("_id", 2), new Document("$set", updateDoc));
        System.out.println("update");
    }

    public static void Add(MongoCollection<Document> collection) {
        Document addDoc = new Document();
        addDoc.append("_id", 2);
        addDoc.append("abc", "1123");
        addDoc.append("efg", "4456");
        collection.insertOne(addDoc);
        System.out.println("add");
    }

    public static void AddList(MongoCollection<Document> collection) {
        List<Document> movieList = Arrays.asList(new Document().append("_id", 3).append("title", "Short Circuit 3"), new Document().append("_id", 4).append("title", "The Lego Frozen Movie"));
        InsertManyResult result = collection.insertMany(movieList);
        System.out.println(result.getInsertedIds());
    }

    public static void Delete(MongoCollection<Document> collection) {
        collection.deleteMany(eq("_id", "3"));
        System.out.println("delete");
    }

    public static void Test() {
//        String uri = "mongodb://admin:mima123456mima@123.60.141.63:10010/?maxPoolSize=20&w=majority";
        String uri = "mongodb://admin:123456@127.0.0.1:27017/?maxPoolSize=20&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("test_db");
            MongoCollection<Document> collection = database.getCollection("sensor_test_2");
            List<String> stcd_array = new ArrayList<>();
            List<LocalDateTime> tm_array = new ArrayList<>();
            for (int i = 10000000; i < 10010000; i++) {
                stcd_array.add(i + "");
            }
            LocalDateTime t = LocalDateTime.of(2000, 01, 01, 0, 0, 0);
            for (int i = 0; i < 10000; i++) {
                tm_array.add(t.plusMinutes(i * 5));
            }
            System.out.println(LocalDateTime.now());
            BigDecimal v = new BigDecimal("100.12");
            Document doc = new Document();
            doc.put("AA", v);
            doc.put("BB", v);
            doc.put("CC", v);
            doc.put("DD", v);
            doc.put("EE", v);
            doc.put("FF", v);
            doc.put("GG", v);
            doc.put("HH", v);
            doc.put("II", v);
            doc.put("JJ", v);
            for (String stcd : stcd_array) {
//                new Thread(() -> {
                List<Document> sensorList = new ArrayList<>();
                doc.put("STCD", stcd);
                for (LocalDateTime tm : tm_array) {
                    doc.put("TM", tm);
                    sensorList.add(doc);
                    if (sensorList.size() > 1000) {
                        InsertManyResult manyResult = collection.insertMany(sensorList);
                        sensorList.clear();
                    }
                }
                if (sensorList.size() > 0) {
                    InsertManyResult manyResult = collection.insertMany(sensorList);
                    sensorList.clear();
                }
//                }, "线程---" + stcd).start();
            }
            System.out.println(LocalDateTime.now());
        }
    }


    public static void Test_TS() {
        //  创建时序集合：db.createCollection( "sensor_ts", { timeseries: { timeField: "TM", metaField: "data", granularity: "hours" } } )
        //  String uri = "mongodb://admin:mima123456mima@123.60.141.63:10010/?maxPoolSize=20&w=majority";
        String uri = "mongodb://admin:123456@127.0.0.1:27017/?maxPoolSize=20&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("test_db");
            MongoCollection<Document> collection = database.getCollection("sensor_ts_2");
            List<String> stcd_array = new ArrayList<>();
            List<LocalDateTime> tm_array = new ArrayList<>();

            for (int i = 10000000; i < 10010000; i++) {
                stcd_array.add(i + "");
            }
            LocalDateTime t = LocalDateTime.of(2000, 01, 01, 0, 0, 0);
            for (int i = 0; i < 10000; i++) {
                tm_array.add(t.plusMinutes(i * 5));
            }
            System.out.println(LocalDateTime.now());
            BigDecimal v = new BigDecimal("100.12");
            Document doc = new Document();
            Map<String, BigDecimal> children = new HashMap<>();
            children.put("AA", v);
            children.put("BB", v);
            children.put("CC", v);
            children.put("DD", v);
            children.put("EE", v);
            children.put("FF", v);
            children.put("GG", v);
            children.put("HH", v);
            children.put("II", v);
            children.put("JJ", v);
            doc.put("data", children);
            for (String stcd : stcd_array) {
                doc.put("STCD", stcd);
//                new Thread(() -> {
                List<Document> sensorList = new ArrayList<>();
                for (LocalDateTime tm : tm_array) {
                    doc.put("TM", tm);
                    sensorList.add(doc);
                    if (sensorList.size() > 1000) {
                        InsertManyResult manyResult = collection.insertMany(sensorList);
                        sensorList.clear();
                    }
                }
                if (sensorList.size() > 0) {
                    InsertManyResult manyResult = collection.insertMany(sensorList);
                    sensorList.clear();
                }
//                }, "线程---" + stcd).start();
            }
            System.out.println(LocalDateTime.now());
        }
    }
}
