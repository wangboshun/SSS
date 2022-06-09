package org.wbs.quality.check;

import cn.hutool.core.util.ClassUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.wbs.quality.ApplicationMain;
import org.wbs.quality.business.check.CheckInvoker;
import org.wbs.quality.business.check.DataCheckBase;
import org.wbs.quality.business.check.algprothm.algprothm1;
import org.wbs.quality.business.check.algprothm.algprothm2;
import org.wbs.quality.business.check.enums.CompareEnum;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SpringBootTest(classes = ApplicationMain.class)
class DataCheckAbstractTest {


    /**
     * 通过一般实例化添加算法
     */
    @Test
    void test1() {
        BigDecimal value = new BigDecimal("800");

        DataCheckBase one = new algprothm1();
        DataCheckBase two = new algprothm2();

        Set<DataCheckBase> list = new HashSet<>();
        list.add(one);
        list.add(two);

        CheckInvoker invoker = new CheckInvoker(list);
        invoker.action(value, CompareEnum.GREATER);
    }


    /**
     * 通过反射方式添加算法
     */
    @Test
    void test2() {
        Set<Class<?>> list = ClassUtil.scanPackageBySuper(null, DataCheckBase.class);
        Set<DataCheckBase> l = new HashSet<>();

        for (Class<?> c : list) {
            try {
                l.add((DataCheckBase) Class.forName(c.getName()).getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        BigDecimal value = new BigDecimal("9");
        CheckInvoker invoker = new CheckInvoker(l);
        invoker.action(value, CompareEnum.LESS);

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("结束");
    }


    /**
     * fork join测试
     */
    @Test
    void test3() throws InterruptedException {
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + "-f1");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "f1";
        });
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + "-f2");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "f2";
        });
        CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + "-f3");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "f3";
        });

        CompletableFuture.allOf(f1, f2, f3).thenApply((Integer) -> {
            try {
                System.out.println(Thread.currentThread() + f1.get() + f2.get() + f3.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return 1;
        });
        Thread.sleep(10000);
        System.out.println(Thread.currentThread() + " end");

    }
}