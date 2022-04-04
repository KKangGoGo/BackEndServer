package com.kkanggogo.facealbum;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkanggogo.facealbum.login.config.auth.PrincipalDetails;
import com.kkanggogo.facealbum.login.config.jwt.JwtProperties;
import com.kkanggogo.facealbum.login.config.jwt.JwtProvider;
import com.kkanggogo.facealbum.login.dto.RequestLoginDto;
import com.kkanggogo.facealbum.login.dto.RequestSignUpDto;
import com.kkanggogo.facealbum.login.dto.RequestUpdateUserInfoDto;
import com.kkanggogo.facealbum.login.dto.ResponseDto;
import com.kkanggogo.facealbum.login.model.RoleType;
import com.kkanggogo.facealbum.login.model.User;
import com.kkanggogo.facealbum.login.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.FileInputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SecurityTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JwtProvider jwtProvider;

    private MockMvc mvc;

    private User user;

    private String objectToJsonBody, expectResponseDto;

    private ObjectMapper mapper;

    private RequestSignUpDto requestSignUpDto;

    // JUnit5
    @BeforeEach
    public void setUp() throws Exception {
        mvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        user = User
                .builder()
                .username("ksb")
                .password("1234")
                .email("ksb@gm")
                .role(RoleType.USER)
                .build();

        requestSignUpDto = RequestSignUpDto
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .build();

        mapper = new ObjectMapper();

        // {"status":200,"data":1}
        expectResponseDto = mapper.writeValueAsString(new ResponseDto<Integer>(HttpStatus.OK.value(), 1));
    }

    public MvcResult executeSecurity(String url, String body) throws Exception {
        MockMultipartFile image = new MockMultipartFile("files", "imagefile.jpeg", "image/jpeg", "<<jpeg data>>".getBytes());

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .multipart(url)
                .file(image)
                .param("signupInfo", body))
                .andExpect(status().isOk())
                .andReturn();
        return mvcResult;
    }

    public MvcResult oneUserSignUp(RequestSignUpDto requestSignUpDto) throws Exception {
        userRepository.deleteAll();
        assertThat(userRepository.getCount(), is(0));

        // Object -> Json
        objectToJsonBody = mapper.writeValueAsString(requestSignUpDto);
        MvcResult mvcResult = executeSecurity("/api/signup", objectToJsonBody);
        assertThat(userRepository.getCount(), is(1));

        return mvcResult;
    }

    public String getAuthorizedUserToken(User saveUser){
        PrincipalDetails principalDetails = new PrincipalDetails(saveUser);

        String testToken = jwtProvider.createToken(principalDetails);

        // 사용자를 SecurityContestHolder에 강제 저장
        if (testToken != null && jwtProvider.isTokenValid(testToken)) {
            Authentication authentication = jwtProvider.getAuthentication(testToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        return testToken;
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void signUpTest() throws Exception {

        // given, when
        MvcResult mvcResult = oneUserSignUp(requestSignUpDto);

        // then
        String result = mvcResult.getResponse().getContentAsString();
        assertThat(result, is(expectResponseDto));
    }

    @Test
    @DisplayName("로그인 테스트")
    public void loginTest() throws Exception {
        // given
        oneUserSignUp(requestSignUpDto);

        RequestLoginDto loginUser = RequestLoginDto
                .builder()
                .username(user.getUsername())
                .password((user.getPassword()))
                .build();
        objectToJsonBody = mapper.writeValueAsString(loginUser);

        // when
        MvcResult mvcResult = executeSecurity("/login", objectToJsonBody);

        // then
        JwtProperties jwtProperties = new JwtProperties();
        boolean isGetToken = mvcResult
                .getResponse()
                .getHeader(jwtProperties.headerString)
                .startsWith("Bearer"); // 응답 값에 Bearer로 시작하는 문자열이 있는지 확인
        assertThat(isGetToken, is(true));
    }

    @Test
    @DisplayName("회원 비밀번호 수정 테스트")
    public void updateUserTest() throws Exception {
        // given
        oneUserSignUp(requestSignUpDto);

        // 같은 유저의 정보를 security 내부 저장소에 저장 및 해당 유저의 토큰을 가져옴
        String accessToken = getAuthorizedUserToken(this.user);

        // 현재 DTO에 password만 받아 수정 요청을 하고 있음
        RequestUpdateUserInfoDto requestUpdateUserInfoDto = RequestUpdateUserInfoDto
                .builder()
                .password("12345")
                .build();
        objectToJsonBody = mapper.writeValueAsString(requestUpdateUserInfoDto);

        // when
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .put("/api/user/mypage/update")
                .header("access_token", accessToken)
                .contentType(MediaType.APPLICATION_JSON).content(objectToJsonBody))
                .andExpect(status().isOk())
                .andReturn();

        // then
        String updatedPassword = userRepository.searchUsername(this.user.getUsername()).getPassword();
        String result = mvcResult.getResponse().getContentAsString();
        assertThat(result, is(expectResponseDto));
        assertThat(true, is(encoder.matches(requestUpdateUserInfoDto.getPassword(), updatedPassword)));
    }

    @Test
    @DisplayName("사용할 수 없는 토큰 사용 테스트")
    public void unavailableTokenUseTest() throws Exception {
        // given
        String expiredToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0b2tlbiIsInJvbGUiOiJVU0VSIiwiaWQiOjEwMCwiZXhwIjoxNjQ4OTk0OTE3LCJ1c2VybmFtZSI6ImtzYiJ9.aR0jz48QMnYrxFdarZ2RriloaPOyizCglol0uJY9XtTc4nilt2GkvdOiKaxsD_8gVLMMvFZBjFnUVzoEAOnzNA";
        String wiredToken = "fldnvlkdnfkd";

        RequestUpdateUserInfoDto requestUpdateUserInfoDto = RequestUpdateUserInfoDto
                .builder()
                .password("12345")
                .build();
        objectToJsonBody = mapper.writeValueAsString(requestUpdateUserInfoDto);

        // when, then
        //만료된 토큰
        mvc.perform(MockMvcRequestBuilders
                .put("/api/user/mypage/update")
                .header("access_token", expiredToken)
                .contentType(MediaType.APPLICATION_JSON).content(objectToJsonBody))
                .andExpect(status().is4xxClientError()); //401
        // 디코딩할 수 없는 토큰(이상한 토큰 값)
        mvc.perform(MockMvcRequestBuilders
                .put("/api/user/mypage/update")
                .header("access_token", wiredToken)
                .contentType(MediaType.APPLICATION_JSON).content(objectToJsonBody))
                .andExpect(status().is4xxClientError()); //401
    }
}
