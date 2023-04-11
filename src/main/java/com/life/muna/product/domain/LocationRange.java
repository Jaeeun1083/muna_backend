package com.life.muna.product.domain;

import com.life.muna.common.error.exception.BusinessException;
import lombok.Getter;

import java.util.Arrays;

import static com.life.muna.common.error.ErrorCode.INVALID_LOCATION_RANGE_CODE;

@Getter
public enum LocationRange {
    L000("전체", "L000"),
    L001("시", "L001"),
    L002("구", "L002"),
    L003("동", "L003"),
    ;

    private final String data;
    private final String code;
    LocationRange(String data, String code) {
        this.data = data;
        this.code = code;
    }

    public static LocationRange fromCode(String code) {
        return Arrays.stream(values())
                .filter(g -> g.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new BusinessException(INVALID_LOCATION_RANGE_CODE));
    }

}
