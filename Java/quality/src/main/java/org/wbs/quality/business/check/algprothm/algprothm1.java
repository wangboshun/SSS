package org.wbs.quality.business.check.algprothm;


import org.wbs.quality.business.check.DataCheckBase;
import org.wbs.quality.business.check.enums.CompareEnum;

import java.math.BigDecimal;

/**
 * @author WBS
 */
public class algprothm1 extends DataCheckBase {

    @Override
    public boolean execute(BigDecimal value, CompareEnum e) {
        BigDecimal compareValue = new BigDecimal("500");
        return super.compare(value, compareValue, e);
    }
}
