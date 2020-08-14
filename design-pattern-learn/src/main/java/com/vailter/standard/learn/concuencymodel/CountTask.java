package com.vailter.standard.learn.concuencymodel;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * CountTask:
 * 将分割出来的子任务放入双端队列中, 然后几个启动线程从双端队列中获取任务执行.
 * 子任务执行的结果放到一个队列里, 另起线程从队列中获取数据, 合并结果.
 * <p>
 * ForkJoin线程池使用一个无锁的栈来管理空闲线程, 如果一个工作线程暂时取不到可用的任务, 则可能被挂起.
 * 挂起的线程将被压入由线程池维护的栈中, 待将来有任务可用时, 再从栈中唤醒这些线程.
 * <p>
 * 假设我们的场景需要计算从0到20000000L的累加求和. CountTask继承自RecursiveTask, 可以携带返回值.
 * 每次分解大任务, 简单的将任务划分为100个等规模的小任务, 并使用fork()提交子任务.
 * 在子任务中通过THRESHOLD设置子任务分解的阈值, 如果当前需要求和的总数大于THRESHOLD, 则子任务需要再次分解,如果子任务可以直接执行, 则进行求和操作, 返回结果. 最终等待所有的子任务执行完毕, 对所有结果求和.
 */
public class CountTask extends RecursiveTask<Long> {
    // 任务分解的阈值
    private static final int THRESHOLD = 2000;
    private final long start;
    private final long end;


    public CountTask(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public Long compute() {
        long sum = 0;
        boolean canCompute = (end - start) < THRESHOLD;
        if (canCompute) {
            for (long i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            // 分成100个小任务
            long step = (start + end) / 3;
            ArrayList<CountTask> subTasks = new ArrayList<>();
            long pos = start;
            for (int i = 0; i < 3; i++) {
                long lastOne = pos + step;
                if (lastOne > end) {
                    lastOne = end;
                }
                CountTask subTask = new CountTask(pos, lastOne);
                pos += step + 1;
                // 将子任务推向线程池
                subTasks.add(subTask);
                subTask.fork();
            }

            for (CountTask task : subTasks) {
                // 对结果进行join
                sum += task.join();
            }
        }
        return sum;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool pool = new ForkJoinPool();
        // 累加求和 0 -> 20000000L
        CountTask task = new CountTask(0, 5000);
        ForkJoinTask<Long> result = pool.submit(task);
        System.out.println("sum result : " + result.get());
    }
}
