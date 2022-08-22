package base.thread;

/**
 * @author WBS
 * Date:2022/8/17
 */

class Test1Class {
    private int num = 0;

    public synchronized void add() throws InterruptedException {

        while (num != 0) {
            this.wait();
        }
        num++;
        System.out.println(Thread.currentThread().getName() + "--->" + num);
        this.notifyAll();
    }

    public synchronized void remove() throws InterruptedException {
        while (num == 0) {
            this.wait();
        }
        num--;
        System.out.println(Thread.currentThread().getName() + "--->" + num);
        this.notifyAll();
    }
}

class Test1ClassMain {
    public static void main(String[] args) {

        Test1Class t=new Test1Class();

        new Thread(()->{

            for(int i=0; i<10; i++){
                try {
                    t.add();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"addThread").start();

        new Thread(()->{

            for(int i=0; i<10; i++){
                try {
                    t.remove();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"removeThread").start();
    }
}
