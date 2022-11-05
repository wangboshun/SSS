package com.zny.pipe.component.sink;

import com.zny.common.enums.DbTypeEnum;
import com.zny.pipe.component.base.SinkAbstract;
import com.zny.pipe.component.enums.SinkTypeEnum;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * Date:2022/10/12
 * mssql目的服务类
 */

@Component
@SinkTypeEnum(DbTypeEnum.MsSQL)
public class MsSqlSink extends SinkAbstract {

}
