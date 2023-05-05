package com.wbs.pipe.application.engine.sqlserver;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.pipe.application.ConnectApplication;
import com.wbs.pipe.application.engine.base.SubscriberAbstract;
import com.wbs.pipe.application.engine.base.WriteTypeEnum;
import com.wbs.pipe.model.engine.WriterResult;
import com.wbs.pipe.model.event.SqlServerEventModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/**
 * @author WBS
 * @date 2023/4/26 11:25
 * @desciption SqlServerSubscriber
 */
@Component
public class SqlServerSubscriber extends SubscriberAbstract {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public SqlServerSubscriber(AsyncEventBus defaultEventBus, ConnectApplication connectApplication) {
        super(connectApplication);
        defaultEventBus.register(this);
    }

    @Subscribe
    public void receive(SqlServerEventModel event) {
        config(event.getTaskInfo(), event.getSinkInfo(), DbTypeEnum.SQLSERVER);

        WriterResult insertResult;
        WriterResult updateResult;
        try {
            if (writer != null) {
                writer.config( event.getSinkInfo().getTable_name(), connection, columnList);
                insertResult = writer.insertData(event.getDt());
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
        } catch (Exception e1) {
            logger.error("------SqlServerSubscriber receive error------", e1);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e2) {
                    logger.error("------SqlServerSubscriber receive error------", e2);
                }
            }
        }
    }
}
