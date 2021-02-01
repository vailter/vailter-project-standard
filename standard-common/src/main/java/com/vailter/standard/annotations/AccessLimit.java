package com.vailter.standard.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {
    /**
     * 秒数限制
     *
     * @return
     */
    int seconds();

    /**
     * 最大访问次数
     *
     * @return
     */
    int maxCount();

    /**
     * 是否需要登录
     *
     * @return
     */
    boolean needLogin() default true;
}