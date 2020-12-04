package com.vailter.standard.config;

import com.vailter.standard.ret.CommonResult;
import com.vailter.standard.ret.Result;
import com.vailter.standard.ret.v2.PageUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 配置全局返回值
 */
@EnableWebMvc
@Configuration
public class UnifiedReturnConfig {

    @RestControllerAdvice("com.vailter.standard")
    static class CommonResultResponseAdvice implements ResponseBodyAdvice<Object> {
        @Override
        public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
            // 如果接口返回的类型本身就是 Result 那就没有必要进行额外的操作，返回false
            return !methodParameter.getGenericParameterType().equals(Result.class)
                    || !methodParameter.getGenericParameterType().equals(CommonResult.class);
        }

        @Override
        public Object beforeBodyWrite(Object body,
                                      MethodParameter methodParameter,
                                      MediaType mediaType,
                                      Class<? extends HttpMessageConverter<?>> aClass,
                                      ServerHttpRequest serverHttpRequest,
                                      ServerHttpResponse serverHttpResponse) {
            if (body instanceof CommonResult || body instanceof Result) {
                return body;
            }

            if (body instanceof PageUtils) {
                return body;
            }

            //if (aClass == StringHttpMessageConverter.class) {
            //    return body;
            //}
            //if (mediaType.includes(MediaType.TEXT_HTML) || mediaType.includes(MediaType.TEXT_PLAIN)) {
            //    return body;
            //}
            return new CommonResult<>(body);
        }
    }
}
