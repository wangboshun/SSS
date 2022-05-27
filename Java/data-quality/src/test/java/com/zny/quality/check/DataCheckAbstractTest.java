package com.zny.quality.check;

import com.zny.quality.check.algprothm.OneDataCheckImpl;
import com.zny.quality.check.algprothm.TwoDataCheckImpl;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class DataCheckAbstractTest {
    @Test
    void name() {

        BigDecimal value = new BigDecimal("10");

        DataCheckAbstract one = new OneDataCheckImpl();
        DataCheckAbstract two = new TwoDataCheckImpl();

        List<DataCheckAbstract> list = new ArrayList<>();
        list.add(one);
        list.add(two);

        CheckInvoker invoker = new CheckInvoker(list);
        invoker.action(value,CompareEnum.GREATER);
    }
}