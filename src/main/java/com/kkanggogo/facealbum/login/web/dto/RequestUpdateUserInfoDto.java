package com.kkanggogo.facealbum.login.web.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateUserInfoDto {

    private String password;

    private String email;
}
