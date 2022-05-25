package com.zny.quality.check;

import java.math.BigDecimal;

public abstract class DataCheckHandler {
    public BigDecimal value;
    private DataCheckHandler next;

    public void setNextHandler(DataCheckHandler handler) {
        this.next = handler;
    }

    protected void less(BigDecimal value) {
        if (value.compareTo(this.value) < 0) {
            addWarn(value, ErrorEnum.LESS);
        } else {
            if (next != null) {
                next.less(value);
            } else {
                System.out.println("最后一级了");
            }
        }
    }

    protected void more(BigDecimal value) {
        if (value.compareTo(this.value) > 0) {
            addWarn(value, ErrorEnum.LESS);
        } else {
            if (next != null) {
                next.more(value);
            } else {
                System.out.println("最后一级了");
            }
        }
    }

    protected abstract void addWarn(BigDecimal value, ErrorEnum error);
}
