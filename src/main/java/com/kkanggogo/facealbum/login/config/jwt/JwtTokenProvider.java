package com.kkanggogo.facealbum.login.config.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.kkanggogo.facealbum.login.config.auth.PrincipalDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private JwtProperties jwtProperties = new JwtProperties();
    private String secretKey = jwtProperties.secret;
    private int tokenExpirationTime = jwtProperties.expireAccessTokenTime;

    @PostConstruct
    protected void init(){
        //secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    //HMAC512
    //token을 만들 때 id와 username포함
    public String createToken(PrincipalDetails principalDetails){
        String jwtAccessToken = JWT.create()
                .withSubject("token") //아무거나 써도 됨.
                .withExpiresAt(new Date(System.currentTimeMillis()+(tokenExpirationTime)))
                .withClaim("id", principalDetails.getUser().getId()) // 발행 유저정보 저장
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(jwtProperties.secret)); //고윳값

        return jwtAccessToken;
    }

    public boolean validateAccessToken(String token){
        try{
            Jws<Claims> claims =Jwts.parser().setSigningKey(jwtProperties.secret).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
