package com.wbs.pipe.application;

import cn.hutool.core.util.StrUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.wbs.common.extend.ResponseResult;
import com.wbs.pipe.model.connect.ConnectInfoModel;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WBS
 * @date 2023/3/8 14:43
 * @desciption ConnectApplication
 */
@Service
public class ConnectApplication {
    private final MongoCollection<ConnectInfoModel> collection;

    public ConnectApplication(MongoDatabase defaultMongoDatabase) {
        this.collection = defaultMongoDatabase.getCollection("connect_info", ConnectInfoModel.class);
    }

    public List<ConnectInfoModel> getConnectList() {
        List<ConnectInfoModel> list = new ArrayList<>();
        FindIterable<ConnectInfoModel> iterable = collection.find();
        iterable.into(list);
        return list;
    }

    public ConnectInfoModel getConnectInfo(String id, String name) {
        Bson query;
        if (StrUtil.isNotBlank(id)) {
            query = Filters.eq("_id", id);
        } else if (StrUtil.isNotBlank(name)) {
            query = Filters.eq("name", name);
        } else {
            return null;
        }
        return collection.find(query).first();
    }

    public ResponseResult addConnect(ConnectInfoModel model) {
        ConnectInfoModel info = getConnectInfo(null, model.getName());
        if (info != null) {
            return new ResponseResult().Error("名称已存在!");
        }
        try {
            ObjectId id = new ObjectId();
            model.setId(id.toString());
            collection.insertOne(model);
            return new ResponseResult().Ok("添加成功!");
        } catch (Exception e) {
            return new ResponseResult().Error("添加失败!");
        }
    }

    public boolean deleteConnect(String id) {
        Bson query;
        if (StrUtil.isNotBlank(id)) {
            query = Filters.eq("_id", id);
        } else {
            return false;
        }
        DeleteResult result = collection.deleteOne(query);
        return result.getDeletedCount() > 0;
    }

    public boolean updateConnect(ConnectInfoModel model) {
        Bson query = Filters.eq("_id", model.getId());
        UpdateResult result = collection.replaceOne(query, model);
        return result.getModifiedCount() > 0;
    }
}
