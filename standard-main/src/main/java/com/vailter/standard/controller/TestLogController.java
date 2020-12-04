package com.vailter.standard.controller;

import com.github.AopLog;
import com.github.LogData;
import com.vailter.standard.service.TestLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@AopLog(type = "测试类型", stackTraceOnErr = true)
@RestController
public class TestLogController {

    @Autowired
    private TestLogService testLogService;

    @GetMapping(value = "get", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String get(String name) {
        LogData.step("1. 第一步执行完成");
        //......
        LogData.step("2. 第二步执行完成");
        //.....
        LogData.step("3. service的方法执行完成");
        //.....
        return testLogService.get(name);
    }
}
