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
import com.wbs.pipe.model.ColumnConfigModel;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WBS
 * @date 2023/3/9 10:54
 * @desciption ColumnConfigApplication
 */
@Service
public class ColumnConfigApplication {
    private final MongoCollection<ColumnConfigModel> collection;

    public ColumnConfigApplication(MongoDatabase defaultMongoDatabase) {
        this.collection = defaultMongoDatabase.getCollection("column_config", ColumnConfigModel.class);
    }

    public ResponseResult getColumnConfigList() {
        List<ColumnConfigModel> list = new ArrayList<>();
        FindIterable<ColumnConfigModel> iterable = collection.find();
        iterable.into(list);
        if (list.isEmpty()) {
            return new ResponseResult().NULL();
        } else {
            return new ResponseResult().OK(list);
        }
    }

    public ResponseResult getColumnConfig(String id, String taskId) {
        Bson query;
        if (StrUtil.isNotBlank(id)) {
            query = Filters.eq("_id", id);
        } else if (StrUtil.isNotBlank(taskId)) {
            query = Filters.eq("task_id", taskId);
        } else {
            return new ResponseResult().NULL();
        }
        ColumnConfigModel model = collection.find(query).first();
        if (model == null) {
            return new ResponseResult().NULL();
        } else {
            return new ResponseResult().OK(model);
        }
    }

    public ResponseResult addColumnConfig(ColumnConfigModel model) {
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

    public ResponseResult deleteColumnConfig(String id) {
        Bson query = Filters.eq("_id", id);
        DeleteResult result = collection.deleteOne(query);
        if (result.getDeletedCount() > 0) {
            return new ResponseResult().OK();
        } else {
            return new ResponseResult().FAILED();
        }
    }

    public ResponseResult updateColumnConfig(ColumnConfigModel model) {
        if (StrUtil.isBlank(model.getId())) {
            return new ResponseResult().ERROR("id不可为空！", HttpEnum.PARAM_VALID_ERROR);
        }
        Bson query = Filters.eq("_id", model.getId());
        ColumnConfigModel old = collection.find(query).first();
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
