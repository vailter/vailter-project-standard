package com.vailter.standard.config.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vailter.standard.annotations.AccessLimit;
import com.vailter.standard.ret.CodeMsg;
import com.vailter.standard.ret.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 接口防刷拦截器
 */
public class AccessLimitInterceptor extends HandlerInterceptorAdapter {

    private final ObjectMapper mapper = new ObjectMapper();

    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //判断请求是否属于方法的请求
        if (handler instanceof HandlerMethod) {

            HandlerMethod hm = (HandlerMethod) handler;

            //获取方法中的注解,看是否有该注解
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean login = accessLimit.needLogin();
            String key = request.getRequestURI();
            //如果需要登录
            if (login) {
                //获取登录的session进行判断
                //.....
                key += "" + "1";  //这里假设用户是1,项目中是动态获取的userId
            }

            // 从redis中获取用户访问的次数
            Integer count = (Integer) redisTemplate.opsForValue().get(key);
//            Integer count = redisService.get(ak, key, Integer.class);
            if (count == null) {
                //第一次访问
//                redisService.set(ak, key, 1);
                redisTemplate.opsForValue().set(key, 1, seconds, TimeUnit.SECONDS);
            } else if (count < maxCount) {
                //加1
//                redisService.incr(ak, key);
                redisTemplate.opsForValue().increment(key, 1);
            } else {
                //超出访问次数
                render(response, CodeMsg.ACCESS_LIMIT_REACHED); //这里的CodeMsg是一个返回参数
                return false;
            }
        }

        return true;

    }

    private void render(HttpServletResponse response, CodeMsg cm) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = mapper.writeValueAsString(Result.error(cm));
        out.write(str.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
