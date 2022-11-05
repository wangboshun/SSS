package com.zny.pipe.component.source;

import com.zny.common.enums.DbTypeEnum;
import com.zny.pipe.component.base.SourceAbstract;
import com.zny.pipe.component.enums.SourceTypeEnum;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * Date:2022/10/14
 * mssql源端服务类
 */

@Component
@SourceTypeEnum(DbTypeEnum.MsSQL)
public class MsSqlSource extends SourceAbstract {

}
