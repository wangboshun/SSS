package com.zny.pipe.component.sink;

import com.zny.common.enums.DbTypeEnum;
import com.zny.pipe.component.base.SinkAbstract;
import com.zny.pipe.component.base.annotations.SinkTypeAnnotation;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * Date:2022/10/12
 * PostgreSql目的服务类
 * PostgreSQL在查询时，需要给表名和字段加双引号，从而解决大小写问题
 */

@Component
@SinkTypeAnnotation(DbTypeEnum.PostgreSQL)
public class PostgreSqlSink extends SinkAbstract {

}
