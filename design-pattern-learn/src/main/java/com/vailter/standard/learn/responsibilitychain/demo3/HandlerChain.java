package com.vailter.standard.learn.responsibilitychain.demo3;

import java.util.ArrayList;
import java.util.List;

public class HandlerChain {
    private final List<Handler> handlers = new ArrayList<>();
    private int index = 0;// 标记调用链位置

    public HandlerChain addHandler(Handler handler) {
        handlers.add(handler);
        return this;
    }

    // 根据index执行链中元素
    public void doHandler(String request, String response) {
        if (index != handlers.size()) {
            Handler handler = handlers.get(index);
            index++;
            handler.doHandler(request, response, this);
        }
    }
}
