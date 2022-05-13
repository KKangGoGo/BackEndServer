package com.kkanggogo.facealbum.error;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private Integer stateCode; // httpStateCode
    private String errorCode;  // 세분화를 위한 에러 코드
    private String message; // 예외 메시지

    public ErrorResponse(ErrorStatues errorStatues,ErrorCode errorCode) {
        this.stateCode = errorStatues.getHttpStateCode();
        this.errorCode = errorCode.getErrorCode();
        this.message = errorCode.getMessage();
    }
}
