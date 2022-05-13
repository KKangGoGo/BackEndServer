package com.kkanggogo.facealbum.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse clientParamException(IllegalArgumentException e){
        log.error(e.toString());
        ErrorResponse response = getErrorResponse(ErrorCode.E4001);
        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ErrorResponse authenticationException(BadCredentialsException e){
        log.error(e.toString());
        ErrorResponse response = getErrorResponse(ErrorCode.E4011);
        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ErrorResponse userDuplicatedException(CustomUserDuplicatedException e){
        log.error(e.toString());
        ErrorResponse response =getErrorResponse(ErrorCode.E4091);
        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponse handleException(Exception e){
        log.error(e.toString());
        ErrorResponse response = getErrorResponse(ErrorCode.E5001);
        return response;
    }

    private ErrorResponse getErrorResponse(ErrorCode errorCode) {
        return new ErrorResponse(ErrorStatues.findByErrorCode(errorCode),errorCode);
    }
}
