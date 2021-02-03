package com.vailter.standard.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vailter.standard.exception.AppException;
import com.vailter.standard.ret.CodeMsg;
import com.vailter.standard.ret.CommonResult;
import com.vailter.standard.ret.Result;
import com.vailter.standard.ret.v2.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

/**
 * 配置全局返回值
 */
//@EnableWebMvc
//@Configuration
public class UnifiedReturnConfig {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @RestControllerAdvice("com.vailter.standard")
    static class ResultResponseAdvice implements ResponseBodyAdvice<Object> {
        @Override
        public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converter) {
            // 如果接口返回的类型本身就是 Result 那就没有必要进行额外的操作，返回false
            return !methodParameter.getGenericParameterType().equals(Result.class)
                    || !methodParameter.getGenericParameterType().equals(CommonResult.class);
        }

        @Override
        public Object beforeBodyWrite(Object body,
                                      MethodParameter methodParameter,
                                      MediaType mediaType,
                                      Class<? extends HttpMessageConverter<?>> convert,
                                      ServerHttpRequest serverHttpRequest,
                                      ServerHttpResponse serverHttpResponse) {
            if (body instanceof CommonResult || body instanceof Result) {
                return body;
            }

            // 分页对象
            if (body instanceof PageUtils) {
                return body;
            }

            if (convert == StringHttpMessageConverter.class) {
                try {
                    return objectMapper.writeValueAsString(new CommonResult<>(body));
                } catch (JsonProcessingException e) {
                    return body;
                }
            }
            return new CommonResult<>(body);
        }
    }
}
