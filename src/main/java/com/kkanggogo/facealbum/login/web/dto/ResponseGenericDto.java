package com.kkanggogo.facealbum.login.web.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseGenericDto<T> {
    @NotBlank
    T responseUser;

    @NotBlank
    int nullCheck;
}
