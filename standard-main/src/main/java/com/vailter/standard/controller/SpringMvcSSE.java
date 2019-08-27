package com.vailter.standard.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Vailter67
 */
@RestController
public class SpringMvcSSE {
    private ConcurrentHashMap<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    /**
     * 必须必须返回SseEmitter对象，SseEmitter对象是Session级别的，
     * 如果你要点对点针对每个session要独立存储。
     * 如果你是广播可以共用一个SseEmitter对象。
     * 按照SSE规范也必须声明produces为"text/event-stream"。
     * 当你调用该接口的时候将建立起SSE连接。
     *
     * @param id
     * @return
     */
    @GetMapping(value = "events/{id}", consumes = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sseEmitter(@PathVariable String id) {
        SseEmitter sseEmitter = new SseEmitter();
        this.sseEmitterMap.put(id, sseEmitter);
        return sseEmitter;
    }
}
