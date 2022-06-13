package org.wbs.quality.juc;

import org.junit.jupiter.api.Test;

/**
 * @author WBS
 * Date:2022/6/12
 */

public class thread_2 {

    public static void main(String[] args) {

        Thread t = new Thread() {

            @Override
            public void run() {
                test2(1);
            }

        };
        t.setName("thread-------------");
        t.start();
        test2(222);

    }

    public static void test2(int a) {
        System.out.println(a);
    }

}
