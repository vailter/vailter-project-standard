package com.vailter.standard.learn.chain.demo2;

public interface Pipeline {
    public Valve getBasic();
    public void setBasic(Valve valve);
    public void addValve(Valve valve);
    public Valve getFirst();
}
