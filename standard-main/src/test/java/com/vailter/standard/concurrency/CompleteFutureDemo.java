package com.vailter.standard.concurrency;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CompleteFutureDemo {
    @Test
    void test1() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("message");
        Assertions.assertTrue(cf.isDone());
        System.out.println(cf.getNow(null));
    }

    @Test
    void test2() {
        CompletableFuture cf = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + ": " + Thread.currentThread().isDaemon());
            randomSleep();
        });
        System.out.println(cf.isDone());
        //Assertions.assertTrue(cf.isDone());
        sleepEnough();
        Assertions.assertTrue(cf.isDone());
        System.out.println(cf.getNow(null));
    }

    @Test
    void test3() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("message").thenApply(String::toUpperCase);
        System.out.println(cf.getNow(null));
    }

    /**
     * 通过调用异步方法(方法后边加Async后缀)，串联起来的CompletableFuture可以异步地执行（使用ForkJoinPool.commonPool()）
     */
    @Test
    void test4() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("message").thenApplyAsync(String::toUpperCase);
        System.out.println(cf.getNow(null));
        System.out.println(cf.join());
    }

    /**
     * 使用自定义线程池异步执行
     */
    @Test
    void test5() {
        ExecutorService executor = Executors.newFixedThreadPool(3, new ThreadFactory() {
            int count = 1;

            @Override
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "custom-executor-" + count++);
            }
        });
        CompletableFuture<String> cf = CompletableFuture.completedFuture("message").thenApplyAsync(s -> {
            System.out.println(Thread.currentThread().getName().startsWith("custom-executor-"));
            System.out.println(Thread.currentThread().isDaemon());
            randomSleep();
            return s.toUpperCase();
        }, executor);

        System.out.println(cf.getNow(null));
        System.out.println(cf.join());
    }

    /**
     * 消费前一阶段的结果
     */
    @Test
    void thenAcceptExample() {
        StringBuilder result = new StringBuilder();
        CompletableFuture.completedFuture("thenAccept message")
                .thenAccept(s -> result.append(s));
        System.out.println(result.length() > 0);
        System.out.println(result);
    }

    /**
     * 异步地消费迁移阶段的结果
     */
    @Test
    void thenAcceptAsyncExample() {
        StringBuilder result = new StringBuilder();
        CompletableFuture<Void> cf = CompletableFuture.completedFuture("thenAcceptAsync message")
                .thenAcceptAsync(result::append);

        System.out.println(cf.join());
        System.out.println(result);
    }

    /**
     * 完成计算异常
     */
    @Test
    void completeExceptionallyExample() {
        CompletableFuture cf = CompletableFuture.completedFuture("message").thenApplyAsync(String::toUpperCase
                // jdk 9 开始
                //CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS) Ex)
        );
        CompletableFuture exceptionHandler = cf.handle((s, th) -> (th != null) ? "message upon cancel" : "");
        cf.completeExceptionally(new RuntimeException("completed exceptionally"));
        System.out.println(cf.isCompletedExceptionally());
        try {
            cf.join();
            //fail("Should have thrown an exception");
        } catch (CompletionException ex) { // just for testing
            ex.printStackTrace();
        }

        System.out.println(exceptionHandler.join());
    }

    @Test
    void applyToEitherExample() {
        String original = "Message";
        // 在两个完成的阶段其中之一上应用函数
        CompletableFuture<String> cf1 = CompletableFuture.completedFuture(original)
                .thenApplyAsync(String::toUpperCase);
        CompletableFuture<String> cf2 = cf1.applyToEither(
                CompletableFuture.completedFuture(original).thenApplyAsync(String::toUpperCase),
                s -> s + " from applyToEither");
        System.out.println(cf2.join());

        StringBuilder result = new StringBuilder();
        // 在两个完成的阶段其中之一上调用消费函数
        CompletableFuture<Void> cf = CompletableFuture.completedFuture(original)
                .thenApplyAsync(String::toUpperCase)
                .acceptEither(CompletableFuture.completedFuture(original).thenApplyAsync(String::toLowerCase),
                        s -> result.append(s).append("acceptEither"));
        cf.join();
        System.out.println(result);
    }

    /**
     * 在两个阶段都执行完后运行一个 Runnable
     */
    @Test
    void runAfterBothExample() {
        String original = "Message";
        StringBuilder result = new StringBuilder();
        CompletableFuture.completedFuture(original)
                .thenApply(String::toUpperCase)
                .runAfterBoth(
                        CompletableFuture.completedFuture(original).thenApply(String::toLowerCase),
                        () -> result.append("done")
                );
        System.out.println(result);
    }

    /**
     * 使用BiConsumer处理两个阶段的结果
     */
    @Test
    void thenAcceptBothExample() {
        StopWatch stopWatch = new StopWatch();
        String original = "Message";
        StringBuilder result = new StringBuilder();
        stopWatch.start("thenAcceptBoth");
        CompletableFuture.completedFuture(original)
                .thenApply(s -> {
                    sleep(1);
                    return s.toUpperCase();
                })
                .thenAcceptBoth(
                        CompletableFuture.completedFuture(original)
                                .thenApply(s -> {
                                    sleep(2);
                                    return s.toLowerCase();
                                }),
                        (s1, s2) -> result.append(s1).append(s2)
                );
        System.out.println(result);
        stopWatch.stop();

        // 整个流水线是同步的，所以getNow()会得到最终的结果，它把大写和小写字符串连接起来。
        stopWatch.start("thenCombine");
        CompletableFuture<String> cf = CompletableFuture.completedFuture(original)
                .thenApply(s -> {
                    sleep(1);
                    return s.toUpperCase();
                })
                .thenCombine(CompletableFuture.completedFuture(original)
                                .thenApply(s -> {
                                    sleep(2);
                                    return s.toLowerCase();
                                }),
                        (s1, s2) -> s1 + s2);
        System.out.println(cf.getNow(null));
        stopWatch.stop();

        // 依赖的前两个阶段异步地执行，所以thenCombine()也异步地执行，即时它没有Async后缀。
        stopWatch.start("thenCombineAsync");
        CompletableFuture<String> cf2 = CompletableFuture.completedFuture(original)
                .thenApplyAsync(s -> {
                    sleep(1);
                    return s.toUpperCase();
                })
                .thenCombine(CompletableFuture.completedFuture(original)
                                .thenApplyAsync(s -> {
                                    sleep(2);
                                    return s.toLowerCase();
                                }),
                        (s1, s2) -> s1 + s2);
        System.out.println(cf2.getNow(null));
        System.out.println(cf2.join());
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }

    /**
     *
     */
    @Test
    void anyOfExample() {

        StringBuilder result = new StringBuilder();
        List messages = Arrays.asList("a", "b", "c");
        List<CompletableFuture> futures = (List<CompletableFuture>) messages.stream()
                .map(msg -> CompletableFuture.completedFuture(msg)
                        .thenApply(s -> s.toString().toUpperCase())
                )
                .collect(Collectors.toList());

        // 当几个阶段中的一个完成，创建一个完成的阶段
        CompletableFuture.anyOf(futures.toArray(new CompletableFuture[futures.size()])).whenComplete((res, th) -> {
            if (th == null) {
                //assertTrue(isUpperCase((String) res));
                System.out.println(res);
                result.append(res);
            }
        });
        System.out.println(result);

        // 当所有的阶段都完成后创建一个阶段
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
                .whenComplete((v, th) -> {
                    futures.forEach(cf -> System.out.println(cf.getNow(null)));
                    result.append("done");
                });
        System.out.println(result);

        // 当所有的阶段都完成后异步地创建一个阶段
        CompletableFuture allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
                .whenComplete((v, th) -> {
                    futures.forEach(cf -> System.out.println(cf.getNow(null)));
                    result.append("done");
                });
        allOf.join();
        System.out.println(result);


        // 首先异步调用cars方法获得Car的列表，它返回CompletionStage场景。cars消费一个远程的REST API。
        //然后我们复合一个CompletionStage填写每个汽车的评分，通过rating(manufacturerId)返回一个CompletionStage, 它会异步地获取汽车的评分(可能又是一个REST API调用)
        //当所有的汽车填好评分后，我们结束这个列表，所以我们调用allOf得到最终的阶段， 它在前面阶段所有阶段完成后才完成。
        //在最终的阶段调用whenComplete(),我们打印出每个汽车和它的评分

        // 因为每个汽车的实例都是独立的，得到每个汽车的评分都可以异步地执行，这会提高系统的性能(延迟)，而且，等待所有的汽车评分被处理使用的是allOf方法，而不是手工的线程等待(Thread#join() 或 a CountDownLatch)。

        //cars().thenCompose(cars -> {
        //    List<CompletionStage> updatedCars = cars.stream()
        //            .map(car -> rating(car.manufacturerId).thenApply(r -> {
        //                car.setRating(r);
        //                return car;
        //            })).collect(Collectors.toList());
        //
        //    CompletableFuture done = CompletableFuture
        //            .allOf(updatedCars.toArray(new CompletableFuture[updatedCars.size()]));
        //    return done.thenApply(v -> updatedCars.stream().map(CompletionStage::toCompletableFuture)
        //            .map(CompletableFuture::join).collect(Collectors.toList()));
        //}).whenComplete((cars, th) -> {
        //    if (th == null) {
        //        cars.forEach(System.out::println);
        //    } else {
        //        throw new RuntimeException(th);
        //    }
        //}).toCompletableFuture().join();
    }


    private void randomSleep() {
        try {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(3));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sleepEnough() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
