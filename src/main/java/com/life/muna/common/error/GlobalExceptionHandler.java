package com.life.muna.common.error;

import com.life.muna.common.dto.ErrorResponse;
import com.life.muna.common.dto.InputFieldErrorResponse;
import com.life.muna.common.error.exception.BusinessException;
import com.life.muna.common.error.exception.InputFieldException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e, HttpServletRequest request) {
        LOG.error("##### handleBusinessException occurred in class : {}", request.getClass().getName());
        LOG.error("##### handleBusinessException occurred url: {}, trace: {}",e.getMessage(), e.getStackTrace());
        LOG.error("##### handleBusinessException message: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = ErrorResponse.from(errorCode);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InputFieldException.class)
    protected ResponseEntity<InputFieldErrorResponse> handleInputFieldException(InputFieldException e, HttpServletRequest request) {
        LOG.error("##### handleInputFieldException occurred in class : {}", request.getClass().getName());
        LOG.error("##### handleInputFieldException occurred url: {}, trace: {}", request.getRequestURI(), e.getStackTrace());
        LOG.error("##### handleInputFieldException message: {}",e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        InputFieldErrorResponse errorResponse = InputFieldErrorResponse.from(errorCode, e.getField());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request){
        LOG.error("##### handleConstraintViolationException occurred in class: {}", request.getClass().getName());
        LOG.error("##### handleConstraintViolationException occurred url: {}, trace: {}",request.getRequestURI(), e.getStackTrace());
        LOG.error("##### handleConstraintViolationException message: {}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.from(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodValidException(MethodArgumentNotValidException e, HttpServletRequest request){
        LOG.error("##### methodValidException occurred in class: {}", e.getClass().getName());
        LOG.error("##### MethodArgumentNotValidException url: {}, trace: {}",request.getRequestURI(), e.getStackTrace());
        LOG.error("##### MethodArgumentNotValidException message: {}", e.getMessage());
        ErrorResponse errorResponse = makeErrorResponse(e.getBindingResult());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private ErrorResponse makeErrorResponse(BindingResult bindingResult){
        int code = 9998;
        String detail = "";

        if(bindingResult.hasErrors()){
            detail = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
        }
        return ErrorResponse.from(code, detail);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request){
        LOG.error("##### handleException occurred in class: {}", e.getClass().getName());
        LOG.error("##### handleException url: {}, trace: {}",request.getRequestURI(),  Arrays.toString(e.getStackTrace()));
        LOG.error("##### handleException message: {}",e.getMessage());
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse = ErrorResponse.from(errorCode.getErrorCode(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
