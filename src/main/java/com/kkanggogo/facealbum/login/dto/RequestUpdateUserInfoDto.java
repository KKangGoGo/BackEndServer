package com.kkanggogo.facealbum.login.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateUserInfoDto {
    private String nickName;
    private String password;
}
