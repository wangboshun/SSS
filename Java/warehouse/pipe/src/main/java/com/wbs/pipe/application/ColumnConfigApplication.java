package com.wbs.pipe.application;

import cn.hutool.core.text.CharSequenceUtil;
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

import static com.mongodb.client.model.Filters.eq;

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

    public List<ColumnConfigModel> getColumnConfigList() {
        List<ColumnConfigModel> list = new ArrayList<>();
        FindIterable<ColumnConfigModel> iterable = collection.find();
        iterable.into(list);
        return list;
    }

    public ColumnConfigModel getColumnConfigByTask(String taskId) {
        if (CharSequenceUtil.isNotBlank(taskId)) {
            Bson query = eq("task_id", taskId);
            return collection.find(query).first();
        } else {
            return null;
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
        Bson query = eq("_id", id);
        DeleteResult result = collection.deleteOne(query);
        if (result.getDeletedCount() > 0) {
            return new ResponseResult().OK();
        } else {
            return new ResponseResult().FAILED();
        }
    }

    public ResponseResult deleteAll() {
        List<ColumnConfigModel> taskList = getColumnConfigList();
        taskList.forEach(x -> {
            Bson query = Filters.eq("_id", x.getId());
            collection.deleteOne(query);
        });
        return new ResponseResult().OK();
    }

    public ResponseResult updateColumnConfig(ColumnConfigModel model) {
        if (CharSequenceUtil.isBlank(model.getId())) {
            return new ResponseResult().ERROR("id不可为空！", HttpEnum.PARAM_VALID_ERROR);
        }
        Bson query = eq("_id", model.getId());
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
