package com.vailter.standard.utils;

import com.vailter.standard.exception.AppException;
import com.vailter.standard.ret.CodeMsg;
import org.hibernate.validator.HibernateValidator;
import org.springframework.util.ClassUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class ValidatorUtils {
    private static final Validator validator;
    private static SmartValidator validatorAdapter;

    static {
        // 快速返回模式
        validator = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                .buildValidatorFactory()
                .getValidator();
    }

    public static Validator getValidator() {
        return validator;
    }

    private static SmartValidator getValidatorAdapter(Validator validator) {
        if (validatorAdapter == null) {
            validatorAdapter = new SpringValidatorAdapter(validator);
        }
        return validatorAdapter;
    }

    /**
     * 校验参数，用于普通参数校验 [未测试！]
     *
     * @param
     */
    public static void validateParams(Object... params) {
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(params);

        if (!constraintViolationSet.isEmpty()) {
            throw new ConstraintViolationException(constraintViolationSet);
        }
    }

    /**
     * 校验对象
     *
     * @param object
     * @param groups
     * @param <T>
     */
    public static <T> void validate(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> constraintViolationSet = validator.validate(object, groups);

        if (!constraintViolationSet.isEmpty()) {
            throw new ConstraintViolationException(constraintViolationSet);
        }
    }

    /**
     * 校验对象
     * 使用与 Spring 集成的校验方式。
     *
     * @param object 待校验对象
     * @param groups 待校验的组
     * @throws BindException
     */
    public static <T> void validateBySpring(T object, Class<?>... groups) {
        DataBinder dataBinder = getBinder(object);
        dataBinder.validate(groups);
        BindingResult bindingResult;
        if ((bindingResult = dataBinder.getBindingResult()).hasErrors()) {
            //throw new BindException(bindingResult);
            System.out.println(bindingResult);

            FieldError fieldError = bindingResult.getFieldError();
            String errorMsg = null == fieldError ? "参数不合法" : fieldError.getDefaultMessage();
            throw new AppException(CodeMsg.BIND_ERROR.fillArgs(errorMsg));
        }
    }

    private static <T> DataBinder getBinder(T object) {
        DataBinder dataBinder = new DataBinder(object, ClassUtils.getShortName(object.getClass()));
        dataBinder.setValidator(getValidatorAdapter(validator));
        return dataBinder;
    }
}
