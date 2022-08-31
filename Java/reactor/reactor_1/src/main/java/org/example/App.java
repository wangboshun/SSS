package org.example;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws InterruptedException {
        test8();
    }

    public static void test1() {

        Runnable t = new Test();
        Flux<String> f = Flux.just("Hello", "World");
        List<String> list = new ArrayList<String>();
        list.add("Hello");
        list.add("World");

        Flux<String> f2 = Flux.fromIterable(list);

        Flux<Integer> f3 = Flux.range(1, 10);

        f3.subscribe((i) -> {
            try {
                Thread.sleep(1000);
                if (i > 5) {
                    //throw new RuntimeException("制造异常！");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("success:" + i);
        }, (e) -> {
            System.out.println("error:" + e);
        }, t);

    }

    public static void test2() {
        Mono<String> m = Mono.just("Hello World");

    }

    public static void test3() {
        Scheduler s = Schedulers.newParallel("parallel-scheduler", 4);

        final Flux<String> flux = Flux.range(1, 2).map(i -> 10 + i).publishOn(s).map(i -> "value " + i);

        flux.subscribe(System.out::println);
    }

    public static void test4() {
        Flux.range(1, 6)    // 1
                .doOnRequest(n -> System.out.println("Request " + n + " values..."))    // 2
                .subscribe(new BaseSubscriber<Integer>() {  // 3
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) { // 4
                        System.out.println("Subscribed and make a request...");
                        request(1); // 5
                    }

                    @Override
                    protected void hookOnNext(Integer value) {  // 6
                        try {
                            TimeUnit.SECONDS.sleep(1);  // 7
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Get value [" + value + "]");    // 8
                        request(1); // 9
                    }
                });
    }

    public static void test5() {

        Observer<Object> observer = new Observer<Object>() {

            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("onSubscribe:" + d);
            }

            public void onNext(@NonNull Object o) {

                if (o.toString().equals("Two")) throw new RuntimeException("制造异常！" + o);

                System.out.println("onNext:" + o);
            }

            public void onError(@NonNull Throwable e) {
                System.out.println("onSubscribe:" + e);
            }

            public void onComplete() {
                System.out.println("onComplete");
            }
        };

        Observable observable = Observable.just("One", "Two", "Three");
        observable.subscribe(observer);
    }

    public static void test6() throws InterruptedException {
        Disposable d = Observable.just("Hello", "world").delay(1, TimeUnit.SECONDS).subscribeWith(new DisposableObserver<String>() {
            @Override
            public void onStart() {
                System.out.println("Start!");
            }

            @Override
            public void onNext(String t) {
                System.out.println(t);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Done!");
            }
        });

        Thread.sleep(5000);
        // the sequence can now be disposed via dispose()
        d.dispose();
    }

    public static void test7() throws InterruptedException {
        Disposable d = Flowable.just("Hello", "world", "!").delay(1, TimeUnit.SECONDS).subscribeWith(new DisposableSubscriber<String>() {
            @Override
            public void onStart() {
                System.out.println("Start!");
                request(1);
            }

            @Override
            public void onNext(String t) {
                System.out.println(t);
                request(1);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Done!");
            }
        });

        Thread.sleep(5000);
        // the sequence can now be cancelled via dispose()
        d.dispose();
    }

    public static void test8() throws InterruptedException {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(3);
                emitter.onNext(2);
                emitter.onNext(1);
                emitter.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe");
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println(integer + "");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }
}

class Test implements Runnable {

    public void run() {
        System.out.println("Test");
    }
}