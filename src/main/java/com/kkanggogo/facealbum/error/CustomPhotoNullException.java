package com.kkanggogo.facealbum.error;

import org.springframework.validation.Errors;

public class CustomPhotoNullException extends CustomException{
    private static final long serialVersionUID = -2116671122895194101L;

    private final Errors errors;

    public CustomPhotoNullException(Errors errors){
        super(ErrorCode.PHOTO_NULL);
        this.errors = errors;
    }

    public Errors getErrors(){
        return this.errors;
    }

}
