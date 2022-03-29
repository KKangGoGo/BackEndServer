package com.kkanggogo.facealbum;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkanggogo.facealbum.login.dto.ResponseDto;
import com.kkanggogo.facealbum.login.model.RoleType;
import com.kkanggogo.facealbum.login.model.User;
import com.kkanggogo.facealbum.login.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
public class SecurityLoginTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    private MockMvc mvc;

    private User user;

    @BeforeEach
    public void setUp(){
        mvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        user = User.builder()
                .username("ksbksb")
                .password(encoder.encode("1234"))
                .email("ksb@gm")
                .photo("s3_url")
                .build();
    }

    @Test
    public void signUp() throws Exception {
        userRepository.deleteAll();

        ObjectMapper mapper = new ObjectMapper();
        // Object -> Json
        String objectToJson = mapper.writeValueAsString(user);
        //{"status":200,"data":1}
        String expected = mapper.writeValueAsString(new ResponseDto<Integer>(HttpStatus.OK.value(), 1));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON).content(objectToJson))
                .andExpect(content().string(expected))
                .andReturn();

        // 반환 값으로 비교
        //  String result = mvcResult.getResponse().getContentAsString();
        //  System.out.println("qweqwe "+expected);
        //  assertThat(result, is(expected));
    }
}
