package com.vailter.standard.learn.chain.demo3;

public class TextHandler implements Handler {
    @Override
    public void doHandler(String request, String response, HandlerChain chain) {
        System.out.println("this is text");
        chain.doHandler(request, response);// 返回责任链入口
    }
}
