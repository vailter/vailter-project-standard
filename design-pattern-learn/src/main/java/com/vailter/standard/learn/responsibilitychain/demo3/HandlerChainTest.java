package com.vailter.standard.learn.responsibilitychain.demo3;

public class HandlerChainTest {
    public static void main(String[] args) {
        String req = "request";
        String resp = "response";
        HandlerChain chain = new HandlerChain();
        chain.addHandler(new HtmlHandler())
                .addHandler(new TextHandler());
        chain.doHandler(req, resp);
    }
}
