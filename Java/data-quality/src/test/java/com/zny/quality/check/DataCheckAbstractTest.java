package com.zny.quality.check;

import cn.hutool.core.util.ClassUtil;
import com.zny.quality.check.algprothm.algprothm1;
import com.zny.quality.check.algprothm.algprothm2;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

class DataCheckAbstractTest {
    @Test
    void test1() {
        BigDecimal value = new BigDecimal("8");

        DataCheckAbstract one = new algprothm1();
        DataCheckAbstract two = new algprothm2();

        Set<DataCheckAbstract> list = new HashSet<>();
        list.add(one);
        list.add(two);

        CheckInvoker invoker = new CheckInvoker(list);
        invoker.action(value, CompareEnum.GREATER);
    }

    @Test
    void test2() {
        Set<Class<?>> list = ClassUtil.scanPackageBySuper(null, DataCheckAbstract.class);
        Set<DataCheckAbstract> l = new HashSet<>();

        for (Class<?> c : list) {
            try {
                l.add((DataCheckAbstract) Class.forName(c.getName()).getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        BigDecimal value = new BigDecimal("9");
        CheckInvoker invoker = new CheckInvoker(l);
        invoker.action(value, CompareEnum.LESS);
    }
}