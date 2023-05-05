package com.wbs.pipe.application.engine.base;

import cn.hutool.core.util.EnumUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import com.wbs.common.database.DbUtils;
import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.pipe.application.ConnectApplication;
import com.wbs.pipe.model.engine.WriterResult;
import com.wbs.pipe.model.event.EventAbstractModel;
import com.wbs.pipe.model.sink.SinkInfoModel;
import com.wbs.pipe.model.task.TaskInfoModel;
import com.wbs.pipe.model.task.TaskLogModel;
import com.wbs.pipe.model.task.TaskStatusEnum;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author WBS
 * @date 2023/5/4 15:33
 * @desciption SubscriberAbstract
 */
@Component
public class SubscriberAbstract {
    private final MongoDatabase defaultMongoDatabase;
    private final MongoCollection<TaskLogModel> taskLogCollection;
    private final ConnectApplication connectApplication;
    protected WriteTypeEnum writeTypeEnum;
    protected IWriter writer;
    protected Connection connection;
    protected List<ColumnInfo> columnList;

    public SubscriberAbstract(ConnectApplication connectApplication) {
        this.connectApplication = connectApplication;
        defaultMongoDatabase = SpringUtil.getBean("defaultMongoDatabase");
        this.taskLogCollection = defaultMongoDatabase.getCollection("task_log", TaskLogModel.class);
    }

    public void config(TaskInfoModel taskInfo, SinkInfoModel sinkInfo, DbTypeEnum dbTypeEnum) {
        connection = connectApplication.getConnection(sinkInfo.getConnect_id());
        columnList = DbUtils.getColumns(connection, sinkInfo.getTable_name());
        writeTypeEnum = EnumUtil.getEnumMap(WriteTypeEnum.class).get(taskInfo.getExist_type().toUpperCase());
        writer = EngineManager.getWriter(dbTypeEnum);
    }

    /**
     * 任务结束
     */
    public void taskEnd(EventAbstractModel event, WriterResult insertResult, WriterResult updateResult) {
        Bson query = Filters.eq("task_id", event.getTaskInfo().getId());
        Bson sort = Filters.eq("ct", -1);
        TaskLogModel model = taskLogCollection.find(query).sort(sort).first();
        if (model != null) {
            WriterResult insert = model.getInsert();
            WriterResult update = model.getUpdate();
            if (insert == null) {
                insert = new WriterResult();
            }
            if (update == null) {
                update = new WriterResult();
            }
            insert.total(insertResult, updateResult);
            update.total(insertResult, updateResult);
            model.setInsert(insert);
            model.setUpdate(update);
            if (event.isEnd()) {
                model.setEt(LocalDateTime.now());
                model.setStatus(TaskStatusEnum.COMPLETE.name());
            }
            query =Filters.eq("_id", model.getId());
            UpdateResult result = taskLogCollection.replaceOne(query, model);
            if (result.getModifiedCount() > 0) {

            } else {

            }
            System.out.println(model.toString());
        }
    }
}
