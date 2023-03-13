package com.life.muna.common.error.exception;

import com.life.muna.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class InputFieldException extends BusinessException {

    private final String field;

    public InputFieldException(ErrorCode errorCode, String field) {
        super(errorCode);
        this.field = field;
    }

}
