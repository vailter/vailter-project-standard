package com.vailter.standard.learn.responsibilitychain.demo2;

public interface Valve {
    public Valve getNext();

    public void setNext(Valve valve);

    public void invoke(Request request, Response response);
}
