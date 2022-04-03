package com.kkanggogo.facealbum.login.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.kkanggogo.facealbum.login.config.auth.PrincipalDetailService;
import com.kkanggogo.facealbum.login.config.auth.PrincipalDetails;
import com.kkanggogo.facealbum.login.model.User;
import com.kkanggogo.facealbum.login.repository.UserRepository;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtFilter extends OncePerRequestFilter {
    private JwtProvider provider;

    public JwtFilter(JwtProvider provider) {
        this.provider = provider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = provider.resolveJwtToken(request);
            if (token != null && provider.isTokenValid(token)) {
                Authentication authentication = provider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (TokenExpiredException e){
            System.out.println("ex : 만료된 토큰");
            request.setAttribute("exception", HttpStatus.UNAUTHORIZED);
        } catch (MalformedJwtException e){
            System.out.println("ex : 이상한 토큰");
            request.setAttribute("exception", HttpStatus.UNAUTHORIZED);
        } catch (JWTDecodeException e){
            System.out.println("ex : 디코딩할 수 없는 토큰");
            request.setAttribute("exception", HttpStatus.UNAUTHORIZED);
        } catch (NullPointerException e){
            System.out.println("ex : 사용자를 찾을 수 없음");
            request.setAttribute("exception", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        filterChain.doFilter(request, response);
    }
}
