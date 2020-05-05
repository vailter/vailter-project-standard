package com.vailter.standard.java8;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Lambda表达式的语法非常简洁，但是使用时有几个问题需要特别注意：
 * <p>
 * 1. 使用Lambda表达式必须具有接口，且要求接口中有且仅有一个抽象方法。
 * 2. 使用Lambda必须具有上下文推断。也就是方法的参数或局部变量类型必须为Lambda对应的接口类型，才能使用Lambda作为该接口的实例。
 * <p>
 * Lambda标准形式
 * (参数类型 参数名称) ‐> { 代码语句 }
 * 说明：
 * 1. 小括号内：没有参数就留空（），多个参数就用逗号分隔。
 * 2. -> 是新引入的语法格式，代表指向动作。
 * 3. 大括号内的语法与传统方法体要求基本一致。
 * <p>
 * Lambda的省略：凡是可以根据上下文推导得知的信息，都可以省略
 * <p>
 * 在Lambda表达式标准形式的基础上：
 * 1. 小括号内参数的类型可以省略；
 * 2. 如果小括号内只有一个参数，则小括号可以省略；
 * 3. 如果大括号内只有一个语句，则无论是否有返回值，都可以省略大括号、return关键字及语句分号。
 * 综上所述，从 Java 8 开始，lambda 是迄今为止表示小函数对象的最佳方式。除非必须创建非函数式接口类型的实例，否则不要使用匿名类作为函数对象。
 */
public class LambdaDemo1 {

    public static void main(String[] args) {
        String a = "arqasasdas";

        boolean aBoolean = a.startsWith("ac");

//        把右边那块代码，赋给一个叫做aBlockOfCode的Java变量：
//        aBlockCode = public void doSomething(String param) {
//            System.out.println(param);
//        }

//        public是多余的
//        aBlockCode = void doSomething(String param) {
//            System.out.println(param);
//        }

//        函数名 doSomething 是多余的，因为已经赋值给 aBlockCode
//        aBlockCode = void (String param) {
//            System.out.println(param);
//        }

//        void不需要，编译器可以判断返回类型
//        aBlockCode = (String param) {
//            System.out.println(param);
//        }

//        编译器可以判断参数类型
//        aBlockCode = (param) {
//            System.out.println(param);
//        }

//        只有一行可以不用大括号
//        aBlockCode = (param) -> System.out.println(param);

//        最简洁的
//        aBlockCode = System.out::println;

//        我们就成功的非常优雅的把“一块代码”赋给了一个变量。而“这块代码”，或者说“这个被赋给一个变量的函数”，就是一个Lambda表达式。
//        这个 aBlockCode 是什么类型

        // java.util.Comparator
        List<String> words = Arrays.asList("qwewq", "ewqeq", "2312", "d", "12312dads");
//        Collections.sort(words, new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                return Integer.compare(o1.length(), o2.length());
//            }
//        });

//        words.sort((o1, o2) -> Integer.compare(o1.length(), o2.length()));
        words.sort(Comparator.comparingInt(String::length));
        System.out.println(words);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println(111);
//            }
//        }).start();

        new Thread(() -> System.out.println(Thread.currentThread().getName() + ":" + 111)).start();
    }
}
