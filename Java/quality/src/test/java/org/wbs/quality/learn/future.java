package org.wbs.quality.learn;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author WBS
 * Date:2022/5/31
 */

public class future {

    @Test
    void complete() throws InterruptedException {

        /*
        * 其中supplyAsync用于有返回值的任务，runAsync则用于没有返回值的任务。
        * Executor参数可以手动指定线程池，否则默认ForkJoinPool.commonPool()系统级公共线程池，
        * 注意：这些线程都是Daemon线程，主线程结束Daemon线程不结束，只有JVM关闭时，生命周期终止。
        * */

        ExecutorService executorService = Executors.newCachedThreadPool();
        CompletableFuture<String> c = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                //做处理
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "hello";
            }
        }, executorService);

        c.thenAccept(item -> System.out.println(item));

        System.out.println("over");

//        Thread.sleep(5000);
    }

    @Test
    void future_complete() throws ExecutionException, InterruptedException {

        List<String> list = new ArrayList<>();

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("start---1");
                Thread.sleep(2000);
                System.out.println("end---1");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 10086;
        });
        CompletableFuture<Integer> r = future.whenComplete((result, error) -> {
            System.out.println("start---2");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("拨打" + result);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("end---2");
            //complete不能自定义返回值类型,handle可以自定义返回值类型
        });


        System.out.println("over：" + r.get());
        while (true) {

        }
    }

    @Test
    public void future_handle() {
        CompletableFuture<List> future = CompletableFuture.supplyAsync(() -> {
            List<String> list = new ArrayList<>();
            list.add("语文");
            list.add("数学");
            // 获取得到今天的所有课程
            return list;
        });

        // 使用handle()方法接收list数据和error异常
        CompletableFuture<Integer>  future2 = future.handle((list, error) -> {
            // 如果报错，就打印出异常
//            error.printStackTrace();
            // 如果不报错，返回一个包含Integer的全新的CompletableFuture
            return list.size();
            //complete不能自定义返回值类型,handle可以自定义返回值类型
        });

        try {
            System.out.println(future2.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }


    @Test
    public void future_apply() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        CompletableFuture<List> future = CompletableFuture.supplyAsync(() -> {
            List<String> list = new ArrayList<>();
            list.add("语文");
            list.add("数学");
            // 获取得到今天的所有课程
            return list;
        }, executorService);

        // apply和handle差不多,少了一个error异常参数
        CompletableFuture<Integer>  future2 = future.thenApply((list) -> {
            // 如果不报错，返回一个包含Integer的全新的CompletableFuture
            return list.size();
        });

        try {
            System.out.println(future2.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void future_accept() {
        CompletableFuture<List> future = CompletableFuture.supplyAsync(() -> {
            List<String> list = new ArrayList<>();
            list.add("语文");
            list.add("数学");
            // 获取得到今天的所有课程
            return list;
        });

        // accept没有返回值
        CompletableFuture<Void> future2 = future.thenAccept((list) -> {
          System.out.println("over");
        });

        try {
            System.out.println(future2.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void future_compose(){
        CompletableFuture<List<String>> total = CompletableFuture.supplyAsync(() -> {
            // 第一个任务获取美术课需要带的东西，返回一个list
            List<String> stuff = new ArrayList<>();
            stuff.add("画笔");
            stuff.add("颜料");
            return stuff;
        }).thenCompose(list -> {
            // 向第二个任务传递参数list(上一个任务美术课所需的东西list)
            CompletableFuture<List<String>> insideFuture = CompletableFuture.supplyAsync(() -> {
                List<String> stuff = new ArrayList<>();
                // 第二个任务获取劳技课所需的工具
                stuff.add("剪刀");
                stuff.add("折纸");
                // 合并两个list，获取课程所需所有工具
                List<String> allStuff = Stream.of(list, stuff).flatMap(Collection::stream).collect(Collectors.toList());
                return allStuff;
            });
            return insideFuture;
        });
        System.out.println(total.join().size());
    }

    @Test
    public void future_combine(){
        CompletableFuture<List<String>> painting = CompletableFuture.supplyAsync(() -> {
            // 第一个任务获取美术课需要带的东西，返回一个list
            List<String> stuff = new ArrayList<>();
            stuff.add("画笔");
            stuff.add("颜料");
            return stuff;
        });
        CompletableFuture<List<String>> handWork = CompletableFuture.supplyAsync(() -> {
            // 第二个任务获取劳技课需要带的东西，返回一个list
            List<String> stuff = new ArrayList<>();
            stuff.add("剪刀");
            stuff.add("折纸");
            return stuff;
        });
        CompletableFuture<List<String>> total = painting
                // 传入handWork列表，然后得到两个CompletableFuture的参数Stuff1和2
                .thenCombine(handWork, (stuff1, stuff2) -> {
                    // 合并成新的list
                    List<String> totalStuff = Stream.of(stuff1, stuff1)
                            .flatMap(Collection::stream)
                            .collect(Collectors.toList());
                    return totalStuff;
                });
        System.out.println(total.join().size());
    }

    @Test
    public void future_allOf(){

        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                //使用sleep()模拟耗时操作
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        });

        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            return 2;
        });
        System.out.println("计算中------");
        CompletableFuture.allOf(future1, future2);
        System.out.println("主程序继续------");
        // 输出3
        System.out.println(future1.join()+future2.join());
        System.out.println("计算完成------");
    }

    @Test
    public void future_anyOf() throws InterruptedException {


        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                //使用sleep()模拟耗时操作
              Thread.sleep(2000);
              System.out.println("over");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        });

        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            return 2;
        });
        System.out.println("计算中------");

        CompletableFuture<Object> anyOf = CompletableFuture
                .anyOf(future1, future2)
                .exceptionally(error -> {
                    error.printStackTrace();
                    return 2;
                });

        System.out.println("主程序继续------");
        // 输出3
        System.out.println(anyOf.join());
        System.out.println("计算完成------");

        Thread.sleep(5000);
    }


    /**
     * 两个线程合并，得到最终结果
     */
    @Test
    public void callable() {
        ExecutorService executor = Executors.newCachedThreadPool();
        Callable<String> callable1 = () -> {
            System.out.println("start-1");

            Thread.sleep(2000);

            return "return-1";
        };

        Callable<String> callable2 = () -> {
            System.out.println("start-2");

            Thread.sleep(5000);

            return "return-2";
        };


        System.out.println("已提交任务");

        Future<String> future1 = executor.submit(callable1);
        Future<String> future2 = executor.submit(callable2);

        System.out.println("主线程继续运行");
        try {
//            String r1=future1.get();
//            System.out.println("结果："+r1);

//            String r2=future2.get();
//            System.out.println("结果："+r2);

            String s = future1.get() + future2.get();
            System.out.println(s);
        } catch (Exception e) {

        }
        System.out.println("结束");
    }

    @Test
    public void callable2() {
        ExecutorService executor = Executors.newCachedThreadPool();
        FutureTask<String> future1 = new FutureTask<>(new tesk1());
        FutureTask<String> future2 = new FutureTask<>(new tesk2());
        executor.submit(future1);
        executor.submit(future2);
        try {
            System.out.println("主线程继续运行----1");
            String s = future1.get() + future2.get();
            System.out.println("主线程继续运行-----2");
            System.out.println(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public class tesk1 implements Callable<String> {

        @Override
        public String call() throws Exception {
            System.out.println("start-1");

            Thread.sleep(2000);

            return "return-1";
        }
    }

    public class tesk2 implements Callable<String> {

        @Override
        public String call() throws Exception {
            System.out.println("start-2");

            Thread.sleep(5000);

            return "return-2";
        }
    }

}
