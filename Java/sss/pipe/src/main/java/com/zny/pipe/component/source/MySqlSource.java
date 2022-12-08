package com.zny.pipe.component.source;

import com.zny.common.enums.DbTypeEnum;
import com.zny.pipe.component.base.SourceAbstract;
import com.zny.pipe.component.base.annotations.SourceTypeAnnotation;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * Date:2022/10/12
 * mysql源端服务类
 */

@Component
@SourceTypeAnnotation(DbTypeEnum.MySql)
public class MySqlSource extends SourceAbstract {

}
