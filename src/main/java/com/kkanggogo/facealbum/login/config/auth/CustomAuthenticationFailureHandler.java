package com.kkanggogo.facealbum.login.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkanggogo.facealbum.error.ErrorCode;
import com.kkanggogo.facealbum.error.ErrorResponse;
import com.kkanggogo.facealbum.error.ErrorStatues;
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
            response.setCharacterEncoding("utf-8");
            ErrorResponse build =new ErrorResponse(ErrorStatues.findByErrorCode(ErrorCode.E4011),ErrorCode.E4011);
            String s = objectMapper.writeValueAsString(build);
            response.setContentType("application/json");
            response.setStatus(UNAUTHORIZED.value());
            response.getWriter().write(s);
            response.getWriter().flush();
            return;
        }
    }

}
