package com.vailter.standard.learn.chain.demo5.v2;

import com.vailter.standard.learn.chain.demo5.Member;

public abstract class Handler {
    protected Handler next;

    public void next(Handler next) {
        this.next = next;
    }

    abstract void doHandler(Member member);
}
