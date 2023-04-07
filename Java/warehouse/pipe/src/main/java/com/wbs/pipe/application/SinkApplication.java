package com.wbs.pipe.application;

import cn.hutool.core.text.CharSequenceUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.wbs.common.database.DbUtils;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.common.database.base.model.TableInfo;
import com.wbs.common.enums.HttpEnum;
import com.wbs.common.extend.ResponseResult;
import com.wbs.pipe.model.sink.SinkInfoModel;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.sql.Connection;
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
    private final ConnectApplication connectApplication;

    public SinkApplication(MongoDatabase defaultMongoDatabase, ConnectApplication connectApplication) {
        this.collection = defaultMongoDatabase.getCollection("sink_info", SinkInfoModel.class);
        this.connectApplication = connectApplication;
    }

    public List<SinkInfoModel> getSinkList() {
        List<SinkInfoModel> list = new ArrayList<>();
        FindIterable<SinkInfoModel> iterable = collection.find();
        iterable.into(list);
        return list;
    }

    public SinkInfoModel getSink(String id, String name) {
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

    public ResponseResult addSink(SinkInfoModel model) {
        SinkInfoModel info = getSink(null, model.getName());
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

    public ResponseResult deleteSink(String id) {
        Bson query = Filters.eq("_id", id);
        DeleteResult result = collection.deleteOne(query);
        if (result.getDeletedCount() > 0) {
            return new ResponseResult().OK();
        } else {
            return new ResponseResult().FAILED();
        }
    }

    public ResponseResult deleteAll() {
        List<SinkInfoModel> taskList = getSinkList();
        taskList.forEach(x -> {
            Bson query = Filters.eq("_id", x.getId());
            collection.deleteOne(query);
        });
        return new ResponseResult().OK();
    }

    public ResponseResult updateSink(SinkInfoModel model) {
        if (CharSequenceUtil.isBlank(model.getId())) {
            return new ResponseResult().ERROR("id不可为空！", HttpEnum.PARAM_VALID_ERROR);
        }
        Bson query = Filters.eq("_id", model.getId());
        SinkInfoModel old = collection.find(query).first();
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

    public List<TableInfo> getTables(String id) {
        SinkInfoModel sinkInfo = this.getSink(id, null);
        Connection connection = connectApplication.getConnection(sinkInfo.getConnect_id());
        return DbUtils.getTables(connection);
    }

    public List<ColumnInfo> getColumns(String id, String table) {
        SinkInfoModel sourceInfo = this.getSink(id, null);
        Connection connection = connectApplication.getConnection(sourceInfo.getConnect_id());
        return DbUtils.getColumns(connection, table);
    }
}
