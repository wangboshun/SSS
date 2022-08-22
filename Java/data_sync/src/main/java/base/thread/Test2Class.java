package base.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author WBS
 * Date:2022/8/17
 */

class Test2Class {
    private Lock lock = new ReentrantLock();
    private int num = 1;

    private Condition conditionA = lock.newCondition();
    private Condition conditionB = lock.newCondition();
    private Condition conditionC = lock.newCondition();

    public void Test1() {
        lock.lock();
        try {
            while (num != 1) {
                conditionA.await();
            }
            System.out.println(Thread.currentThread().getName() + "=>AAAAAAA");
            num = 2;
            conditionB.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void Test2() {
        lock.lock();
        try {
            while (num != 2) {
                conditionB.await();
            }
            System.out.println(Thread.currentThread().getName() + "=>BBBBBBB");
            num = 3;
            conditionC.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void Test3() {
        lock.lock();
        try {
            while (num != 3) {
                conditionC.await();
            }
            System.out.println(Thread.currentThread().getName() + "=>CCCCC");
            num = 1;
            conditionA.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

class Test2ClassMain {
    public static void main(String[] args) {
        Test2Class t = new Test2Class();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                t.Test1();
            }
        }, "A").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                t.Test2();
            }
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                t.Test3();
            }
        }, "C").start();

    }
}
