package com.zny.quality.check;

import com.zny.quality.check.algorithm.oneAlgorithmHandler;
import com.zny.quality.check.algorithm.threeAlgorithmHandler;
import com.zny.quality.check.algorithm.twoAlgorithmHandler;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class DataCheckHandlerTest {
    @Test
    void test1() {

        DataCheckHandler handler1 = new oneAlgorithmHandler();
        DataCheckHandler handler2 = new twoAlgorithmHandler();
        DataCheckHandler handler3 = new threeAlgorithmHandler();

        handler1.setNextHandler(handler2);
        handler2.setNextHandler(handler3);

        BigDecimal a2 = new BigDecimal("12");

        handler1.less(a2);
        handler1.more(a2);
    }
}