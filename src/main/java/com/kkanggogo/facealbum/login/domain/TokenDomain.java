package com.kkanggogo.facealbum.login.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@RedisHash
@Getter
@AllArgsConstructor
@Builder
public class TokenDomain {
    @Id
    private Long id;

    @Indexed
    private String username;

    private String accessToken;

    private String refreshToken;

    public TokenDomain(){}

    public TokenDomain saveTokenToRedis(String username, String accessToken, String refreshToken){
        return TokenDomain.builder()
                .username(username)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
