package com.life.muna.common.error;

import com.life.muna.common.dto.ErrorResponse;
import com.life.muna.common.dto.InputFieldErrorResponse;
import com.life.muna.common.error.exception.BusinessException;
import com.life.muna.common.error.exception.InputFieldException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        LOG.error(errorCode.getMessage());
        ErrorResponse errorResponse = ErrorResponse.from(errorCode);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatusCode()));
    }

    @ExceptionHandler(InputFieldException.class)
    protected ResponseEntity<InputFieldErrorResponse> handleInputFieldException(InputFieldException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        LOG.error(errorCode.getMessage());
        InputFieldErrorResponse errorResponse = InputFieldErrorResponse.from(errorCode, exception.getField());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatusCode()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        LOG.error(exception.getMessage());
        ErrorResponse errorResponse = ErrorResponse.from(errorCode);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
