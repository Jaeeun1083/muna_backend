package com.life.muna.common.dto;

import com.life.muna.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private final int statusCode;
    private final String message;

    protected ErrorResponse(ErrorCode errorCode) {
        this.statusCode = errorCode.getStatusCode();
        this.message = errorCode.getMessage();
    }

    protected ErrorResponse(int StatusCode, String message) {
        this.statusCode = StatusCode;
        this.message = message;
    }

    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }

    public static ErrorResponse from(int StatusCode, String message) {
        return new ErrorResponse(StatusCode, message);
    }

}
