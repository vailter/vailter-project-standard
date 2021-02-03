package com.vailter.standard.exception.v2;

import com.vailter.standard.exception.AppException;
import com.vailter.standard.ret.Result;
import com.vailter.standard.ret.v2.CommonResponseEnum;
import com.vailter.standard.ret.v2.ErrorResponse;
import com.vailter.standard.ret.v2.ServletResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 异常分成几类，实际上只有两大类，一类是ServletException、ServiceException，还记得上文提到的 按阶段分类 吗，即对应 进入Controller前的异常 和 Service 层异常；然后 ServiceException 再分成自定义异常、未知异常。对应关系如下：
 * <p>
 * 进入Controller前的异常: handleServletException、handleBindException、handleValidException
 * <p>
 * 自定义异常: handleBusinessException、handleBaseException
 * <p>
 * 未知异常: handleException
 */
@Slf4j
@RestControllerAdvice
@ConditionalOnWebApplication
public class UnifiedExceptionHandler {
    /**
     * 生产环境
     */
    private final static String ENV_PROD = "prod";

    //@Autowired
    //private UnifiedMessageSource unifiedMessageSource;

    /**
     * 当前环境
     */
    @Value("${spring.profiles.active}")
    private String profile;

    /**
     * 获取国际化消息
     *
     * @param e 异常
     * @return
     */
    //public String getMessage(BaseException e) {
    //    String code = "response." + e.getResponseEnum().toString();
    //    String message = unifiedMessageSource.getMessage(code, e.getArgs());
    //
    //    if (message == null || message.isEmpty()) {
    //        return e.getMessage();
    //    }
    //
    //    return message;
    //}

    /**
     * 业务异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = BusinessException.class)
    //@ResponseBody
    public ErrorResponse handleBusinessException(BaseException e) {
        log.error(e.getMessage(), e);

        return new ErrorResponse(e.getResponseEnum().getCode(), e.getMessage());
    }

    @ExceptionHandler(AppException.class)
    public Result<Void> handleDataDisposeException(AppException ex) {
        return Result.error(ex.getCm());
    }

    /**
     * 自定义异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = BaseException.class)
    //@ResponseBody
    public ErrorResponse handleBaseException(BaseException e) {
        log.error(e.getMessage(), e);

        return new ErrorResponse(e.getResponseEnum().getCode(), e.getMessage());
    }

    /**
     * Controller上一层相关异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler({
            NoHandlerFoundException.class, // 404
            HttpRequestMethodNotSupportedException.class, // get,post等请求方式不匹配
            HttpMediaTypeNotSupportedException.class, // 请求头不支持
            /*
            * 比如url为：/licence/{licenceId}，参数签名包含@PathVariable("licenceId")，
            * 当请求的url为/licence，在没有明确定义url为/licence的情况下，会被判定为：缺少路径参数；
            * */
            MissingPathVariableException.class, // 缺少路径参数
            MissingServletRequestParameterException.class, // 缺少请求参数
            TypeMismatchException.class, // 参数类型匹配失败
            // 与上面的HttpMediaTypeNotSupportedException举的例子完全相反，
            // 即请求头携带了"content-type: application/json;charset=UTF-8"，
            // 但接收参数却没有添加注解@RequestBody，或者请求体携带的 json 串反序列化成 pojo 的过程中失败了，也会抛该异常；
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class, // 返回的 pojo 在序列化成 json 过程失败了，那么抛该异常；
            HttpMediaTypeNotAcceptableException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })
    public ErrorResponse handleServletException(Exception e) {
        log.error(e.getMessage(), e);
        int code = CommonResponseEnum.SERVER_ERROR.getCode();
        try {
            ServletResponseEnum servletExceptionEnum = ServletResponseEnum.valueOf(e.getClass().getSimpleName());
            code = servletExceptionEnum.getCode();
        } catch (IllegalArgumentException e1) {
            log.error("class [{}] not defined in enum {}", e.getClass().getName(), ServletResponseEnum.class.getName());
        }

        if (ENV_PROD.equals(profile)) {
            // 当为生产环境, 不适合把具体的异常信息展示给用户, 比如404.
            code = CommonResponseEnum.SERVER_ERROR.getCode();
            BaseException baseException = new BaseException(CommonResponseEnum.SERVER_ERROR);
            String message = baseException.getMessage();
            return new ErrorResponse(code, message);
        }

        return new ErrorResponse(code, e.getMessage());
    }


    /**
     * 参数绑定异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = BindException.class)
    //@ResponseBody
    public ErrorResponse handleBindException(BindException e) {
        log.error("参数绑定校验异常", e);

        return wrapperBindingResult(e.getBindingResult());
    }

    /**
     * 参数校验异常，将校验失败的所有异常组合成一条错误信息
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    //@ResponseBody
    public ErrorResponse handleValidException(MethodArgumentNotValidException e) {
        log.error("参数绑定校验异常", e);

        return wrapperBindingResult(e.getBindingResult());
    }

    /**
     * 包装绑定异常结果
     *
     * @param bindingResult 绑定结果
     * @return 异常结果
     */
    private ErrorResponse wrapperBindingResult(BindingResult bindingResult) {
        StringBuilder msg = new StringBuilder();

        for (ObjectError error : bindingResult.getAllErrors()) {
            msg.append(", ");
            if (error instanceof FieldError) {
                msg.append(((FieldError) error).getField()).append(": ");
            }
            msg.append(error.getDefaultMessage() == null ? "" : error.getDefaultMessage());

        }

        //return new ErrorResponse(ArgumentResponseEnum.VALID_ERROR.getCode(), msg.substring(2));
        return null;
    }

    /**
     * 未定义异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = Exception.class)
    //@ResponseBody
    public ErrorResponse handleException(Exception e) {
        log.error(e.getMessage(), e);

        if (ENV_PROD.equals(profile)) {
            // 当为生产环境, 不适合把具体的异常信息展示给用户, 比如数据库异常信息.
            int code = CommonResponseEnum.SERVER_ERROR.getCode();
            BaseException baseException = new BaseException(CommonResponseEnum.SERVER_ERROR);
            //String message = getMessage(baseException);
            String message = baseException.getMessage();
            return new ErrorResponse(code, message);
        }

        return new ErrorResponse(CommonResponseEnum.SERVER_ERROR.getCode(), e.getMessage());
    }

}
