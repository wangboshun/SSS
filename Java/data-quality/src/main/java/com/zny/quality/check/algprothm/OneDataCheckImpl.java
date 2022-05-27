package com.zny.quality.check.algprothm;

import com.zny.quality.check.CompareEnum;
import com.zny.quality.check.DataCheckAbstract;

import java.math.BigDecimal;

/**
 * @author WBS
 */
public class OneDataCheckImpl extends DataCheckAbstract {

    @Override
    public boolean execute(BigDecimal value, CompareEnum e) {
        System.out.println("OneDataCheckImpl");
        BigDecimal compareValue = new BigDecimal("5");
       return super.compareTo(value, compareValue, e);
    }
}
