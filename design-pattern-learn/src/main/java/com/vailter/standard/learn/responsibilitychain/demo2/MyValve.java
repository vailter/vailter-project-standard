package com.vailter.standard.learn.responsibilitychain.demo2;

public class MyValve implements Valve {
    protected Valve next = null;

    @Override
    public Valve getNext() {
        return next;
    }

    @Override
    public void setNext(Valve valve) {
        this.next = valve;
    }

    @Override
    public void invoke(Request request, Response response) {
        //处理request,response
        //...
        System.out.println("execute myValve...");
        getNext().invoke(request, response);
        return;
    }
}
