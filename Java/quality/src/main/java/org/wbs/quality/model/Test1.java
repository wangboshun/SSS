package org.wbs.quality.model;

import cn.hutool.core.date.DateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author WBS
 * Date:2022/6/1
 */

public class Test1 {

    public String Id;

    @Override
    public String toString() {
        return "Test1{" +
                "Id='" + Id + '\'' +
                ", TM=" + TM +
                ", SJ=" + SJ +
                '}';
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Timestamp getTM() {
        return TM;
    }

    public void setTM(Timestamp TM) {
        this.TM = TM;
    }

    public BigDecimal getSJ() {
        return SJ;
    }

    public void setSJ(BigDecimal SJ) {
        this.SJ = SJ;
    }


    public Timestamp TM;
    public BigDecimal SJ;

}
