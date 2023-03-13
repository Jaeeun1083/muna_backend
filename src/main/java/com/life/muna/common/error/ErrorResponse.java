package com.life.muna.common.error;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private final int statusCode;
    private final String message;

    protected ErrorResponse(ErrorCode errorCode) {
        this.statusCode = errorCode.getStatusCode();
        this.message = errorCode.getMessage();
    }

    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }

}
