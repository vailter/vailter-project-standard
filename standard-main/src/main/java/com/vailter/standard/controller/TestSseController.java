package com.vailter.standard.controller;

import com.vailter.standard.utils.SseEmitterServer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@CrossOrigin
@RestController
@RequestMapping("/sse")
public class TestSseController {
    /**
     * 用于创建连接
     */
    @GetMapping("/connect/{userId}")
    public SseEmitter connect(@PathVariable String userId) {
        return SseEmitterServer.connect(userId);
    }

    /**
     * 推送给所有人
     *
     * @param message
     * @return
     */
    @GetMapping(value = "/push/{message}",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public String push(@PathVariable(name = "message") String message) {
        SseEmitterServer.batchSendMessage(message);
        return "WebSocket 推送消息给所有人";
    }

    /**
     * 发送给单个人
     *
     * @param message
     * @param userid
     * @return
     */
    @GetMapping("/push_one/{message}/{userid}")
    public String pushOne(@PathVariable(name = "message") String message, @PathVariable(name = "userid") String userid) {
        SseEmitterServer.sendMessage(userid, message);
        return "WebSocket 推送消息给" + userid;
    }

    /**
     * 关闭连接
     */
    @GetMapping("/close/{userid}")
    public ResponseEntity<String> close(@PathVariable("userid") String userid) {
        SseEmitterServer.removeUser(userid);
        return ResponseEntity.ok("连接关闭");
    }
}
