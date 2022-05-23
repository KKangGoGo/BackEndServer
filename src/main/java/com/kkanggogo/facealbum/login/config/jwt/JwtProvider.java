package com.kkanggogo.facealbum.login.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.kkanggogo.facealbum.login.config.auth.PrincipalDetails;
import com.kkanggogo.facealbum.login.domain.User;
import com.kkanggogo.facealbum.login.domain.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {
    private final JwtProperties properties;
    private final UserRepository userRepository;

    private String secretKey, accessTokenHeader, refreshTokenHeader, tokenPrefix;
    private int accessTokenExpirationTime, refreshTokenExpirationTime;

    @PostConstruct
    protected void init() {
        secretKey = properties.secret;
        accessTokenHeader = properties.accessTokenHeader;
        refreshTokenHeader = properties.refreshTokenHeader;
        accessTokenExpirationTime = properties.expireAccessTokenTime;
        refreshTokenExpirationTime = properties.expireRefreshTokenTime;
        tokenPrefix = properties.tokenPrefix;
        //secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    //HMAC512
    //token을 만들 때 id와 username포함
    public String createAccessToken(PrincipalDetails principalDetails) {
        String jwtAccessToken = JWT.create()
                .withSubject("token") //아무거나 써도 됨.
                .withExpiresAt(new Date(System.currentTimeMillis() + (accessTokenExpirationTime)))
                .withClaim("id", principalDetails.getUser().getId()) // 발행 유저정보 저장
                .withClaim("username", principalDetails.getUser().getUsername())
                .withClaim("role", principalDetails.getUser().getRole().toString())
                .sign(Algorithm.HMAC512(secretKey)); //고윳값

        return jwtAccessToken;
    }

    public String createRefreshToken(PrincipalDetails principalDetails) {
        String jwtAccessToken = JWT.create()
                .withSubject("token") //아무거나 써도 됨.
                .withExpiresAt(new Date(System.currentTimeMillis() + (refreshTokenExpirationTime)))
                .withClaim("id", principalDetails.getUser().getId()) // 발행 유저정보 저장
                .withClaim("username", principalDetails.getUser().getUsername())
                .withClaim("role", principalDetails.getUser().getRole().toString())
                .sign(Algorithm.HMAC512(secretKey)); //고윳값

        return jwtAccessToken;
    }

    public Optional<String> resolveAccessToken(HttpServletRequest request) {
        log.info("access_token:{}",request.getHeader("access_token"));
        log.info("accessTokenHeader:{}",request.getHeader(accessTokenHeader));
        return Optional.ofNullable(request.getHeader(accessTokenHeader));
    }

    public Optional<String> resolveRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshTokenHeader));
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);

        if (username != null) {
            User user = userRepository.searchUsername(username);

            //username가 null이 아니기 때문에 정상 유저임
            //jwt토큰 서명을 통해 만든 Authentication의 객체
            PrincipalDetails principalDetails = new PrincipalDetails(user);

            return new UsernamePasswordAuthenticationToken(principalDetails,
                    null, principalDetails.getAuthorities());
        }
        return null;
    }

    public PrincipalDetails getPrincipalDetails(String token) {
        String username = getUsername(token);
        if (username != null) {
            User user = userRepository.searchUsername(username);
            return new PrincipalDetails(user);
        }
        return null;
    }

    public String getUsername(String token) {
        token = token.replace(tokenPrefix, "");
        try {
            return JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token).
                    getClaim("username").asString();
        }catch (TokenExpiredException e){
            return null;
        }
    }

    public Jws<Claims> getClaimsFromJwtToken(String jwtToken) throws JwtException {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
    }

    public boolean isTokenValid(String token) {
        token = token.replace(tokenPrefix, "");
        String username = getUsername(token);
        if (username != null) return true;
        return false;
    }

}
