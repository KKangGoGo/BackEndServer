package com.kkanggogo.facealbum.login.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.kkanggogo.facealbum.login.config.auth.PrincipalDetails;
import com.kkanggogo.facealbum.login.model.User;
import com.kkanggogo.facealbum.login.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//BasicAuthenticationFilter라는 것이 있다.
//만약 권한, 인증이 필요한 특정 주소를 요청 했을 때 무조건 위 필터를 타게 되어 있다.
//권한, 인증이 필요 없으면 타지 않는다.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;

    }

    //인증 or 권한이 필요한 주소 요청이 있을 때 해당 필터를 타게 됨.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //super.doFilterInternal(request, response, chain);
        //System.out.printf("권한 or 인증 필요 주소 요청됨");
        JwtProperties jwtProperties = new JwtProperties();

        //헤더에 저장된 토큰 검증
        String jwtHeader = request.getHeader(jwtProperties.headerString);

        //postman에서 header를 통해 전달받은 jwt토큰을 검증해서 정상적인 사용자인지 확인
        if ((jwtHeader == null) || (!jwtHeader.startsWith(jwtProperties.tokenPrefix))) {
            chain.doFilter(request, response);
            return;
        }

        String jwtToken = request.getHeader(jwtProperties.headerString).replace(jwtProperties.tokenPrefix, "");

        // 토큰 검증 (이게 인증이기 때문에 AuthenticationManager도 필요 없음)
        // 내가 SecurityContext에 직접접근해서 세션을 만들때 자동으로 UserDetailsService에 있는 loadByUsername이 호출됨.
        String username = JWT.require(Algorithm.HMAC512(jwtProperties.secret)).build().verify(jwtToken).
                getClaim("username").asString();

        if (username != null) {
            User user = userRepository.searchUsername(username);

            //username가 null이 아니기 때문에 정상 유저임
            //jwt토큰 서명을 통해 만든 Authentication의 객체
            PrincipalDetails principalDetails = new PrincipalDetails(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails,
                    null, principalDetails.getAuthorities());

            //강제로 security 세션에 접근하여 Authentication 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }
        chain.doFilter(request, response);
    }
}
