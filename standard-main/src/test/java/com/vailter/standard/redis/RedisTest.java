package com.vailter.standard.redis;

import com.vailter.standard.utils.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class RedisTest {
    @Resource
    private RedisUtils redisUtils;

    @Test
    void testCache() {
        String key = "dadasdasdasd";
        redisUtils.set(key, "weqwewqeqw");
        System.out.println(redisUtils.get(key));
        //redisUtils.remove(key);
        //System.out.println(redisUtils.get(key));

    }
}
