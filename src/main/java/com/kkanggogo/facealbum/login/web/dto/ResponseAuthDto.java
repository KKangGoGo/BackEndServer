package com.kkanggogo.facealbum.login.web.dto;

import com.kkanggogo.facealbum.login.domain.RoleType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseAuthDto {
    public String username;

    public String password;

    public String email;

    public RoleType role;

    public String photo;

    public String reAccessToken;

    public String reRefreshToken;
}
