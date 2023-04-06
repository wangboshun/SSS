package com.wbs.pipe.application;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.wbs.common.enums.HttpEnum;
import com.wbs.common.extend.ResponseResult;
import com.wbs.pipe.model.source.SourceInfoModel;
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
 * @desciption SourceApplication
 */
@Service()
public class SourceApplication {
    private final MongoCollection<SourceInfoModel> collection;

    public SourceApplication(MongoDatabase defaultMongoDatabase) {
        this.collection = defaultMongoDatabase.getCollection("source_info", SourceInfoModel.class);
    }

    public List<SourceInfoModel> getSourceList() {
        List<SourceInfoModel> list = new ArrayList<>();
        FindIterable<SourceInfoModel> iterable = collection.find();
        iterable.into(list);
        return list;
    }

    public SourceInfoModel getSource(String id, String name) {
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

    public ResponseResult addSource(SourceInfoModel model) {
        SourceInfoModel info = getSource(null, model.getName());
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

    public ResponseResult deleteSource(String id) {
        Bson query = Filters.eq("_id", id);
        DeleteResult result = collection.deleteOne(query);
        if (result.getDeletedCount() > 0) {
            return new ResponseResult().OK();
        } else {
            return new ResponseResult().FAILED();
        }
    }

    public ResponseResult deleteAll() {
        List<SourceInfoModel> taskList = getSourceList();
        taskList.forEach(x -> {
            Bson query = Filters.eq("_id", x.getId());
            collection.deleteOne(query);
        });
        return new ResponseResult().OK();
    }

    public ResponseResult updateSource(SourceInfoModel model) {
        if (CharSequenceUtil.isBlank(model.getId())) {
            return new ResponseResult().ERROR("id不可为空！", HttpEnum.PARAM_VALID_ERROR);
        }
        Bson query = Filters.eq("_id", model.getId());
        SourceInfoModel old = collection.find(query).first();
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
