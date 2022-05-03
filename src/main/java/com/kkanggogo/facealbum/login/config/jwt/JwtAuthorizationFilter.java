package com.kkanggogo.facealbum.login.config.jwt;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.kkanggogo.facealbum.login.config.auth.PrincipalDetails;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private JwtProvider jwtProvider;

    public JwtAuthorizationFilter(JwtProvider provider) {
        this.jwtProvider = provider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = jwtProvider.resolveAccessToken(request);
            String refreshToken = jwtProvider.resolveRefreshToken(request);

            if (accessToken != null && refreshToken != null) {
                boolean accessTokenValid = jwtProvider.isTokenValid(accessToken);
                boolean refreshTokenValid = jwtProvider.isTokenValid(refreshToken);
                if (accessTokenValid && refreshTokenValid) {
                    Authentication authentication = jwtProvider.getAuthentication(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else if (!accessTokenValid && refreshTokenValid) {
                    // access token은 만료이고 refresh token은 만료되지 않았을 경우
                    PrincipalDetails principalDetails = jwtProvider.getPrincipalDetails(refreshToken);
                    String reAccessToken = jwtProvider.createAccessToken(principalDetails);
                    String reRefreshToken = jwtProvider.createAccessToken(principalDetails);
                    Authentication authentication1 = jwtProvider.getAuthentication(reAccessToken);
                    Authentication authentication2 = jwtProvider.getAuthentication(reRefreshToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication1);
                    SecurityContextHolder.getContext().setAuthentication(authentication2);
                    request.setAttribute("re_access_token", reAccessToken);
                    request.setAttribute("re_refresh_token", reRefreshToken);
                } else if (!accessTokenValid && !refreshTokenValid) {
                    // 토큰 두개 다 만료되었을 경우 -> 재로그인
                    throw new TokenExpiredException("토큰이 만료되어 재 로그인이 필요합니다.");
                } else {
                    // access token은 만료되지 않았는데, refresh token이 만료되었을 경우(서버에 이상이 생긴것임)
                    throw new TokenExpiredException("토큰이 만료되어 재 로그인이 필요합니다.");
                }
            }
        } catch (TokenExpiredException e) {
            System.out.println("ex : 만료된 토큰");
            request.setAttribute("exception", HttpStatus.UNAUTHORIZED);
        } catch (MalformedJwtException e) {
            System.out.println("ex : 이상한 토큰");
            request.setAttribute("exception", HttpStatus.UNAUTHORIZED);
        } catch (JWTDecodeException e) {
            System.out.println("ex : 디코딩할 수 없는 토큰");
            request.setAttribute("exception", HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e) {
            System.out.println("ex : 인자가 잘못된 토큰");
            request.setAttribute("exception", HttpStatus.UNAUTHORIZED);
        } catch (SignatureVerificationException e) {
            System.out.println("ex : 잘못만들어진 토큰");
            request.setAttribute("exception", HttpStatus.UNAUTHORIZED);
        } catch (IllegalStateException e) {
            System.out.println("ex : 코드상에 문제 발생");
            request.setAttribute("exception", HttpStatus.UNAUTHORIZED);
        }
        filterChain.doFilter(request, response);
    }
}