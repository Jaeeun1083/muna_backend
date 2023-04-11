package com.life.muna.common.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {

    /**
    * 메소드에서 설정된 enum 클래스의 값들을 담고 있으며, 검증 대상인 값이 이 리스트에 포함되어 있는지 검사
    * */
    private List<Object> valueList;

    /**
     * EnumValue 어노테이션에서 선언한 enum 클래스와 대소문자 구분 여부를 설정
     * */
    @Override
    public void initialize(EnumValue constraintAnnotation) {
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClass();
        boolean ignoreCase = constraintAnnotation.ignoreCase();

        this.valueList = Arrays.stream(enumClass.getEnumConstants())
                .map(value -> ignoreCase ? value.toString().toLowerCase() : value.toString())
                .collect(Collectors.toList());
//        for (Object object : valueList) {
//            System.out.println("object: " + object);
//        }
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        /**
        *  검증 대상 값을 문자열로 변환한 뒤 allowedValues 집합에 포함되어 있는지 여부를 검사하여 결과를 반환.
        * */
        return valueList.contains(value.toString());
    }
}
