package com.kkanggogo.facealbum.login.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.kkanggogo.facealbum.login.config.auth.PrincipalDetails;
import com.kkanggogo.facealbum.login.model.User;
import com.kkanggogo.facealbum.login.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final JwtProperties properties;
    private final UserRepository userRepository;

    private String secretKey, headerString, tokenPrefix;
    private int tokenExpirationTime;

    @PostConstruct
    protected void init() {
        secretKey = properties.secret;
        headerString = properties.headerString;
        tokenExpirationTime = properties.expireAccessTokenTime;
        tokenPrefix = properties.tokenPrefix;
        //secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    //HMAC512
    //token을 만들 때 id와 username포함
    public String createToken(PrincipalDetails principalDetails) {
        String jwtAccessToken = JWT.create()
                .withSubject("token") //아무거나 써도 됨.
                .withExpiresAt(new Date(System.currentTimeMillis() + (tokenExpirationTime)))
                .withClaim("id", principalDetails.getUser().getId()) // 발행 유저정보 저장
                .withClaim("username", principalDetails.getUser().getUsername())
                .withClaim("role", principalDetails.getUser().getRole().toString())
                .sign(Algorithm.HMAC512(secretKey)); //고윳값

        return jwtAccessToken;
    }

    public String resolveJwtToken(HttpServletRequest request) {
        return request.getHeader(headerString);
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

    public String getUsername(String token) {
        token = token.replace(tokenPrefix, "");
        return JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token).
                getClaim("username").asString();
    }

    public Jws<Claims> getClaimsFromJwtToken(String jwtToken) throws JwtException {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
    }

    public boolean isTokenValid(String token) {
        token = token.replace(tokenPrefix, "");

        String username = getUsername(token);
        if(username != null) return true;

        return false;
    }

}
