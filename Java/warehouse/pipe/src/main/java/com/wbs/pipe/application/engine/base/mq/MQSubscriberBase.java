package com.wbs.pipe.application.engine.base.mq;

import cn.hutool.extra.spring.SpringUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.wbs.common.enums.MQTypeEnum;
import com.wbs.common.utils.TimeUtils;
import com.wbs.pipe.application.ConnectApplication;
import com.wbs.pipe.application.engine.base.EngineManager;
import com.wbs.pipe.model.connect.ConnectInfoModel;
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
import java.time.LocalDateTime;

/**
 * @author WBS
 * @date 2023/5/4 15:33
 * @desciption MqSubscriberBase
 */
@Component
public class MQSubscriberBase {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MongoCollection<TaskLogModel> taskLogCollection;
    private final ConnectApplication connectApplication;
    protected IMQSender sender;
    protected Connection connection;
    private ConnectInfoModel connectInfo;
    private SinkInfoModel sinkInfo;

    @Value("${pipe.message-timeout}")
    private int messageTimeout;

    public MQSubscriberBase() {
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

    public void config(TaskInfoModel taskInfo, SinkInfoModel sinkInfo, MQTypeEnum mqTypeEnum) {
        sender = EngineManager.getSender(mqTypeEnum);
        this.connectInfo = this.connectApplication.getConnectInfo(sinkInfo.getConnect_id(), null);
        this.sinkInfo=sinkInfo;
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
        try {
            if (sender != null) {
                sender.config(this.connectInfo,this.sinkInfo);
                sender.sendData(message.getTable());
            }
        } catch (Exception e1) {
            logger.error("------MQSubscriberBase process error------", e1);
        }
    }
}
