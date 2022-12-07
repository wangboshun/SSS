package com.zny.pipe.component.sink;

import com.zny.common.enums.DbTypeEnum;
import com.zny.pipe.component.base.SinkAbstract;
import com.zny.pipe.component.base.annotations.SinkTypeAnnotation;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * Date:2022/10/12
 * PostgreSql目的服务类
 */

@Component
@SinkTypeAnnotation(DbTypeEnum.PostGres)
public class PostgreSqlSink extends SinkAbstract {

}
