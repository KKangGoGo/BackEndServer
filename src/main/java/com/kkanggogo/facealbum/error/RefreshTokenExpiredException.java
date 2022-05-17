package com.kkanggogo.facealbum.error;

import com.auth0.jwt.exceptions.TokenExpiredException;

public class RefreshTokenExpiredException extends TokenExpiredException {
    public RefreshTokenExpiredException(String message) {
        super(message);
    }
}
