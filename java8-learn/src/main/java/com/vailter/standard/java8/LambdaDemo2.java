package com.vailter.standard.java8;

public class LambdaDemo2 {
    public static void main(String[] args) {
        String param = "eqweqweqw";
        //  before Java8
//        MyLambdaInterface myLambdaInterface1 = new MyLamdaInterfaceImpl();
        MyLambdaInterface myLambdaInterface1 = new MyLambdaInterface() {
            @Override
            public void doSomeThing(String param) {
                System.out.println(param);
            }
        };
        myLambdaInterface1.doSomeThing(param);

        // java 8
        MyLambdaInterface myLambdaInterface2 = System.out::println;
        myLambdaInterface2.doSomeThing(param);

        // 作为方法参数
        test(myLambdaInterface1, param);
        test(System.out::println, param);
    }

    public static void test(MyLambdaInterface myLambdaInterface, String s) {
        myLambdaInterface.doSomeThing(s);
    }
}
