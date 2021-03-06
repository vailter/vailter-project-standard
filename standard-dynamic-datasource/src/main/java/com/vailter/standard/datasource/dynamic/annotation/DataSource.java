package com.vailter.standard.datasource.dynamic.annotation;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 多数据源注解
 *
 * @since 1.0.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@DS("")
public @interface DataSource {
    @AliasFor(annotation = DS.class)
    String value() default "";
}
