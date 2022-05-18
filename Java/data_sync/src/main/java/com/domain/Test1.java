package com.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Test1 {
    private String id;

    private Date tm;

    private BigDecimal sj;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTm() {
        return tm;
    }

    public void setTm(Date tm) {
        this.tm = tm;
    }

    public BigDecimal getSj() {
        return sj;
    }

    public void setSj(BigDecimal sj) {
        this.sj = sj;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", tm=").append(tm);
        sb.append(", sj=").append(sj);
        sb.append("]");
        return sb.toString();
    }
}