package com.kkanggogo.facealbum.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_PARAMETER(400, null, "Invalid Request Data"),
    PHOTO_NULL(500, null, "사진이 등록되지 않은 사용자 입니다."),
    EXPECTATION_FAILED(417, null, "잘못된 입력입니다.");

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
