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
        ErrorResponse response = getErrorResponse(e, HttpStatus.BAD_REQUEST);
        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ErrorResponse authenticationException(BadCredentialsException e){
        log.error(e.toString());
        ErrorResponse response = getErrorResponse(e, HttpStatus.UNAUTHORIZED);
        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ErrorResponse userDuplicatedException(CustomUserDuplicatedException e){
        log.error(e.toString());
        ErrorResponse response = getErrorResponse(e, HttpStatus.CONFLICT);
        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponse handleException(Exception e){
        log.error(e.toString());
        ErrorResponse response = getErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
        return response;
    }

    private ErrorResponse getErrorResponse(Exception e, HttpStatus httpStatus) {
        return ErrorResponse
                .builder()
                .code(httpStatus.value())
                .message(e.getMessage())
                .build();
    }
}
