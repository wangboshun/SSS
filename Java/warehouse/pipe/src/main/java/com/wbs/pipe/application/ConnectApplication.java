package com.wbs.pipe.application;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.EnumUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.database.factory.ConnectionFactory;
import com.wbs.common.database.factory.DataSourceFactory;
import com.wbs.common.enums.HttpEnum;
import com.wbs.common.extend.ResponseResult;
import com.wbs.pipe.model.connect.ConnectInfoModel;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;

/**
 * @author WBS
 * @date 2023/3/8 14:43
 * @desciption ConnectApplication
 */
@Service
public class ConnectApplication {
    private final MongoCollection<ConnectInfoModel> collection;
    private final DataSourceFactory dataSourceFactory;
    private final ConnectionFactory connectionFactory;

    public ConnectApplication(MongoDatabase defaultMongoDatabase, DataSourceFactory dataSourceFactory, ConnectionFactory connectionFactory) {
        this.collection = defaultMongoDatabase.getCollection("connect_info", ConnectInfoModel.class);
        this.dataSourceFactory = dataSourceFactory;
        this.connectionFactory = connectionFactory;
    }

    /**
     * 获取连接
     *
     * @param connectId
     * @return
     */
    public Connection getConnection(String connectId) {
        try {
            ConnectInfoModel model = getConnectInfo(connectId, null);
            String host = model.getHost();
            int port = model.getPort();
            String username = model.getUsername();
            String password = model.getPassword();
            String database = model.getDatabase();
            String schema = model.getSchema();
            DbTypeEnum dbType = EnumUtil.getEnumMap(DbTypeEnum.class).get(model.getType().toUpperCase());
            DataSource dataSource = dataSourceFactory.createDataSource(model.getName(), host, port, username, password, database, schema, dbType);
            return connectionFactory.createConnection(model.getName(), dataSource);
        } catch (Exception e) {
            return null;
        }
    }

    public List<ConnectInfoModel> getConnectList() {
        List<ConnectInfoModel> list = new ArrayList<>();
        FindIterable<ConnectInfoModel> iterable = collection.find();
        iterable.into(list);
        return list;
    }

    public ConnectInfoModel getConnectInfo(String id, String name) {
        List<Bson> query = new ArrayList<>();
        if (CharSequenceUtil.isNotBlank(id)) {
            query.add(eq("_id", id));
        }
        if (CharSequenceUtil.isNotBlank(name)) {
            query.add(eq("name", name));
        }
        if (query.isEmpty()) {
            return null;
        }
        return collection.find(or(query)).first();
    }

    public ResponseResult addConnect(ConnectInfoModel model) {
        ConnectInfoModel info = getConnectInfo(null, model.getName());
        if (info != null) {
            return new ResponseResult().ERROR(HttpEnum.EXISTS);
        }
        try {
            model.setCt(LocalDateTime.now());
            model.setUt(null);
            ObjectId id = new ObjectId();
            model.setId(id.toString());
            collection.insertOne(model);
            return new ResponseResult().OK();
        } catch (Exception e) {
            return new ResponseResult().ERROR(HttpEnum.EXCEPTION);
        }
    }

    public ResponseResult deleteConnect(String id) {
        Bson query = Filters.eq("_id", id);
        DeleteResult result = collection.deleteOne(query);
        if (result.getDeletedCount() > 0) {
            return new ResponseResult().OK();
        } else {
            return new ResponseResult().FAILED();
        }
    }

    public ResponseResult deleteAll() {
        List<ConnectInfoModel> taskList = getConnectList();
        taskList.forEach(x -> {
            Bson query = Filters.eq("_id", x.getId());
            collection.deleteOne(query);
        });
        return new ResponseResult().OK();
    }

    public ResponseResult updateConnect(ConnectInfoModel model) {
        if (CharSequenceUtil.isBlank(model.getId())) {
            return new ResponseResult().ERROR("id不可为空！", HttpEnum.PARAM_VALID_ERROR);
        }
        Bson query = Filters.eq("_id", model.getId());
        ConnectInfoModel old = collection.find(query).first();
        if (old != null) {
            model.setUt(LocalDateTime.now());
            model.setCt(old.getCt());
            UpdateResult result = collection.replaceOne(query, model);
            if (result.getModifiedCount() > 0) {
                return new ResponseResult().OK();
            } else {
                return new ResponseResult().FAILED();
            }
        } else {
            return new ResponseResult().NULL();
        }
    }
}
