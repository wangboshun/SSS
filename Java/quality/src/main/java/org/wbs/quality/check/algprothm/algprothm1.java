package org.wbs.quality.check.algprothm;


import org.wbs.quality.check.AbstractDataCheck;
import org.wbs.quality.check.CompareEnum;

import java.math.BigDecimal;

/**
 * @author WBS
 */
public class algprothm1 extends AbstractDataCheck {

    @Override
    public boolean execute(BigDecimal value, CompareEnum e) {
        BigDecimal compareValue = new BigDecimal("500");
        return super.compare(value, compareValue, e);
    }
}
