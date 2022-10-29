package com.zny.pipe.component.sink;

import com.zny.common.enums.DbTypeEnum;
import com.zny.pipe.model.ConnectConfigModel;
import com.zny.pipe.model.SinkConfigModel;
import com.zny.pipe.model.TaskConfigModel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author WBS
 * Date 2022-10-29 16:28
 * SinkFactory
 */

public class SinkFactory {
    private static final Map<String, SinkBase> sinkMap = new ConcurrentHashMap<>();

    public static void register(DbTypeEnum type, SinkBase sink) {
        sinkMap.put(type.toString(), sink);
    }

    public static void run(TaskConfigModel taskConfig, SinkConfigModel sinkConfig, ConnectConfigModel connectConfig) {
        DbTypeEnum e = DbTypeEnum.values()[connectConfig.getDb_type()];
        SinkBase sink = sinkMap.get(e.toString());
        sink.config(sinkConfig, connectConfig, taskConfig);
        sink.start();
    }
}
