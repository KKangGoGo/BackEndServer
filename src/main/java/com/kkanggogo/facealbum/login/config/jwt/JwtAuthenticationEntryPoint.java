package com.kkanggogo.facealbum.login.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkanggogo.facealbum.error.ErrorResponse;
import com.kkanggogo.facealbum.error.ErrorStatues;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 401에러
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final ObjectMapper objectMapper=new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        Object exception = request.getAttribute("exception");
        log.debug("JwtAuthenticationEntryPoint send Error");

        setResponse(response,(ErrorResponse) exception);

//        if (exception == null) {
//            setResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "필터에서 예외 처리가 되지 않은 예외가 발생했습니다.");
//            return;
//        } else {
//            exception = exception.toString();
//        }
//
//        // 401
//        if (exception.equals(HttpStatus.UNAUTHORIZED.toString())) {
//            setResponse(response, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
//        }
//
//        // 500
//        if (exception.equals(HttpStatus.INTERNAL_SERVER_ERROR.toString())) {
//            setResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "토큰에서 사용자를 찾을 수 없습니다.");
//        }
//
//        if (exception.equals(HttpStatus.EXPECTATION_FAILED.toString())) {
//            setResponse(response, HttpStatus.EXPECTATION_FAILED, "잘못된 입력입니다.");
//        }
    }

    private void setResponse(HttpServletResponse response, ErrorResponse errorResponse) throws IOException {
        String responseString = objectMapper.writeValueAsString(errorResponse);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorResponse.getStateCode());
        response.getWriter().println(responseString);
    }

}
