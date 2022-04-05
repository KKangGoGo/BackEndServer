package com.kkanggogo.facealbum.error;

import org.springframework.validation.Errors;

public class CustomMethodArgumentNotValidException extends CustomException{
    private static final long serialVersionUID = -2116671122895194101L;

    private final Errors errors;

    public CustomMethodArgumentNotValidException(Errors errors){
        super(ErrorCode.INVALID_PARAMETER);
        this.errors = errors;
    }

    public Errors getErrors(){
        return this.errors;
    }

}
