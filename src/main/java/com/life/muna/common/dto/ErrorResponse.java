package com.life.muna.common.dto;

import com.life.muna.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse {

    private final int errorCode;
    private final String message;

    protected ErrorResponse(ErrorCode errorCode) {
        this.errorCode = errorCode.getErrorCode();
        this.message = errorCode.getMessage();
    }

    protected ErrorResponse(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }

    public static ErrorResponse from(int errorCode, String message) {
        return new ErrorResponse(errorCode, message);
    }

}
