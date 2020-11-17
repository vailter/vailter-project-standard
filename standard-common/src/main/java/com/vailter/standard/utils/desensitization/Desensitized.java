package com.vailter.standard.utils.desensitization;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@JsonSerialize(using = SensitiveInfoSerialize.class)
@JacksonAnnotationsInside
public @interface Desensitized {
    /**
     * 脱敏类型(规则)
      */
    SensitiveTypeEnum type();
}
