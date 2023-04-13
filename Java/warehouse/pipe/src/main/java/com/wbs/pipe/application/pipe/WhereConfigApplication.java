package com.wbs.pipe.application.pipe;

import cn.hutool.core.text.CharSequenceUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.wbs.common.database.base.model.WhereInfo;
import com.wbs.common.enums.HttpEnum;
import com.wbs.common.extend.ResponseResult;
import com.wbs.pipe.model.pipe.WhereConfigModel;
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
 * @desciption WhereConfigApplication
 */
@Service
public class WhereConfigApplication {
    private final MongoCollection<WhereConfigModel> collection;

    public WhereConfigApplication(MongoDatabase defaultMongoDatabase) {
        this.collection = defaultMongoDatabase.getCollection("where_config", WhereConfigModel.class);
    }

    public List<WhereConfigModel> getWhereConfigList() {
        List<WhereConfigModel> list = new ArrayList<>();
        FindIterable<WhereConfigModel> iterable = collection.find();
        iterable.into(list);
        return list;
    }

    public List<WhereInfo> getWhereConfigByTask(String taskId) {
        if (CharSequenceUtil.isNotBlank(taskId)) {
            Bson query = eq("task_id", taskId);
            WhereConfigModel model = collection.find(query).first();
            if(model != null){
                return model.getFilter();
            }
            return null;
        } else {
            return null;
        }
    }

    public ResponseResult addWhereConfig(WhereConfigModel model) {
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

    public ResponseResult deleteWhereConfig(String id) {
        Bson query = eq("_id", id);
        DeleteResult result = collection.deleteOne(query);
        if (result.getDeletedCount() > 0) {
            return new ResponseResult().OK();
        } else {
            return new ResponseResult().FAILED();
        }
    }

    public ResponseResult deleteAll() {
        List<WhereConfigModel> taskList = getWhereConfigList();
        taskList.forEach(x -> {
            Bson query = Filters.eq("_id", x.getId());
            collection.deleteOne(query);
        });
        return new ResponseResult().OK();
    }

    public ResponseResult updateWhereConfig(WhereConfigModel model) {
        if (CharSequenceUtil.isBlank(model.getId())) {
            return new ResponseResult().ERROR("id不可为空！", HttpEnum.PARAM_VALID_ERROR);
        }
        Bson query = eq("_id", model.getId());
        WhereConfigModel old = collection.find(query).first();
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
