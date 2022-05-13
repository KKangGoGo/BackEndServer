package com.kkanggogo.facealbum.error;

import com.auth0.jwt.exceptions.TokenExpiredException;

public class AccessTokenExpiredException extends TokenExpiredException {
    public AccessTokenExpiredException(String message) {
        super(message);
    }
}
