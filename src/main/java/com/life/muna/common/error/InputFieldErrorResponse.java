package com.life.muna.common.error;

import lombok.Getter;

@Getter
public class InputFieldErrorResponse extends ErrorResponse {

    private final String field;

    protected InputFieldErrorResponse(ErrorCode errorCode, String field) {
        super(errorCode);
        this.field = field;
    }

    public static InputFieldErrorResponse from(ErrorCode errorCode, String field) {
        return new InputFieldErrorResponse(errorCode, field);
    }

}
