package com.zny.pipe.component.source;

import com.zny.common.enums.DbTypeEnum;
import com.zny.pipe.component.base.SourceAbstract;
import com.zny.pipe.component.base.annotations.SourceTypeAnnotation;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * Date:2022/10/14
 * mssql源端服务类
 */

@Component
@SourceTypeAnnotation(DbTypeEnum.MsSql)
public class MsSqlSource extends SourceAbstract {
    @Override
    public void start() {
        super.start();
        System.out.println("MsSqlSource start");
    }
}
