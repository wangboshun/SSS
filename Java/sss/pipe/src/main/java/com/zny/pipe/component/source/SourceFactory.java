package com.zny.pipe.component.source;

import com.zny.common.enums.DbTypeEnum;
import com.zny.pipe.model.ConnectConfigModel;
import com.zny.pipe.model.SourceConfigModel;
import com.zny.pipe.model.TaskConfigModel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author WBS
 * Date 2022-10-29 16:36
 * SourceFactory
 */

public class SourceFactory {
    private static final Map<String, SourceBase> sourceMap = new ConcurrentHashMap<>();

    public static void register(DbTypeEnum type, SourceBase source) {
        sourceMap.put(type.toString(), source);
    }

    public static void run(TaskConfigModel taskConfig, SourceConfigModel sourceConfig, ConnectConfigModel connectConfig) {
        DbTypeEnum e = DbTypeEnum.values()[connectConfig.getDb_type()];
        SourceBase sink = sourceMap.get(e.toString());
        sink.config(sourceConfig, connectConfig, taskConfig);
        sink.start();
    }
}
