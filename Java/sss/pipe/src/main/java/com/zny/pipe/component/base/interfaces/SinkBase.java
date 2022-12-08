package com.zny.pipe.component.base.interfaces;

import com.zny.pipe.component.base.enums.TaskStatusEnum;
import com.zny.pipe.model.ColumnConfigModel;
import com.zny.pipe.model.ConnectConfigModel;
import com.zny.pipe.model.SinkConfigModel;
import com.zny.pipe.model.TaskConfigModel;

import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/10/12
 * sink目的接口类
 */

public interface SinkBase {

    void config(SinkConfigModel sinkConfig, ConnectConfigModel connectConfig, TaskConfigModel taskConfig, List<ColumnConfigModel> columnList, Integer version);

    void start(List<Map<String, Object>> list);

    void stop();

    void setStatus(TaskStatusEnum e);
}
