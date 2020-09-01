package com.vailter.standard.learn.proxy.dynamic.jdk;

public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHi(String name) {
        return "Hi, " + name;
    }

    @Override
    public void sayHi() {
        System.out.println("111");
    }
}
