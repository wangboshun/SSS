package com.zny.pipe.component.base;

import com.zny.pipe.model.ConnectConfigModel;
import com.zny.pipe.model.SinkConfigModel;
import com.zny.pipe.model.TaskConfigModel;

/**
 * @author WBS
 * Date:2022/10/12
 * sink目的接口类
 */

public interface SinkBase {

    void config(SinkConfigModel sinkConfig, ConnectConfigModel connectConfig, TaskConfigModel taskConfig);

    void start();

    void stop();
}
