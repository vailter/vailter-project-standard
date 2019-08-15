package com.vailter.standard;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * wait: 释放当前锁，阻塞直到被notify或notifyAll唤醒，或者超时，或者线程被中断(InterruptedException)
 * <p>
 * notify: 任意选择一个（无法控制选哪个）正在这个对象上等待的线程把它唤醒，其它线程依然在等待被唤醒
 * <p>
 * notifyAll: 唤醒所有线程，让它们去竞争，不过也只有一个能抢到锁
 * <p>
 * sleep: 不是Object中的方法，而是Thread类的静态方法，让当前线程持有锁阻塞指定时间
 */
public class ThreadTest {
    // 日志行号记录
    private AtomicInteger count = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        ThreadTest threadTest = new ThreadTest();
        // 测试sleep wait 开启两个线程去执行test方法
        //new Thread(threadTest::testSleepAndWait).start();
        //new Thread(threadTest::testSleepAndWait).start();

        // 测试notify 开启两个线程去执行test方法, notify时如果不能准确控制和wait的线程数对应，可能会导致某些线程永远阻塞。
        //for (int i = 0; i < 10; i++) {
        //    new Thread(threadTest::testWait).start();
        //}
        //Thread.sleep(1000);
        //for (int i = 0; i < 9; i++) {
        //    threadTest.testNotify();
        //}

        // 测试notifyAll 开启两个线程去执行test方法
        for (int i = 0; i < 5; i++) {
            new Thread(threadTest::testWait).start();
        }
        Thread.sleep(1000);
        threadTest.testNotifyAll();

    }

    /**
     * sleep和wait
     */
    private synchronized void testSleepAndWait() {
        try {
            log("进入了同步方法，并开始睡觉，1s");
            // sleep不会释放锁，因此其他线程不能进入这个方法
            Thread.sleep(1000);
            log("睡好了，但没事做，有事叫我，等待2s");
            //阻塞在此，并且释放锁，其它线程可以进入这个方法
            //当其它线程调用此对象的notify或者notifyAll时才有机会停止阻塞
            //就算没有人notify，如果超时了也会停止阻塞
            wait(2000);
            log("我要走了，但我要再睡一觉，10s");
            //这里睡的时间很长，因为没有释放锁，其它线程就算wait超时了也无法继续执行
            Thread.sleep(10000);
            log("走了");
            notify();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * notify：唤醒一个线程，其他线程依然处于wait的等待唤醒状态，如果被唤醒的线程结束时没调用notify，其他线程就永远没人去唤醒，只能等待超时，或者被中断
     * <p>
     * notifyAll：所有线程退出wait的状态，开始竞争锁，但只有一个线程能抢到，这个线程执行完后，其他线程又会有一个幸运儿脱颖而出得到锁
     */
    private synchronized void testWait() {
        try {
            log("进入了同步方法，开始wait");
            wait();
            log("wait结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void testNotify() {
        notify();
    }

    private synchronized void testNotifyAll() {
        notifyAll();
    }

    // 打印日志
    private void log(String s) {
        System.out.println(count.incrementAndGet() + " "
                + new Date().toString().split(" ")[3]
                + "\t" + Thread.currentThread().getName() + " " + s);
    }

}
