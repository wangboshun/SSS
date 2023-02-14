package com.zny.pipe.component.source;

import com.zny.common.enums.DbTypeEnum;
import com.zny.pipe.component.base.SourceAbstract;
import com.zny.pipe.component.base.annotations.SourceTypeAnnotation;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * Date:2022/12/07
 * ClickHouse源端服务类
 */

@Component
@SourceTypeAnnotation(DbTypeEnum.ClickHouse)
public class ClickHouseSource extends SourceAbstract {
    @Override
    public void start() {
        super.start();
        System.out.println("ClickHouseSource start");
    }
}
