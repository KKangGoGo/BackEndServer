package com.kkanggogo.facealbum.login.config.jwt;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.kkanggogo.facealbum.error.*;
import com.kkanggogo.facealbum.login.config.auth.PrincipalDetails;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.ExpressionException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;


@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private JwtProvider jwtProvider;

    public JwtAuthorizationFilter(JwtProvider provider) {
        this.jwtProvider = provider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/login")) {
            filterChain.doFilter(request, response);
        } else {
            try {
                Optional<String> optionalAccessToken = jwtProvider.resolveAccessToken(request);
                Optional<String> optionalRefreshToken = jwtProvider.resolveRefreshToken(request);
                optionalAccessToken.orElseThrow(()->new AccessTokenExpiredException("만료된 Access Token 입니다."));
                optionalRefreshToken.orElseThrow(()->new RefreshTokenExpiredException("만료된 Refresh Token 입니다."));
                String accessToken = optionalAccessToken.get();
                String refreshToken = optionalRefreshToken.get();

                if (accessToken != null && refreshToken != null) {
                    boolean accessTokenValid = jwtProvider.isTokenValid(accessToken);
                    boolean refreshTokenValid = jwtProvider.isTokenValid(refreshToken);
                    if (accessTokenValid && refreshTokenValid) {
                        Authentication authentication = jwtProvider.getAuthentication(accessToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else if (!accessTokenValid && refreshTokenValid) {
                        // access token은 만료이고 refresh token은 만료되지 않았을 경우 -> 재발급
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
                        throw new AccessTokenExpiredException("토큰이 만료되어 재 로그인이 필요합니다.");
                    }else if(accessToken == null || refreshToken==null){
                        request.setAttribute("exception", HttpStatus.UNAUTHORIZED);
                    }
                    else {
                        // access token은 만료되지 않았는데, refresh token이 만료되었을 경우(서버에 이상이 생긴것임)
                        throw new RefreshTokenExpiredException("토큰이 만료되어 재 로그인이 필요합니다.");
                    }
                }
            }
            catch (AccessTokenExpiredException e){
                log.error("Access Token 오류");
                ErrorCode e4012 = ErrorCode.E4012;
                request.setAttribute("exception", new ErrorResponse(ErrorStatues.findByErrorCode(e4012),e4012));
            }
            catch (RefreshTokenExpiredException e){
                log.error("Refresh Token 오류");
                ErrorCode e4013 = ErrorCode.E4013;
                request.setAttribute("exception",new ErrorResponse(ErrorStatues.findByErrorCode(e4013),e4013));
            }
            catch (TokenExpiredException e) {
                log.error("ex : 만료된 토큰");
                request.setAttribute("exception", HttpStatus.UNAUTHORIZED);
            } catch (MalformedJwtException e) {
                log.error("ex : 이상한 토큰");
                request.setAttribute("exception", HttpStatus.UNAUTHORIZED);
            } catch (JWTDecodeException e) {
                log.error("ex : 디코딩할 수 없는 토큰");
                request.setAttribute("exception", HttpStatus.UNAUTHORIZED);
            } catch (IllegalArgumentException e) {
                log.error("ex : 인자가 잘못된 토큰");
                request.setAttribute("exception", HttpStatus.UNAUTHORIZED);
            } catch (SignatureVerificationException e) {
                log.error("ex : 잘못만들어진 토큰");
                ErrorCode e4012 = ErrorCode.E4012;
                request.setAttribute("exception", new ErrorResponse(ErrorStatues.findByErrorCode(e4012),e4012));
            } catch (IllegalStateException e) {
                log.error("ex : 코드상에 문제 발생");
                request.setAttribute("exception", HttpStatus.UNAUTHORIZED);
            }
            filterChain.doFilter(request, response);
        }
    }
}