package com.kkanggogo.facealbum.login.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkanggogo.facealbum.login.config.auth.PrincipalDetails;
import com.kkanggogo.facealbum.login.dto.RequestLoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//security에서 UsernamePasswordAuthenticationFilter가 있어서
//login 요청시 해당 필터가 동작을 하여 로그인이 진행된다.
//하지만 formLogin().disable()로 인해 form 로그인이 안된다.
//즉, 토근을 사용하기 위해서는 formLogin().disable()이 필요로한데
//form을 통한 로그인이 안된다는 모순 발생
//이를 해결하기 위해 SecurityConfig에다가
// addFilter(new JwtAuthentication(authenticationManager()))을 추가해주면 됨
//그러면 필터가 하나 더 추가하여 로그인시 진행되는 attemptAuthentication함수를 강제 실행시키면된다.

//1. uesrname과 password를 받아서
//2. 정상적인 로그인 시도. authenticationManager로 로그인을 시행하면
//principaldetailsservice가 호출되고 loadUserByUsername 메소드가 작동
//3.principaldetails를 세션에 담고(권한 관리를 위해, 세션에 담지 않으면 권한에 의한 접근 통제가 되지 않는다.)
//4.jwt토큰을 만들어서 응답을 해 주면 됨.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtTokenProvider;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        System.out.printf("JwtAuthentication: 로그인 실행됨 ");

        // json의 데이터를 mapping시켜 서버에서 사용할 수 있도록 만든다.
        ObjectMapper objectMapper = new ObjectMapper();
        RequestLoginDto loginRequestDto = null;
        try {
            loginRequestDto = objectMapper.readValue(request.getInputStream(), RequestLoginDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());

        //principaldetailsservice의 loadUserByUsername 실행하여 로그인 시도
        Authentication authentication =
                authenticationManager.authenticate(authenticationToken);
        // authenticate() 함수가 호출 되면 인증 프로바이더가 유저 디테일 서비스의
        // loadUserByUsername(토큰의 첫번째 파라메터) 를 호출하고
        // UserDetails를 리턴(getPrincipal())받아서 토큰의 두번째 파라메터(credential)과
        // UserDetails(DB값)의 getPassword()함수로 비교해서 동일하면
        // Authentication 객체를 만들어서 필터체인으로 리턴해준다.

        // Tip: 인증 프로바이더의 디폴트 서비스는 UserDetailsService 타입
        // Tip: 인증 프로바이더의 디폴트 암호화 방식은 BCryptPasswordEncoder
        // 결론은 인증 프로바이더에게 알려줄 필요가 없음.
        // principalDetailis에 정보가 있다는 것은 정상적으로 로그인 되었다느 뜻.
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        // authentication를 세션에 저장(권한관리를 위해) 하기 위해 return한다.
        return authentication;
    }

    //attemptAuthentication이후 실행
    //여기서 jwt토근 생성
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
                                            throws IOException, ServletException {

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        JwtProperties jwtProperties = new JwtProperties();

        String jwtToken = jwtTokenProvider.createToken(principalDetails);

        System.out.println("로그인 성공 : "+jwtToken);
        //header에서 값 포함
        response.addHeader(jwtProperties.headerString, jwtProperties.tokenPrefix+jwtToken);
    }

}
