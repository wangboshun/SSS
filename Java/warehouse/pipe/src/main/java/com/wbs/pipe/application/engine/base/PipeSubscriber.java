package com.wbs.pipe.application.engine.base;

import cn.hutool.core.util.EnumUtil;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.pipe.application.SinkApplication;
import com.wbs.pipe.application.TaskApplication;
import com.wbs.pipe.model.event.EventAbstractModel;
import com.wbs.pipe.model.event.PipeEventModel;
import com.wbs.pipe.model.sink.SinkInfoModel;
import com.wbs.pipe.model.task.TaskInfoModel;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * @date 2023/4/26 11:25
 * @desciption PipeSubscriber
 */
@Component
public class PipeSubscriber {

    private final AsyncEventBus defaultEventBus;
    private final TaskApplication taskApplication;
    private final SinkApplication sinkApplication;

    public PipeSubscriber(AsyncEventBus defaultEventBus, TaskApplication taskApplication, SinkApplication sinkApplication) {
        this.defaultEventBus = defaultEventBus;
        this.taskApplication = taskApplication;
        this.sinkApplication = sinkApplication;
        defaultEventBus.register(this);
    }

    @Subscribe
    public void receive(PipeEventModel event) {
        TaskInfoModel taskInfo = taskApplication.getTask(event.getTaskId(), null);
        String[] ids = taskInfo.getSink_id().split(",");
        for (String sinkId : ids) {
            SinkInfoModel sinkInfo = sinkApplication.getSink(sinkId, null);
            DbTypeEnum dbType = EnumUtil.getEnumMap(DbTypeEnum.class).get(sinkInfo.getType().toUpperCase());
            EventAbstractModel e = EngineManager.getEvent(dbType);
            if (e == null) {
                return;
            }
            e.setTaskInfo(taskInfo);
            e.setSinkInfo(sinkInfo);
            e.setDt(event.getDt());
            e.setBatchIndex(event.getBatchIndex());
            e.setBatchSize(event.getBatchSize());
            e.setEnd(event.isEnd());
            defaultEventBus.post(e);
        }
    }
}
