package com.kkanggogo.facealbum.login.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateUserInfoDto {

    @NotBlank
    private String password;
}
