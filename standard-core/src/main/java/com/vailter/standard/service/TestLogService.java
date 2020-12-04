package com.vailter.standard.service;

import com.github.LogData;
import org.springframework.stereotype.Service;

@Service
public class TestLogService {
    public String get(String name) {
        LogData.step("[{}] get",name);
        return "hi, " + name;
    }
}
