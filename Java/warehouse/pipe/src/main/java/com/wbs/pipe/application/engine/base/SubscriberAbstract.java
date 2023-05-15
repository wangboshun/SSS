package com.wbs.pipe.application.engine.base;

import cn.hutool.core.util.EnumUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.wbs.common.database.DbUtils;
import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.pipe.application.ConnectApplication;
import com.wbs.pipe.model.engine.InsertResult;
import com.wbs.pipe.model.engine.UpdateResult;
import com.wbs.pipe.model.event.EventAbstractModel;
import com.wbs.pipe.model.sink.SinkInfoModel;
import com.wbs.pipe.model.task.TaskInfoModel;
import com.wbs.pipe.model.task.TaskLogModel;
import com.wbs.pipe.model.task.TaskStatusEnum;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author WBS
 * @date 2023/5/4 15:33
 * @desciption SubscriberAbstract
 */
@Component
public class SubscriberAbstract {
    private final Logger logger = LoggerFactory.getLogger(getClass());
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
     * 任务日志更新
     */
    public void taskLogUpdate(EventAbstractModel event, InsertResult insertResult, UpdateResult updateResult) {
        Bson query = Filters.eq("task_id", event.getTaskInfo().getId());
        Bson sort = Filters.eq("ct", -1);
        TaskLogModel model = taskLogCollection.find(query).sort(sort).first();
        if (model != null) {
            InsertResult insert = model.getInsert();
            UpdateResult update = model.getUpdate();
            if (insert == null) {
                insert = new InsertResult();
            }
            if (update == null) {
                update = new UpdateResult();
            }
            insert.insertTotal(insertResult);
            update.updateTotal(updateResult);
            model.setInsert(insert);
            model.setUpdate(update);
            if (event.isEnd()) {
                model.setEt(LocalDateTime.now());
                model.setStatus(TaskStatusEnum.COMPLETE.name());
            }
            query = Filters.eq("_id", model.getId());
            taskLogCollection.replaceOne(query, model);
            System.out.println(model.toString());
        }
    }

    public void process(EventAbstractModel message) {
        InsertResult insertResult = null;
        UpdateResult updateResult = null;
        try {
            if (writer != null) {
                writer.config(message.getSinkInfo().getTable_name(), connection, columnList);
                insertResult = writer.insertData(message.getTable());
                if (insertResult.getExistData() != null) {
                    // 如果是存在更新
                    if (writeTypeEnum.equals(WriteTypeEnum.UPSERT)) {
                        updateResult = writer.updateData(insertResult.getExistData());
                    }
                    // 如果是存在忽略
                    else if (writeTypeEnum.equals(WriteTypeEnum.IGNORE)) {
                        insertResult.setIgnoreCount(insertResult.getExistData().size());
                    }
                }
            }
            connection.close();
            taskLogUpdate(message, insertResult, updateResult);
        } catch (Exception e1) {
            logger.error("------SubscriberAbstract process error------", e1);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e2) {
                    logger.error("------SubscriberAbstract process error------", e2);
                }
            }
        }
        System.out.println(message.toString());
    }
}
