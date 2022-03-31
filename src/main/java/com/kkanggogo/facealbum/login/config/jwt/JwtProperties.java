package com.kkanggogo.facealbum.login.config.jwt;

public class JwtProperties {
    public String secret = "kkanggogo"; //HMAC512 방식을 사용하기 때문에 secret키 필요
    public int expirationTime = 1800000; //토큰 만료 시간
    public String tokenPrefix = "Bearer "; //토큰 앞단에 붙는 값
    public String headerString = "access_token"; //header에 저장되는 킷 값

}
