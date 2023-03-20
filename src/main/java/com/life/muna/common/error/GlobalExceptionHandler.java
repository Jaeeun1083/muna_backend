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
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception) {
        LOG.error("handleBusinessException message: {}",exception.getMessage());
        ErrorCode errorCode = exception.getErrorCode();
        ErrorResponse errorResponse = ErrorResponse.from(errorCode);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatusCode()));
    }

    @ExceptionHandler(InputFieldException.class)
    protected ResponseEntity<InputFieldErrorResponse> handleInputFieldException(InputFieldException exception) {
        LOG.error("handleInputFieldException field: {}, message: {}", exception.getField(),exception.getMessage());
        ErrorCode errorCode = exception.getErrorCode();
        InputFieldErrorResponse errorResponse = InputFieldErrorResponse.from(errorCode, exception.getField());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatusCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodValidException(MethodArgumentNotValidException e, HttpServletRequest request){
        LOG.error("MethodArgumentNotValidException url: {}, trace: {}",request.getRequestURI(), e.getStackTrace());
        ErrorResponse errorResponse = makeErrorResponse(e.getBindingResult());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private ErrorResponse makeErrorResponse(BindingResult bindingResult){
        int code = 400;
        String detail = "";

        if(bindingResult.hasErrors()){
            detail = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
        }
        return ErrorResponse.from(code, detail);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception exception) {
        LOG.error(exception.getMessage());
        LOG.error("handleException message: {}",exception.getMessage());
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse = ErrorResponse.from(errorCode.getStatusCode(), exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
