package com.vailter.standard.config;

import com.vailter.standard.exception.AppException;
import com.vailter.standard.ret.CodeMsg;
import com.vailter.standard.ret.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * 全局异常处理器
 *
 * @author Vailter67
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Result handleBindException(BindException ex) {
        // ex.getFieldError():随机返回一个对象属性的异常信息。如果要一次性返回所有对象属性异常信息，则调用ex.getAllErrors()
        FieldError fieldError = ex.getFieldError();
        String errorMsg = null == fieldError ? "参数不合法" : fieldError.getDefaultMessage();
        log.error(errorMsg, ex);
        return Result.error(CodeMsg.BIND_ERROR.fillArgs(errorMsg));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        String msg = "参数不合法";
        if (errors.size() > 0) {
            msg = errors.get(0).getDefaultMessage();
        }
        log.error(msg, ex);
        return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result handleConstraintViolationException(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        StringBuilder message = new StringBuilder();
        for (ConstraintViolation<?> item : violations) {
            message.append(item.getMessage()).append(", ");
        }
        String errorMsg = message.substring(0, message.lastIndexOf(","));
        log.error(errorMsg, ex);
        return Result.error(CodeMsg.BIND_ERROR.fillArgs(errorMsg));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result methodNotSupportHandler(HttpServletRequest request, HttpRequestMethodNotSupportedException ex) {
        String method = ex.getMethod();
        Set<HttpMethod> supportedMethodSet = ex.getSupportedHttpMethods();
        String errorMsg = String.format("[%s]请求方式不支持，请求地址：[%s]，支持的请求方式：%s", method, request.getRequestURI(), supportedMethodSet);
        log.error(errorMsg, ex);
        return Result.error(CodeMsg.NOT_SUPPORTED_HTTP_METHOD.fillArgs(method, supportedMethodSet));
    }

    @ExceptionHandler(AppException.class)
    public Result handleDataDisposeException(AppException ex) {
        return Result.error(ex.getCm());
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception ex, HttpServletRequest request) {
        if (ex instanceof HttpMessageNotReadableException) {
            HttpMessageNotReadableException exception = (HttpMessageNotReadableException) ex;
            log.error("参数校验异常：", ex);
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(exception.getLocalizedMessage()));
        }

        if (ex instanceof HttpMediaTypeNotSupportedException) {
            HttpMediaTypeNotSupportedException exception = (HttpMediaTypeNotSupportedException) ex;
            String errorMsg = String.format("请求地址[%s]，Content-Type[%s]不支持：%s，支持的Content-Type：%s",
                    request.getRequestURI(), exception.getContentType(), exception.getMessage(), exception.getSupportedMediaTypes());
            log.error(errorMsg, ex);
            return Result.error(CodeMsg.NOT_SUPPORTED_CONTENT_TYPE.fillArgs(exception.getMessage(), exception.getSupportedMediaTypes()));
        }
        log.error("未知异常: ", ex);
        return Result.error(CodeMsg.SERVER_ERROR);
    }
}
