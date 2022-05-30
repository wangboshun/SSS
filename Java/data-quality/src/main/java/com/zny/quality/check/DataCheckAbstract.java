package com.zny.quality.check;

import com.zny.quality.msg.MsgObserverInterface;
import com.zny.quality.msg.MsgSubject;
import com.zny.quality.msg.sink.RabbitMqObserverImpl;
import com.zny.quality.msg.sink.RedisObserverImpl;

import java.math.BigDecimal;

/**
 * 使用命令模式
 */
public abstract class DataCheckAbstract {
    protected abstract boolean execute(BigDecimal value, CompareEnum e);

    /**
     * 添加告警
     *
     * @param currentValue 当前值
     * @param compareValue 告警值
     * @param e            状态
     */
    private void addWarn(BigDecimal currentValue, BigDecimal compareValue, CompareEnum e) {

        String msg = "currentValue：" + currentValue + " " + "compareValue：" + compareValue + " " + e;
        MsgSubject subject = new MsgSubject();

        MsgObserverInterface observer = new RabbitMqObserverImpl(msg);
        subject.addObserver(observer);

        observer = new RedisObserverImpl(msg);
        subject.addObserver(observer);

        subject.sendMsg();
    }

    /**
     * @param currentValue 当前值
     * @param compareValue 对比值
     * @param e            对比方式
     * @return 对比状态
     */
    protected boolean compare(BigDecimal currentValue, BigDecimal compareValue, CompareEnum e) {
        return switch (e) {
            case GREATER -> this.greater(currentValue, compareValue);
            case GREATER_EQUAL -> this.greaterEqual(currentValue, compareValue);
            case LESS -> this.less(currentValue, compareValue);
            case LESS_EQUAL -> this.lessEqual(currentValue, compareValue);
            case EQUAL -> this.equal(currentValue, compareValue);
            default -> throw new IllegalStateException("Unexpected value: " + e);
        };
    }

    /**
     * 小于对比
     *
     * @param currentValue 当前值
     * @param compareValue 对比值
     * @return 状态
     */
    private boolean less(BigDecimal currentValue, BigDecimal compareValue) {
        if (currentValue.compareTo(compareValue) < 0) {
            this.addWarn(currentValue, compareValue, CompareEnum.LESS);
            return true;
        }
        return false;
    }

    /**
     * 小于等于对比
     *
     * @param currentValue 当前值
     * @param compareValue 对比值
     * @return 状态
     */
    private boolean lessEqual(BigDecimal currentValue, BigDecimal compareValue) {
        if (currentValue.compareTo(compareValue) <= 0) {
            this.addWarn(currentValue, compareValue, CompareEnum.LESS_EQUAL);
            return true;
        }
        return false;
    }

    /**
     * 大于对比
     *
     * @param currentValue 当前值
     * @param compareValue 对比值
     * @return 状态
     */
    private boolean greater(BigDecimal currentValue, BigDecimal compareValue) {
        if (currentValue.compareTo(compareValue) > 0) {
            this.addWarn(currentValue, compareValue, CompareEnum.GREATER);
            return true;
        }
        return false;
    }

    /**
     * 大于等于对比
     *
     * @param currentValue 当前值
     * @param compareValue 对比值
     * @return 状态
     */
    private boolean greaterEqual(BigDecimal currentValue, BigDecimal compareValue) {
        if (currentValue.compareTo(compareValue) <= 0) {
            this.addWarn(currentValue, compareValue, CompareEnum.GREATER_EQUAL);
            return true;
        }
        return false;
    }

    /**
     * 等于对比
     *
     * @param currentValue 当前值
     * @param compareValue 对比值
     * @return 状态
     */
    private boolean equal(BigDecimal currentValue, BigDecimal compareValue) {
        if (currentValue.compareTo(compareValue) == 0) {
            this.addWarn(currentValue, compareValue, CompareEnum.EQUAL);
            return true;
        }
        return false;
    }
}
