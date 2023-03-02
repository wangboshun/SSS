package com.wbs.common.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.wbs.common.database.MongoFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @author WBS
 * @date 2023/3/1 9:59
 * @desciption MongoConfig
 */
@Configuration
public class MongoConfig {
    @Value("${mongodb.host}")
    private String host;
    @Value("${mongodb.port}")
    private int port;
    @Value("${mongodb.username}")
    private String username;
    @Value("${mongodb.password}")
    private String password;
    @Value("${mongodb.database}")
    private String database;
    private final MongoFactory mongoFactory;
    private MongoClient mongoClient;

    public MongoConfig(MongoFactory mongoFactory) {
        this.mongoFactory = mongoFactory;
    }

    @Bean("defaultMongoClient")
    public MongoClient mongoClient() {
        mongoClient = this.mongoFactory.createClient("default", host, port, username, password, database);
        return mongoClient;
    }

    @Bean("defaultMongoDatabase")
    @DependsOn(value = {"defaultMongoClient"})
    public MongoDatabase mongoDatabase() {
        return mongoFactory.getDatabase(mongoClient, database);
    }
}
