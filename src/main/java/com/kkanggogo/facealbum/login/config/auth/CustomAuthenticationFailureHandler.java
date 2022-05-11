package com.kkanggogo.facealbum.login.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private static final HttpStatus UNAUTHORIZED=HttpStatus.UNAUTHORIZED;
    private static ObjectMapper objectMapper=new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        if (exception instanceof BadCredentialsException) {
            Map<String,String> responseMap=new HashMap<>();
            response.setCharacterEncoding("utf-8");
            responseMap.put("code", String.valueOf(UNAUTHORIZED.value()));
            responseMap.put("error",UNAUTHORIZED.name());
            responseMap.put("message", "아이디 또는 비밀번호가 틀렸습니다.");
            String s = objectMapper.writeValueAsString(responseMap);
            response.setContentType("application/json");
            response.setStatus(UNAUTHORIZED.value());
            response.getWriter().write(s);
            response.getWriter().flush();
            return;
        }
    }

}
