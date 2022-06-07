package org.wbs.quality.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author WBS
 * Date:2022/6/1
 */

public class Test1 {

    @lombok.Setter
    @lombok.Getter
    private String Id;

    @lombok.Setter
    @lombok.Getter
    private Timestamp TM;

    @lombok.Setter
    @lombok.Getter
    private BigDecimal SJ;

    @Override
    public String toString() {
        return "Test1{" + "Id='" + Id + '\'' + ", TM=" + TM + ", SJ=" + SJ + '}';
    }

}
