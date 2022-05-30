package com.zny.quality.check.algprothm;

import com.zny.quality.check.CompareEnum;
import com.zny.quality.check.DataCheckAbstract;

import java.math.BigDecimal;

/**
 * @author WBS
 */
public class algprothm1 extends DataCheckAbstract {

    @Override
    public boolean execute(BigDecimal value, CompareEnum e) {
        System.out.println("使用了【algprothm----1】算法");
        BigDecimal compareValue = new BigDecimal("5");
       return super.compareTo(value, compareValue, e);
    }
}
