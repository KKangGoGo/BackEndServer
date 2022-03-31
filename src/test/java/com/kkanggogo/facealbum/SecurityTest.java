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

    private MockMvc mvc;

    private User user;

    private String objectToJson, expected;

    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() throws Exception{
        mvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        user = User.builder()
                .username("ksb")
                .password("1234")
                .email("ksb@gm")
                .photo("s3_url")
                .build();

        mapper = new ObjectMapper();

        // {"status":200,"data":1}
        expected = mapper.writeValueAsString(new ResponseDto<Integer>(HttpStatus.OK.value(), 1));
    }

    public MvcResult executeSecurity(String url, String body) throws Exception{
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post(url)
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andReturn();
        return mvcResult;
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void signUp() throws Exception {
        userRepository.deleteAll();
        assertThat(userRepository.getCount(), is(0));

        // Object -> Json
        objectToJson = mapper.writeValueAsString(user);
        MvcResult mvcResult = executeSecurity("/api/signup", objectToJson);
        assertThat(userRepository.getCount(), is(1));

        // 값 검증
        String result = mvcResult.getResponse().getContentAsString();
        assertThat(result, is(expected));
    }

    @Test
    @DisplayName("로그인 테스트")
    public void login() throws Exception {
        userRepository.deleteAll();
        assertThat(userRepository.getCount(), is(0));

        // 회원가입
        objectToJson = mapper.writeValueAsString(user);
        System.out.println("jsonToObject" + objectToJson);
        executeSecurity("/api/signup", objectToJson);
        assertThat(userRepository.getCount(), is(1));

        // 로그인
        LoginRequestDto loginUser = LoginRequestDto.builder()
                .username(user.getUsername())
                .password((user.getPassword()))
                .build();

        objectToJson = mapper.writeValueAsString(loginUser);
        MvcResult mvcResult = executeSecurity("/login", objectToJson);

        // 값 검증
        JwtProperties jwtProperties = new JwtProperties();
        boolean isGetToken = mvcResult
                .getResponse()
                .getHeader(jwtProperties.headerString)
                .startsWith("Bearer"); // 응답 값에 Bearer로 시작하는 문자열이 있는지 확인
        assertThat(isGetToken, is(true));
    }
}
