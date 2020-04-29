package com.vailter.standard.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SseEmitter（单向，服务端向客户端主动推送）
 * <p>
 * 服务器端实时推送技术之SseEmitter的用法
 * 用于和进度有关的操作: 安装进度, 部署进度, 任务执行进度等等.
 * <p>
 * * 测试步骤:
 * * 1.请求http://localhost:8080/sse/start?clientId=111接口,浏览器会阻塞,等待服务器返回结果;
 * * 2.请求http://localhost:8080/sse/send?clientId=111接口,可以请求多次,并观察第1步的浏览器返回结果;
 * * 3.请求http://localhost:8080/sse/end?clientId=111接口结束某个请求,第1步的浏览器将结束阻塞;
 * * 其中clientId代表请求的唯一标志;
 *
 * @author Vailter67
 */
@RestController
@RequestMapping("sse")
@Slf4j
public class SpringMvcSSE {
//    private ConcurrentHashMap<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

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
//    @GetMapping(value = "events/{id}", consumes = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public SseEmitter sseEmitter(@PathVariable String id) {
//        SseEmitter sseEmitter = new SseEmitter();
//        this.sseEmitterMap.put(id, sseEmitter);
//        return sseEmitter;
//    }

    // 用于保存每个请求对应的 SseEmitter
    private Map<String, Result> sseEmitterMap = new ConcurrentHashMap<>();

    /**
     * 返回SseEmitter对象
     *
     * @param clientId
     * @return
     */
    @RequestMapping("/start")
    public SseEmitter testSseEmitter(String clientId) {
        // 默认30秒超时,设置为0L则永不超时
        SseEmitter sseEmitter = new SseEmitter(0L);
        sseEmitterMap.put(clientId, new Result(clientId, System.currentTimeMillis(), sseEmitter));
        return sseEmitter;
    }

    /**
     * 向SseEmitter对象发送数据
     *
     * @param clientId
     * @return
     */
    @RequestMapping("/send")
    public String setSseEmitter(String clientId) {
        try {
            Result result = sseEmitterMap.get(clientId);
            if (result != null && result.sseEmitter != null) {
                long timestamp = System.currentTimeMillis();
                result.sseEmitter.send(timestamp);
            }
        } catch (IOException e) {
            log.error("IOException!", e);
            return "error";
        }

        return "Succeed!";
    }

    /**
     * 将SseEmitter对象设置成完成
     *
     * @param clientId
     * @return
     */
    @RequestMapping("/end")
    public String completeSseEmitter(String clientId) {
        Result result = sseEmitterMap.get(clientId);
        if (result != null) {
            sseEmitterMap.remove(clientId);
            result.sseEmitter.complete();
        }
        return "Succeed!";
    }

    private static class Result {
        String clientId;
        long timestamp;
        SseEmitter sseEmitter;

        Result(String clientId, long timestamp, SseEmitter sseEmitter) {
            this.clientId = clientId;
            this.timestamp = timestamp;
            this.sseEmitter = sseEmitter;
        }
    }
}
