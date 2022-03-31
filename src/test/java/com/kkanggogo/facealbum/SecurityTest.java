package com.kkanggogo.facealbum;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkanggogo.facealbum.login.config.jwt.JwtProperties;
import com.kkanggogo.facealbum.login.dto.LoginRequestDto;
import com.kkanggogo.facealbum.login.dto.ResponseDto;
import com.kkanggogo.facealbum.login.model.User;
import com.kkanggogo.facealbum.login.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
}
