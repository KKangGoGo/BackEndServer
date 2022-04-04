package com.kkanggogo.facealbum.login.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestLoginDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
