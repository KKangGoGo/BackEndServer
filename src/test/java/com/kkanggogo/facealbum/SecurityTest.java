package com.kkanggogo.facealbum;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkanggogo.facealbum.login.config.jwt.JwtProperties;
import com.kkanggogo.facealbum.login.dto.LoginRequestDto;
import com.kkanggogo.facealbum.login.dto.RequestUpdateUserInfoDto;
import com.kkanggogo.facealbum.login.dto.ResponseDto;
import com.kkanggogo.facealbum.login.model.RoleType;
import com.kkanggogo.facealbum.login.model.User;
import com.kkanggogo.facealbum.login.repository.UserRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

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

    private MockMvc mvc;

    private User user;

    private String objectToJsonBody, expected;

    private ObjectMapper mapper;

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
                .photo("s3_url")
                .build();

        mapper = new ObjectMapper();

        // {"status":200,"data":1}
        expected = mapper.writeValueAsString(new ResponseDto<Integer>(HttpStatus.OK.value(), 1));
    }

    public MvcResult executeSecurity(String url, String body) throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post(url)
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andReturn();
        return mvcResult;
    }

    public MvcResult oneUserSignUp(User user) throws Exception {
        userRepository.deleteAll();
        assertThat(userRepository.getCount(), is(0));

        // Object -> Json
        objectToJsonBody = mapper.writeValueAsString(user);
        MvcResult mvcResult = executeSecurity("/api/signup", objectToJsonBody);
        assertThat(userRepository.getCount(), is(1));

        return mvcResult;
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void signUpTest() throws Exception {

        MvcResult mvcResult = oneUserSignUp(user);

        // 값 검증
        String result = mvcResult.getResponse().getContentAsString();
        assertThat(result, is(expected));
    }

    @Test
    @DisplayName("로그인 테스트")
    public void loginTest() throws Exception {
        // 회원 가입
        oneUserSignUp(this.user);

        // 로그인 유저
        LoginRequestDto loginUser = LoginRequestDto
                .builder()
                .username(user.getUsername())
                .password((user.getPassword()))
                .build();
        objectToJsonBody = mapper.writeValueAsString(loginUser);

        // 회원 가입한 유저와 로그인 한 유저 정보 비교
        MvcResult mvcResult = executeSecurity("/login", objectToJsonBody);

        // 값 검증
        JwtProperties jwtProperties = new JwtProperties();
        boolean isGetToken = mvcResult
                .getResponse()
                .getHeader(jwtProperties.headerString)
                .startsWith("Bearer"); // 응답 값에 Bearer로 시작하는 문자열이 있는지 확인
        assertThat(isGetToken, is(true));
    }

    @Test
    @DisplayName("회원 비밀번호 수정 테스트")
    //@WithMockUser(username = "ksb")
    public void updateUserTest() throws Exception {
        // 회원 가입 후 로그인을 해 토큰 받아와 사용
        oneUserSignUp(this.user);

        LoginRequestDto loginUser = LoginRequestDto
                .builder()
                .username(user.getUsername())
                .password((user.getPassword()))
                .build();
        objectToJsonBody = mapper.writeValueAsString(loginUser);

        // 회원 가입한 유저와 로그인 한 유저 정보 비교
        MvcResult mvcResult = executeSecurity("/login", objectToJsonBody);

        // 응답에서 토큰을 가져옴
        String accessToken = (String) mvcResult.getResponse().getHeaderValue("access_token");

        // 회원 비밀번호 수정 요청
        // 현재 DTO에 password만 받고 있음
        RequestUpdateUserInfoDto requestUpdateUserInfoDto = RequestUpdateUserInfoDto
                .builder()
                .password("12345")
                .build();
        objectToJsonBody = mapper.writeValueAsString(requestUpdateUserInfoDto);

        //회원 정보 수정 실행
        mvcResult = mvc.perform(MockMvcRequestBuilders
                .put("/api/mypage/update")
                .header("access_token", accessToken)
                .contentType(MediaType.APPLICATION_JSON).content(objectToJsonBody))
                .andExpect(status().isOk())
                .andReturn();

        String str = (String) mvcResult.getResponse().getContentAsString();

        /* 예시
        * {
                "responseUser": {
                    "id": 58,
                    "username": "ksb",
                    "password": "$2a$10$V6ldM6/Wvb4h5oV4xxXb0uQ/SxSjRBAxcXAOGvbPc2TfL6ov1fvfm",
                    "email": "qkqk@gm",
                    "role": "USER",
                    "createDate": "2022-04-01T02:54:36.736+00:00",
                    "photo": "s3_url"
                },
                "nullCheck": 1
            }
        * */

        JSONObject jsonObject = new JSONObject(str).getJSONObject("responseUser");
        String id = jsonObject.getString("id");
        String password = jsonObject.getString("password");

        User getUser = userRepository.searchId(Integer.parseInt(id)).orElseThrow(() -> {
            return new IllegalArgumentException("회원찾기 실패");
        });

        // 회원정보 수정 후 저장된 비밀번호와 응답의 비밀번호와 일치하는지 확인
        assertThat(getUser.getPassword(), is(password));
    }
}
