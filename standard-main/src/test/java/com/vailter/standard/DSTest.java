package com.vailter.standard;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class DSTest {
    @Resource
    private TestService testService;

    @Test
    void testDs1() {
        testService.test();
    }
}
