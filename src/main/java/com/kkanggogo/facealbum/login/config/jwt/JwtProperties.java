package com.kkanggogo.facealbum.login.config.jwt;

import org.springframework.stereotype.Component;

@Component
public class JwtProperties {
    public final String secret = "kkanggogo"; //HMAC512 방식을 사용하기 때문에 secret키 필요
    public final int expireAccessTokenTime =  3_600 * 1_000; //토큰 만료 1시간
    public final int expireRefreshTokenTime = 86_400 * 14 * 1_000; //토큰 만료 2주
    public final String tokenPrefix = "Bearer "; //토큰 앞단에 붙는 값
    public final String accessTokenHeader = "access_token"; //header에 저장되는 킷 값
    public final String refreshTokenHeader = "refresh_token";

}
