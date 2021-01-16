package com.vailter.standard.learn.chain.demo3;

public class HtmlHandler implements Handler {
    @Override
    public void doHandler(String request, String response, HandlerChain chain) {
        System.out.println("this is html");
        chain.doHandler(request, response);// 返回责任链入口
    }
}
