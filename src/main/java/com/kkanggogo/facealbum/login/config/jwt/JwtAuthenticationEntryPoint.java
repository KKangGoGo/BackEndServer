package com.kkanggogo.facealbum.login.config.jwt;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//401에러
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException{

        String exception = request.getAttribute("exception").toString();

        System.out.println("에러 발생");

        // 401
        if(exception.equals(HttpStatus.UNAUTHORIZED.toString())){
            setResponse(response, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        }

        // 500
        if(exception.equals(HttpStatus.INTERNAL_SERVER_ERROR.toString())){
            setResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "토큰에서 사용자를 찾을 수 없습니다.");
        }
    }
    private void setResponse(HttpServletResponse response, HttpStatus errorCode, String errorMessage) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Error Code", errorCode);
        jsonObject.put("Error Message", errorMessage);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println(jsonObject);
    }

}
