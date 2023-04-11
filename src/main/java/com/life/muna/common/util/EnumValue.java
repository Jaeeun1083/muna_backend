package com.life.muna.common.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 해당 필드의 값이 주어진 enum 클래스의 값 중 하나인지 검증하는 기능
 * */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValueValidator.class) //해당 어노테이션이 검증 기능을 수행하는 어노테이션임을 나타냄
public @interface EnumValue {
    String message() default "Invalid value for enum type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends Enum<?>> enumClass();

    boolean ignoreCase() default false; //대소문자를 무시할지 여부
}
