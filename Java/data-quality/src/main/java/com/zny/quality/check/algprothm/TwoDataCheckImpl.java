package com.zny.quality.check.algprothm;

import com.zny.quality.check.CompareEnum;
import com.zny.quality.check.DataCheckAbstract;

import java.math.BigDecimal;

/**
 * @author WBS
 */
public class TwoDataCheckImpl extends DataCheckAbstract {

    @Override
    public boolean execute(BigDecimal value, CompareEnum e) {
        System.out.println("TwoDataCheckImpl");
        BigDecimal compareValue = new BigDecimal("10");
        return super.compareTo(value, compareValue, e);
    }
}
