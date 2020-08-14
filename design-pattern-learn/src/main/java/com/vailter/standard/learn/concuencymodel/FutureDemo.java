package com.vailter.standard.learn.concuencymodel;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 该模式的核心思想是异步调用. 有点类似于异步的ajax请求.
 * 当调用某个方法时, 可能该方法耗时较久, 而在主函数中也不急于立刻获取结果.
 * 因此可以让调用者立刻返回一个凭证, 该方法放到另外线程执行,后续主函数拿凭证再去获取方法的执行结果即可
 */
public class FutureDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        FutureTask<String> futureTask = new FutureTask<>(() -> new RealData().costTime());

        ExecutorService service = Executors.newCachedThreadPool();
        service.submit(futureTask);

        System.out.println("RealData方法调用完毕");
        // 模拟主函数中其他耗时操作
        doOtherThing();
        // 获取RealData方法的结果
        System.out.println(Thread.currentThread().getName() + ": " + futureTask.get());
        Future<String> future = service.submit(new RealData2());

        System.out.println("RealData2方法调用完毕");
        // 模拟主函数中其他耗时操作
        doOtherThing();
        // 获取RealData2方法的结果
        System.out.println(future.get());
        service.shutdown();
    }

    private static void doOtherThing() throws InterruptedException {
        Thread.sleep(2000L);
    }
}

class RealData {

    public String costTime() {
        try {
            // 模拟RealData耗时操作
            Thread.sleep(1000L);
            return Thread.currentThread().getName() + "result";
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Thread.currentThread().getName() + "exception";
    }

}

class RealData2 implements Callable<String> {

    public String costTime() {
        try {
            // 模拟RealData耗时操作
            Thread.sleep(1000L);
            return "result";
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "exception";
    }

    @Override
    public String call() throws Exception {
        return costTime();
    }
}
