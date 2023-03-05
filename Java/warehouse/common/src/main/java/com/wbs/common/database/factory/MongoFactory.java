package com.wbs.common.database.factory;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WBS
 * @date 2023/3/1 10:47
 * @desciption MongoFactory
 */
@Component
public class MongoFactory {
    private final HashMap<String, MongoClient> clientMap = new HashMap<>();

    /**
     * 获取所有客户端
     *
     * @return
     */
    public Map<String, MongoClient> getAllClient() {
        return clientMap;
    }

    /**
     * 移除客户端
     */
    public void removeClient(String clientName) {
        clientMap.remove(clientName);
    }

    /**
     * 获取客户端
     */
    public MongoClient getClient(String clientName) {
        return clientMap.get(clientName);
    }

    /**
     * 获取默认客户端
     */
    public MongoClient getClient() {
        return getClient("default");
    }

    /**
     * 获取数据库
     *
     * @param client
     * @param database
     * @return
     */
    public MongoDatabase getDatabase(MongoClient client, String database) {
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), CodecRegistries.fromProviders(pojoCodecProvider));
        return client.getDatabase(database).withCodecRegistry(pojoCodecRegistry);
    }

    /**
     * 创建客户端
     *
     * @param clientName
     * @param host
     * @param port
     * @param username
     * @param password
     * @param database
     * @return
     */
    public MongoClient createClient(String clientName, String host, int port, String username, String password, String database) {
        MongoClient mongoClient = clientMap.get(clientName);
        if (mongoClient != null) {
            return mongoClient;
        }
        String uri = "mongodb://" + username + ":" + password + "@" + host + ":" + port + "/?authSource=" + database;
        try {
            mongoClient = MongoClients.create(uri);
            MongoDatabase db = mongoClient.getDatabase("admin");
            Bson command = new BsonDocument("ping", new BsonInt64(1));
            Document commandResult = db.runCommand(command);
            if (commandResult.containsKey("ok")) {
                clientMap.put(clientName, mongoClient);
                return mongoClient;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
