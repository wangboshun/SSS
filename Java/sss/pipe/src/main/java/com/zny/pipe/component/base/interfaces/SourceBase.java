package com.zny.pipe.component.base.interfaces;

import com.zny.pipe.model.ColumnConfigModel;
import com.zny.pipe.model.ConnectConfigModel;
import com.zny.pipe.model.SourceConfigModel;
import com.zny.pipe.model.TaskConfigModel;

import java.util.List;

/**
 * @author WBS
 * Date:2022/10/12
 * source源端接口类
 */

public interface SourceBase {

    boolean config(SourceConfigModel sourceConfig, ConnectConfigModel connectConfig, TaskConfigModel taskConfig, List<ColumnConfigModel> columnList, int version);

    void start();

    void stop();
}
