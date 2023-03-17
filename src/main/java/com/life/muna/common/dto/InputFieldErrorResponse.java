package com.life.muna.common.dto;

import com.life.muna.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class InputFieldErrorResponse extends ErrorResponse {

    private final String data;

    protected InputFieldErrorResponse(ErrorCode errorCode, String data) {
        super(errorCode);
        this.data = data;
    }

    public static InputFieldErrorResponse from(ErrorCode errorCode, String data) {
        return new InputFieldErrorResponse(errorCode, data);
    }

}
