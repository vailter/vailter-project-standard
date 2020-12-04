package com.vailter.standard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.LogData;
import com.github.collector.LogCollector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AopLogCollector implements LogCollector {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void collect(LogData logData) {
        try {
            log.info(objectMapper.writeValueAsString(logData));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
