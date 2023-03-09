package com.wbs.pipe.application;

import cn.hutool.core.util.StrUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.wbs.common.enums.HttpEnum;
import com.wbs.common.extend.ResponseResult;
import com.wbs.pipe.model.connect.ConnectInfoModel;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

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
        List<Bson> query = new ArrayList<>();
        if (StrUtil.isNotBlank(id)) {
            query.add(eq("_id", id));
        }
        if (StrUtil.isNotBlank(name)) {
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
            model.setCreate_time(LocalDateTime.now());
            model.setUpdate_time(null);
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
        if (StrUtil.isBlank(model.getId())) {
            return new ResponseResult().ERROR("id不可为空！", HttpEnum.PARAM_VALID_ERROR);
        }
        Bson query = Filters.eq("_id", model.getId());
        ConnectInfoModel old = collection.find(query).first();
        if (old != null) {
            model.setUpdate_time(LocalDateTime.now());
            model.setCreate_time(old.getCreate_time());
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
