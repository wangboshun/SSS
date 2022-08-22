package org.example;

/**
 * @author WBS
 * Date:2022/8/22
 */

public class ThreadTest {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            System.out.println("thread start---");
            for (long i = 0; i < 100_000; i++) {
                for (long j = 0; j < 100_000; j++) {

                }
            }
            System.out.println("thread end---");
        });

        t.start();
        t.join();
        System.out.println("main");
    }
}
