package com.kkanggogo.facealbum.login.web.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestSignUpDto {
    @NotBlank(message = "username은 필수 입니다.")
    String username;

    @NotBlank(message = "password은 필수 입니다.")
    String password;

    @NotBlank(message = "email은 필수 입니다.")
    String email;
}
