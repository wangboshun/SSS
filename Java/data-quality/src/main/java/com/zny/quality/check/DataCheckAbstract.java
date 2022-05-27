package com.zny.quality.check;

import java.math.BigDecimal;

/**
 * WBS
 */
public abstract class DataCheckAbstract {
    protected abstract boolean execute(BigDecimal value, CompareEnum e);

    private void addWarn(BigDecimal currentValue, BigDecimal compareValue, CompareEnum e) {
        System.out.println("currentValue：" + currentValue + " " + "compareValue：" + compareValue + " " + e);
    }

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
            case NO_EQUAL:
                return this.notEqual(currentValue, compareValue);
            default:
                throw new IllegalStateException("Unexpected value: " + e);
        }
    }

    private boolean less(BigDecimal currentValue, BigDecimal compareValue) {
        if (currentValue.compareTo(compareValue) > 0) {
            this.addWarn(currentValue, compareValue, CompareEnum.LESS);
            return true;
        }
        return false;
    }

    private boolean lessEqual(BigDecimal currentValue, BigDecimal compareValue) {
        if (currentValue.compareTo(compareValue) > 0) {
            this.addWarn(currentValue, compareValue, CompareEnum.LESS_EQUAL);
            return true;
        }
        return false;
    }

    private boolean greater(BigDecimal currentValue, BigDecimal compareValue) {
        if (currentValue.compareTo(compareValue) < 0) {
            this.addWarn(currentValue, compareValue, CompareEnum.GREATER);
            return true;
        }
        return false;
    }

    private boolean greaterEqual(BigDecimal currentValue, BigDecimal compareValue) {
        if (currentValue.compareTo(compareValue) < 0) {
            this.addWarn(currentValue, compareValue, CompareEnum.GREATER_EQUAL);
            return true;
        }
        return false;
    }

    private boolean equal(BigDecimal currentValue, BigDecimal compareValue) {
        if (currentValue.compareTo(compareValue) < 0) {
            this.addWarn(currentValue, compareValue, CompareEnum.EQUAL);
            return true;
        }
        return false;
    }

    private boolean notEqual(BigDecimal currentValue, BigDecimal compareValue) {
        if (currentValue.compareTo(compareValue) < 0) {
            this.addWarn(currentValue, compareValue, CompareEnum.NO_EQUAL);
            return true;
        }
        return false;
    }
}
