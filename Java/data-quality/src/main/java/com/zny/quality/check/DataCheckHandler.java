package com.zny.quality.check;

import java.math.BigDecimal;

public abstract class DataCheckHandler {
    public BigDecimal currentValue;
    private DataCheckHandler nextHandler;
    private DataCheckHandler prevHandler;

    public void setNextHandler(DataCheckHandler handler) {
        this.nextHandler = handler;
    }

    public void setPreHandler(DataCheckHandler handler) {
        this.prevHandler = handler;
    }

    protected void less(BigDecimal value) {
        if (value.compareTo(this.currentValue) < 0) {
            addWarn(value, ErrorEnum.LESS);
        } else {
            if (nextHandler != null) {
                nextHandler.less(value);
            } else {
                System.out.println("没有可匹配的值");
            }
        }
    }

    protected void more(BigDecimal value) {
        if (value.compareTo(this.currentValue) > 0) {
            if (nextHandler != null) {
                nextHandler.setPreHandler(this);
                nextHandler.more(value);
            } else {
                System.out.println("没有可匹配的值");
            }
        } else {
            if (prevHandler!= null){
                this.currentValue = prevHandler.currentValue;
                addWarn(value, ErrorEnum.MORE);
            }
            else{
                System.out.println("没有可匹配的值");
            }
        }
    }

    protected abstract void addWarn(BigDecimal value, ErrorEnum error);
}
