package com.vailter.standard.concurrency;

import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureDemo {

    final static ExecutorService executor = new ThreadPoolExecutor(6, 6,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    @Test
    void test1() throws InterruptedException, ExecutionException {
        //String res1 = getResult1();
        //String res2 = getResult2();
        //String res3 = getResult3();
        //saveDb(res1);
        //saveDb(res2);
        //saveDb(res3);

        StopWatch stopWatch = new StopWatch();

        int taskSize = 3;
        //ExecutorService executor = Executors.newFixedThreadPool(taskSize);
        Future<String> future1 = executor.submit(FutureDemo::getResult1);
        Future<String> future2 = executor.submit(FutureDemo::getResult2);
        Future<String> future3 = executor.submit(FutureDemo::getResult3);
        //List<Future<String>> futureList = new ArrayList<>();
        //futureList.add(future1);
        //futureList.add(future2);
        //futureList.add(future3);
        //// 轮询,获取完成任务的返回结果
        //stopWatch.start();
        //while (taskSize > 0) {
        //    for (Future<String> future : futureList) {
        //        String result = null;
        //        try {
        //            result = future.get(0, TimeUnit.SECONDS);
        //        } catch (InterruptedException e) {
        //            taskSize--;
        //            e.printStackTrace();
        //        } catch (ExecutionException e) {
        //            taskSize--;
        //            e.printStackTrace();
        //        } catch (TimeoutException e) {
        //            // 超时异常需要忽略,因为我们设置了等待时间为0,只要任务没有完成,就会报该异常
        //        }
        //        // 任务已经完成
        //        if (result != null) {
        //            System.out.println("result=" + result);
        //            // 从future列表中删除已经完成的任务
        //            futureList.remove(future);
        //            taskSize--;
        //            // 此处必须break，否则会抛出并发修改异常。（也可以通过将futureList声明为CopyOnWriteArrayList类型解决）
        //            break; // 进行下一次while循环
        //        }
        //    }
        //}
        //stopWatch.stop();
        //System.out.println(stopWatch.getTotalTimeMillis());


        // 创建阻塞队列
        //stopWatch.start();
        //BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>(taskSize);
        //executor.execute(() -> run(future1, blockingQueue));
        //executor.execute(() -> run(future2, blockingQueue));
        //executor.execute(() -> run(future3, blockingQueue));
        //// 异步保存所有机票价格
        //for (int i = 0; i < 3; i++) {
        //    String result = blockingQueue.take();
        //    System.out.println(result);
        //    saveDb(result);
        //}
        //stopWatch.stop();
        //System.out.println(stopWatch.getTotalTimeMillis());

        stopWatch.start();
        CompletionService<String> completionService = new ExecutorCompletionService<>(executor);
        completionService.submit(FutureDemo::getResult1);
        completionService.submit(FutureDemo::getResult2);
        completionService.submit(FutureDemo::getResult3);
        //for (int i = 0; i < 3; i++) {
        //    String result = completionService.take().get();
        //    System.out.println(result);
        //    saveDb(result);
        //}

        int count = 0;
        while (count < 3) {
            // 等待三个任务完成
            Future<String> future = completionService.poll();
            if (future != null) {
                System.out.println(future.get());
                count++;
            }
        }

        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

    private static void run(Future<String> flightPriceFuture, BlockingQueue<String> blockingQueue) {
        try {
            blockingQueue.put(flightPriceFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    /**
     * 模拟请求1
     *
     * @return
     * @throws InterruptedException
     */
    public static String getResult1() throws InterruptedException {
        Thread.sleep(10000);
        return "res1";
    }

    /**
     * 模拟请求w2
     *
     * @return
     * @throws InterruptedException
     */
    public static String getResult2() throws InterruptedException {
        Thread.sleep(5000);
        return "res2";
    }


    /**
     * 模拟请求3
     *
     * @return
     * @throws InterruptedException
     */
    public static String getResult3() throws InterruptedException {
        Thread.sleep(3000);
        return "res3";
    }

    /**
     * 保存DB
     *
     * @param result
     */
    public static void saveDb(String result) {
        // 解析字符串 进行异步入库
    }
}
