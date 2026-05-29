package com.wang.Article;

import com.wang.Vatil.StateValidation;
import jakarta.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target(FIELD)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {StateValidation.class})
public @interface State {
    //提供默认的错误信息
    java.lang.String message() default "states只能是草稿或者已发布";
    //提供默认的分组信息
    java.lang.Class<?>[] groups() default {};
    //提供State注解附加信息
    java.lang.Class<? extends jakarta.validation.Payload>[] payload() default {};
}
