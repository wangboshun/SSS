package com.zny.pipe.component.source;

import com.zny.common.enums.DbTypeEnum;
import com.zny.pipe.component.base.SourceAbstract;
import com.zny.pipe.component.base.annotations.SourceTypeAnnotation;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * Date:2022/12/07
 * PostgreSQL源端服务类
 */

@Component
@SourceTypeAnnotation(DbTypeEnum.PostGres)
public class PostgreSQLSource extends SourceAbstract {

}
