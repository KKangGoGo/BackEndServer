package com.kkanggogo.facealbum.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;


// https://bamdule.tistory.com/92
@ControllerAdvice("com.kkanggogo.facealbum")
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        CustomMethodArgumentNotValidException ce
                = new CustomMethodArgumentNotValidException(e);

        ErrorCode errorCode = ce.getErrorCode();

        ErrorResponse response
                = ErrorResponse
                .create()
                .status(errorCode.getStatus())
                .message(ce.toString())
                .errors(ce.getErrors());

        return new ResponseEntity<>(response, HttpStatus.resolve(HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        final ErrorResponse response
                = ErrorResponse
                .create()
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .message(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(CustomExpectationFailed.class)
    protected ResponseEntity<ErrorResponse> handleCustomExpectationFailed(HttpRequestMethodNotSupportedException e) {
        final ErrorResponse response
                = ErrorResponse
                .create()
                .status(HttpStatus.EXPECTATION_FAILED.value())
                .message(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
    }

    // CustomException을 상속받은 클래스가 예외 발생할 때 동작
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();

        ErrorResponse response
                = ErrorResponse
                .create()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(e.toString());

        return new ResponseEntity<>(response, HttpStatus.resolve(errorCode.getStatus()));
    }

    // 모든 Contoroller 단에서 발생하는 예외 처리
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e){
        ErrorResponse response = ErrorResponse
                .create()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.toString());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
