/**
 * 几种常见的并行模式：
 * 1.单例模式：
 * 1）饿汉式-直接初始化的时候new，性能差
 * 2）懒汉式-方法上加synchronized,性能差；使用双校验锁+volatile；静态内部类
 * 2.Future模式
 * 3.生产消费者模式
 * 4.分而治之：Mater-Worker模式；ForkJoin线程池
 */
package com.vailter.standard.learn.concuencymodel;