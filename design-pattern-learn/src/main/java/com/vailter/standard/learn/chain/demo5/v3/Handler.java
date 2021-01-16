package com.vailter.standard.learn.chain.demo5.v3;

import com.vailter.standard.learn.chain.demo5.Member;

import java.util.Objects;

public abstract class Handler<T> {
    protected Handler<T> next;

    public void next(Handler<T> next) {
        this.next = next;
    }

    abstract void doHandler(T t);

    public static class Builder<T> {
        private Handler<T> head;
        private Handler<T> tail;

        public Builder<T> addHandler(Handler<T> handler) {
            if (Objects.isNull(head)) {
                this.head = this.tail = handler;
                return this;
            }
            this.tail.next(handler);
            this.tail = handler;
            return this;
        }

        public Handler<T> build() {
            return this.head;
        }
    }
}
