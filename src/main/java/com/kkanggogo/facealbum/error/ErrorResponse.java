package com.kkanggogo.facealbum.error;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorResponse {

    private Integer code; // 예외를 세분화하기 위한 코드
    private String message; // 예외 메시지


}
