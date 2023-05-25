package com.wbs.pipe.application.engine.base.db;

import cn.hutool.core.util.EnumUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.wbs.common.database.DbUtils;
import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.common.utils.TimeUtils;
import com.wbs.pipe.application.ConnectApplication;
import com.wbs.pipe.application.engine.base.EngineManager;
import com.wbs.pipe.model.engine.InsertResult;
import com.wbs.pipe.model.engine.UpdateResult;
import com.wbs.pipe.model.event.MessageEventModel;
import com.wbs.pipe.model.sink.SinkInfoModel;
import com.wbs.pipe.model.task.TaskInfoModel;
import com.wbs.pipe.model.task.TaskLogModel;
import com.wbs.pipe.model.task.TaskStatusEnum;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author WBS
 * @date 2023/5/4 15:33
 * @desciption DbSubscriberBase
 */
@Component
public class DbSubscriberBase {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MongoCollection<TaskLogModel> taskLogCollection;
    private final ConnectApplication connectApplication;
    protected DbWriteTypeEnum dbWriteTypeEnum;
    protected IDbWriter writer;
    protected Connection connection;
    protected List<ColumnInfo> columnList;

    @Value("${pipe.message-timeout}")
    private int messageTimeout;

    public DbSubscriberBase() {
        this.connectApplication = SpringUtil.getBean("connectApplication");
        MongoDatabase defaultMongoDatabase = SpringUtil.getBean("defaultMongoDatabase");
        this.taskLogCollection = defaultMongoDatabase.getCollection("task_log", TaskLogModel.class);
    }

    /**
     * 消息超时过期
     *
     * @param message
     */
    public boolean messageTimeout(String message) {
        if (messageTimeout == 0) {
            messageTimeout = 30;
        }
        String str = message.substring(message.lastIndexOf("sendTime") + 11, message.lastIndexOf("sendTime") + 30);
        LocalDateTime sendTime = TimeUtils.strToDate(str).plusSeconds(messageTimeout);
        // 如果sendTime在now之前
        return sendTime.isBefore(LocalDateTime.now());
    }

    public void config(TaskInfoModel taskInfo, SinkInfoModel sinkInfo, DbTypeEnum dbTypeEnum) {
        connection = connectApplication.getConnection(sinkInfo.getConnect_id());
        columnList = DbUtils.getColumns(connection, sinkInfo.getTable_name());
        dbWriteTypeEnum = EnumUtil.getEnumMap(DbWriteTypeEnum.class).get(taskInfo.getExist_type().toUpperCase());
        writer = EngineManager.getWriter(dbTypeEnum);
    }

    /**
     * 任务日志更新
     */
    public void taskLogUpdate(MessageEventModel event, InsertResult insertResult, UpdateResult updateResult) {
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
        }
    }

    public void process(MessageEventModel message) {
        InsertResult insertResult = null;
        UpdateResult updateResult = null;
        try {
            if (writer != null) {
                writer.config(message.getSinkInfo().getTable_name(), connection, columnList);
                insertResult = writer.insertData(message.getTable());
                if (insertResult.getExistData() != null) {
                    // 如果是存在更新
                    if (dbWriteTypeEnum.equals(DbWriteTypeEnum.UPSERT)) {
                        updateResult = writer.updateData(insertResult.getExistData());
                    }
                    // 如果是存在忽略
                    else if (dbWriteTypeEnum.equals(DbWriteTypeEnum.IGNORE)) {
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
    }
}
