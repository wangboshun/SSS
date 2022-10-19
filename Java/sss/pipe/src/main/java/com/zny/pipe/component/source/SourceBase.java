package com.zny.pipe.component.source;

import com.zny.pipe.model.ConnectConfigModel;
import com.zny.pipe.model.SourceConfigModel;
import com.zny.pipe.model.TaskConfigModel;

/**
 * @author WBS
 * Date:2022/10/12
 * source源端接口类
 */

public interface SourceBase {

    void config(SourceConfigModel sourceConfig, ConnectConfigModel connectConfig, TaskConfigModel taskConfig);

    void start();

    void stop();

}
