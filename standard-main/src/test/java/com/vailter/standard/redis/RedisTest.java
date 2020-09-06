package com.vailter.standard.redis;

import com.vailter.standard.utils.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.lang.reflect.Field;

//@SpringBootTest
public class RedisTest {
    @Resource
    private RedisUtils redisUtils;

    @Test
    void testCache() throws NoSuchFieldException, IllegalAccessException {
        //String key = "dadasdasdasd";
        //redisUtils.set(key, "weqwewqeqw");
        //System.out.println(redisUtils.get(key));
        //redisUtils.remove(key);
        //System.out.println(redisUtils.get(key));

        Class cache = Integer.class.getDeclaredClasses()[0]; //1
        Field myCache = cache.getDeclaredField("cache"); //2
        myCache.setAccessible(true);//3

        Integer[] newCache = (Integer[]) myCache.get(cache); //4
        newCache[132] = newCache[133]; //5

        int a = 2;
        int b = a + a;
        System.out.printf("%d + %d = %d", a, a, b);

    }
}
