package com.kkanggogo.facealbum.login.dto;

import lombok.*;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    private String username;
    private String password;
}
