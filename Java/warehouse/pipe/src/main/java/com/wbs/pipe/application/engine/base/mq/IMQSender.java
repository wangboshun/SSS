package com.wbs.pipe.application.engine.base.mq;

import com.wbs.common.database.base.DataTable;
import com.wbs.pipe.model.connect.ConnectInfoModel;
import com.wbs.pipe.model.sink.SinkInfoModel;

/**
 * @author WBS
 * @date 2023/3/2 15:29
 * @desciption IWriter
 */
public interface IMQSender {
    public void config(ConnectInfoModel connectInfo, SinkInfoModel sinkInfo);

    public void sendData(DataTable dt);
}
