package com.zny.quality.check;

import com.zny.quality.msg.MsgObserver;
import com.zny.quality.msg.MsgSubject;
import com.zny.quality.msg.sink.RabbitMqObserverImpl;
import com.zny.quality.msg.sink.RedisObserverImpl;

import java.math.BigDecimal;

/**
 * 使用命令模式
 */
public abstract class DataCheckAbstract {
    protected abstract boolean execute(BigDecimal value, CompareEnum e);

    private MsgSubject getMsgSubject() {
        MsgSubject subject = new MsgSubject();

        MsgObserver observer = new RabbitMqObserverImpl();
        subject.addObserver(observer);

        observer = new RedisObserverImpl();
        subject.addObserver(observer);

        return subject;
    }

    private void addWarn(BigDecimal currentValue, BigDecimal compareValue, CompareEnum e) {
        MsgSubject subject = getMsgSubject();
        subject.sendMsg("currentValue：" + currentValue + " " + "compareValue：" + compareValue + " " + e);
    }

    /**
     * @param currentValue 当前值
     * @param compareValue 对比值
     * @param e            对比方式
     * @return 对比状态
     */
    protected boolean compareTo(BigDecimal currentValue, BigDecimal compareValue, CompareEnum e) {
        switch (e) {
            case GREATER:
                return this.greater(currentValue, compareValue);
            case GREATER_EQUAL:
                return this.greaterEqual(currentValue, compareValue);
            case LESS:
                return this.less(currentValue, compareValue);
            case LESS_EQUAL:
                return this.lessEqual(currentValue, compareValue);
            case EQUAL:
                return this.equal(currentValue, compareValue);
            default:
                throw new IllegalStateException("Unexpected value: " + e);
        }
    }

    private boolean less(BigDecimal currentValue, BigDecimal compareValue) {
        if (currentValue.compareTo(compareValue) < 0) {
            this.addWarn(currentValue, compareValue, CompareEnum.LESS);
            return true;
        }
        return false;
    }

    private boolean lessEqual(BigDecimal currentValue, BigDecimal compareValue) {
        if (currentValue.compareTo(compareValue) <= 0) {
            this.addWarn(currentValue, compareValue, CompareEnum.LESS_EQUAL);
            return true;
        }
        return false;
    }

    private boolean greater(BigDecimal currentValue, BigDecimal compareValue) {
        if (currentValue.compareTo(compareValue) > 0) {
            this.addWarn(currentValue, compareValue, CompareEnum.GREATER);
            return true;
        }
        return false;
    }

    private boolean greaterEqual(BigDecimal currentValue, BigDecimal compareValue) {
        if (currentValue.compareTo(compareValue) <= 0) {
            this.addWarn(currentValue, compareValue, CompareEnum.GREATER_EQUAL);
            return true;
        }
        return false;
    }

    private boolean equal(BigDecimal currentValue, BigDecimal compareValue) {
        if (currentValue.compareTo(compareValue) == 0) {
            this.addWarn(currentValue, compareValue, CompareEnum.EQUAL);
            return true;
        }
        return false;
    }
}
