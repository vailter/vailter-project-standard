package com.vailter.standard.learn.chain.demo3;

public interface Handler {
    void doHandler(String request, String response, HandlerChain chain);
}
