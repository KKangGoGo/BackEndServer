package com.kkanggogo.facealbum.login.web.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T> {
    int status;
    T data;
}
