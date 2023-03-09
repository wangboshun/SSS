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
import com.wbs.pipe.model.sink.SinkInfoModel;
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
 * @date 2023/2/23 9:59
 * @desciption SinkApplication
 */
@Service
public class SinkApplication {
    private final MongoCollection<SinkInfoModel> collection;

    public SinkApplication(MongoDatabase defaultMongoDatabase) {
        this.collection = defaultMongoDatabase.getCollection("sink_info", SinkInfoModel.class);
    }

    public ResponseResult getSinkList() {
        List<SinkInfoModel> list = new ArrayList<>();
        FindIterable<SinkInfoModel> iterable = collection.find();
        iterable.into(list);
        if (list.isEmpty()) {
            return new ResponseResult().NULL();
        } else {
            return new ResponseResult().OK(list);
        }
    }

    public ResponseResult getSink(String id, String name) {
        List<Bson> query = new ArrayList<>();
        if (StrUtil.isNotBlank(id)) {
            query.add(eq("_id", id));
        }
        if (StrUtil.isNotBlank(name)) {
            query.add(eq("name", name));
        } else {
            return new ResponseResult().NULL();
        }
        SinkInfoModel model = collection.find(or(query)).first();
        if (model == null) {
            return new ResponseResult().NULL();
        } else {
            return new ResponseResult().OK(model);
        }
    }

    public ResponseResult addSink(SinkInfoModel model) {
        ResponseResult info = getSink(null, model.getName());
        if (info.getData() != null) {
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

    public ResponseResult deleteSink(String id) {
        Bson query = Filters.eq("_id", id);
        DeleteResult result = collection.deleteOne(query);
        if (result.getDeletedCount() > 0) {
            return new ResponseResult().OK();
        } else {
            return new ResponseResult().FAILED();
        }
    }

    public ResponseResult updateSink(SinkInfoModel model) {
        if (StrUtil.isBlank(model.getId())) {
            return new ResponseResult().ERROR("id不可为空！", HttpEnum.PARAM_VALID_ERROR);
        }
        Bson query = Filters.eq("_id", model.getId());
        SinkInfoModel old = collection.find(query).first();
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
