package com.vailter.standard.learn.concuencymodel;

public class SingleTonDemo {
    private static SingleTonDemo single;

    //public static synchronized SingleTonDemo getInstance() {
    //    if (single == null) {
    //        single = new SingleTonDemo();
    //    }
    //    return single;
    //}

    //DCL单例模式（Double Check + Lock）


    //volatite关键词防止指令重排序,下文介绍

    private static volatile SingleTonDemo singleton;

    private SingleTonDemo() {
    }

    //public static SingleTonDemo getInstance() {
    //    //如果singleton不为空,则直接返回对象,若多个线程发现singleton为空，则进入分支
    //    if (singleton == null) {
    //        //多个线程同时争抢一个锁，只有一个线程能成功，其他线程需等待
    //        synchronized (SingleTonDemo.class) {
    //            //争抢到锁的线程需再次判断singleton是否为空，因为有可能被上个线程实例化了
    //            //若不为空则实例化,后续线程再进入的时候则直接返回该对象
    //            //对于之后所有进入该方法的线程则无需获取锁，直接返回对象
    //            if (singleton == null) {
    //                singleton = new SingleTonDemo();
    //            }
    //        }
    //    }
    //    return singleton;
    //}

    public static SingleTonDemo getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder{
        public static SingleTonDemo INSTANCE = new SingleTonDemo();
    }
}
