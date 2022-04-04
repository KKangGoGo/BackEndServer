package com.kkanggogo.facealbum.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_PARAMETER(400, null, "Invalid Request Data");

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
