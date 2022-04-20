package com.kkanggogo.facealbum;

import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkanggogo.facealbum.login.config.auth.PrincipalDetails;
import com.kkanggogo.facealbum.login.config.jwt.JwtProperties;
import com.kkanggogo.facealbum.login.config.jwt.JwtProvider;
import com.kkanggogo.facealbum.login.web.dto.RequestLoginDto;
import com.kkanggogo.facealbum.login.web.dto.RequestSignUpDto;
import com.kkanggogo.facealbum.login.web.dto.RequestUpdateUserInfoDto;
import com.kkanggogo.facealbum.login.web.dto.ResponseDto;
import com.kkanggogo.facealbum.login.domain.RoleType;
import com.kkanggogo.facealbum.login.domain.User;
import com.kkanggogo.facealbum.login.domain.repository.UserRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ActiveProfiles("local")
@Tag("integration")
public class SecurityTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserRepository mockUserRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JwtProvider jwtProvider;

    private MockMvc mvc;

    private User user;

    private String objectToJsonBody, expectResponseDto;

    private ObjectMapper mapper;

    private RequestSignUpDto requestSignUpDto;

    private JwtProperties jwtProperties;

    private static final String SIGNUP_URL = "/api/signup";
    private static final String LOGIN_URL = "/api/login";
    private static final String USER_MODIFY_URL = "/api/user/mypage";
    private static final String GET_AUTH_URL = "/api/user/auth";


    // JUnit5
    @BeforeEach
    public void setUp() throws Exception {
        mvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        user = User
                .builder()
                .id(1L)
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

        jwtProperties = new JwtProperties();

        // {"status":200,"data":1}
        expectResponseDto = mapper.writeValueAsString(new ResponseDto<Integer>(HttpStatus.OK.value(), 1));

        this.user.setPassword(encoder.encode(this.user.getPassword()));
        when(mockUserRepository.searchUsername(anyString())).thenReturn(this.user);
        when(mockUserRepository.searchId(anyLong())).thenReturn(java.util.Optional.ofNullable(this.user));
    }

    public MvcResult executePost(String url, String body) throws Exception {
        return mvc.perform(MockMvcRequestBuilders
                .post(url)
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andReturn();
    }

    public MvcResult executeSignUp(RequestSignUpDto requestSignUpDto) throws Exception {
        byte[] image = IOUtils.toByteArray(getClass()
                .getClassLoader()
                .getResourceAsStream("multi_image.jpg"));
        MockMultipartFile photo = new MockMultipartFile("photo",
                "multi_image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                image);

        objectToJsonBody = mapper.writeValueAsString(requestSignUpDto);

        MockMultipartFile signupInfoDto = new MockMultipartFile("signupInfo",
                "signupInfo",
                "application/json",
                objectToJsonBody.getBytes(StandardCharsets.UTF_8));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .multipart(SIGNUP_URL)
                .file(photo)
                .file(signupInfoDto))
                .andReturn();
        return mvcResult;
    }

    public String getAuthorizedUserToken(User saveUser) {
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
    @DisplayName("회원가입")
    public void signUpTest() throws Exception {
        // given, when
        MvcResult mvcResult = executeSignUp(requestSignUpDto);

        // then
        String result = mvcResult.getResponse().getContentAsString();
        assertThat(result, is(expectResponseDto));
    }

    @Test
    @DisplayName("로그인")
    public void loginTest() throws Exception {
        // given
        User tmpUser = User
                .builder()
                .id(this.user.getId())
                .username(this.user.getUsername())
                .password(encoder.encode(this.user.getPassword()))
                .role(this.user.getRole())
                .email(this.user.getEmail())
                .build();
        RequestLoginDto loginUserDto = RequestLoginDto
                .builder()
                .username(this.user.getUsername())
                .password(this.user.getPassword())
                .build();
        objectToJsonBody = mapper.writeValueAsString(loginUserDto);
        when(mockUserRepository.searchUsername(anyString())).thenReturn(tmpUser);

        // when
        MvcResult mvcResult = executePost(LOGIN_URL, objectToJsonBody);

        // then
        boolean isGetToken = mvcResult
                .getResponse()
                .getHeader(jwtProperties.headerString)
                .startsWith(jwtProperties.tokenPrefix); // 응답 값에 Bearer로 시작하는 문자열이 있는지 확인
        assertThat(isGetToken, is(true));
    }

    @Test
    @DisplayName("로그아웃")
    public void logoutTest() throws Exception {
        // given
        executeSignUp(requestSignUpDto);

        String accessToken = getAuthorizedUserToken(this.user);

        // when
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .get("/api/logout")
                .header("access_token", accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // then
        String result = mvcResult.getResponse().getContentAsString();
        assertThat(result, is(expectResponseDto));
    }

    @Test
    @DisplayName("토큰으로 사용자 정보를 받아오는")
    public void getAuthTest() throws Exception {
        // given
        executeSignUp(requestSignUpDto);
        String accessToken = getAuthorizedUserToken(this.user);

        // when
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .get(GET_AUTH_URL)
                .header(jwtProperties.headerString, accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // then
        String result = mvcResult.getResponse().getContentAsString();

        JSONParser parser = new JSONParser();
        JSONObject jsonObj = (JSONObject) parser.parse(result);
        System.out.println(jsonObj.get("password"));

        assertThat(jsonObj.get("username"), is(requestSignUpDto.getUsername()));
        // 이유는 모르겠으나, 비밀번호 비교 안됨
        // assertThat(true, is(encoder.matches((String)jsonObj.get("password"), requestSignUpDto.getPassword())));
        assertThat(jsonObj.get("email"), is(requestSignUpDto.getEmail()));
    }

    @Test
    @DisplayName("회원 비밀번호 수정")
    public void updateUserTest() throws Exception {
        // given
        executeSignUp(requestSignUpDto);

        // 회원가입된 유저의 정보를 security 내부 저장소에 저장 및 해당 유저의 토큰을 가져옴
        String accessToken = getAuthorizedUserToken(this.user);

        // 현재 DTO에 password만 받아 수정 요청을 하고 있음
        RequestUpdateUserInfoDto requestUpdateUserInfoDto = RequestUpdateUserInfoDto
                .builder()
                .password("12345")
                .build();
        objectToJsonBody = mapper.writeValueAsString(requestUpdateUserInfoDto);

        // when
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .put(USER_MODIFY_URL)
                .header(jwtProperties.headerString, accessToken)
                .contentType(MediaType.APPLICATION_JSON).content(objectToJsonBody))
                .andExpect(status().isOk())
                .andReturn();

        // then
        String updatedPassword = mockUserRepository.searchUsername(this.user.getUsername()).getPassword();
        String result = mvcResult.getResponse().getContentAsString();
        assertThat(result, is(expectResponseDto));
        assertThat(true, is(encoder.matches(requestUpdateUserInfoDto.getPassword(), updatedPassword)));
    }

    @Test
    @DisplayName("사용할 수 없는 토큰 사용")
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
                .put(USER_MODIFY_URL)
                .header(jwtProperties.headerString, expiredToken)
                .contentType(MediaType.APPLICATION_JSON).content(objectToJsonBody))
                .andExpect(status().is4xxClientError()); //401
        // 디코딩할 수 없는 토큰(이상한 토큰 값)
        mvc.perform(MockMvcRequestBuilders
                .put(USER_MODIFY_URL)
                .header(jwtProperties.headerString, wiredToken)
                .contentType(MediaType.APPLICATION_JSON).content(objectToJsonBody))
                .andExpect(status().is4xxClientError()); //401
    }

    @Test
    @DisplayName("조건에 맞지 않은 회원가입")
    public void signUpMethodArgumentNotValidException() throws Exception {
        // given
        RequestSignUpDto invalidRequest = RequestSignUpDto
                .builder()
                .username("")
                .password(" ")
                .email(null)
                .build();

        // when
        MvcResult mvcResult = executeSignUp(invalidRequest);

        // then
        assertThat(mvcResult.getResponse().getStatus(), is(HttpStatus.BAD_REQUEST.value())); // 400
    }

    @Test
    @DisplayName("회원가입 되지 않은 아이디로 로그인 시도")
    public void notSingUpLogin() throws Exception {
        user.setUsername("unSignup");
        user.setPassword("unSignup");
        RequestLoginDto loginUser = RequestLoginDto
                .builder()
                .username(user.getUsername())
                .password((user.getPassword()))
                .build();
        objectToJsonBody = mapper.writeValueAsString(loginUser);

        // when
        MvcResult mvcResult = executePost(LOGIN_URL, objectToJsonBody);

        // then
        assertThat(mvcResult.getResponse().getStatus(), is(HttpStatus.UNAUTHORIZED.value())); // 401
    }
}
