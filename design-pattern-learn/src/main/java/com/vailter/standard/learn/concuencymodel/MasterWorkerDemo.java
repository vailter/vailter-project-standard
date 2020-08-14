package com.vailter.standard.learn.concuencymodel;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 假设一个场景, 需要计算100个任务, 并对结果求和, Master持有10个子进程.
 */
public class MasterWorkerDemo {
    // 盛装任务的集合
    private final ConcurrentLinkedQueue<TaskDemo> workQueue = new ConcurrentLinkedQueue<>();

    // 所有worker
    private final HashMap<String, Thread> workers = new HashMap<>();

    // 每一个worker并行执行任务的结果
    private final ConcurrentHashMap<String, Object> resultMap = new ConcurrentHashMap<>();

    public MasterWorkerDemo(WorkerDemo worker, int workerCount) {
        // 每个worker对象都需要持有queue的引用, 用于领任务与提交结果
        worker.setResultMap(resultMap);
        worker.setWorkQueue(workQueue);
        for (int i = 0; i < workerCount; i++) {
            workers.put("子节点: " + i, new Thread(worker));
        }
    }

    // 提交任务
    public void submit(TaskDemo task) {
        workQueue.add(task);
    }

    // 启动所有的子任务
    public void execute(){
        for (Map.Entry<String, Thread> entry : workers.entrySet()) {
            entry.getValue().start();
        }
    }

    // 判断所有的任务是否执行结束
    public boolean isComplete() {
        for (Map.Entry<String, Thread> entry : workers.entrySet()) {
            if (entry.getValue().getState() != Thread.State.TERMINATED) {
                return false;
            }
        }

        return true;
    }

    // 获取最终汇总的结果
    public int getResult() {
        int result = 0;
        for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
            result += Integer.parseInt(entry.getValue().toString());
        }

        return result;
    }

    public static class WorkerDemo implements Runnable{

        private ConcurrentLinkedQueue<TaskDemo> workQueue;
        private ConcurrentHashMap<String, Object> resultMap;

        @Override
        public void run() {
            while (true) {
                TaskDemo input = this.workQueue.poll();
                // 所有任务已经执行完毕
                if (input == null) {
                    break;
                }
                // 模拟对task进行处理, 返回结果
                int result = input.getPrice();
                this.resultMap.put(input.getId() + "", result);
                System.out.println("任务执行完毕, 当前线程: " + Thread.currentThread().getId());
            }
        }

        public ConcurrentLinkedQueue<TaskDemo> getWorkQueue() {
            return workQueue;
        }

        public void setWorkQueue(ConcurrentLinkedQueue<TaskDemo> workQueue) {
            this.workQueue = workQueue;
        }

        public ConcurrentHashMap<String, Object> getResultMap() {
            return resultMap;
        }

        public void setResultMap(ConcurrentHashMap<String, Object> resultMap) {
            this.resultMap = resultMap;
        }
    }

    public static class TaskDemo {

        private int id;
        private String name;
        private int price;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }
    }

    public static void main(String[] args) {
        MasterWorkerDemo master = new MasterWorkerDemo(new WorkerDemo(), 10);
        for (int i = 0; i < 1000; i++) {
            TaskDemo task = new TaskDemo();
            task.setId(i);
            task.setName("任务" + i);
            task.setPrice(new Random().nextInt(10000));
            master.submit(task);
        }

        master.execute();

        while (true) {
            if (master.isComplete()) {
                System.out.println("执行的结果为: " + master.getResult());
                break;
            }
        }
    }
}


