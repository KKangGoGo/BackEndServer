package com.kkanggogo.facealbum.error;

public class CustomUserDuplicatedException extends RuntimeException {

    public CustomUserDuplicatedException(String message) {
        super(message);
    }
}
