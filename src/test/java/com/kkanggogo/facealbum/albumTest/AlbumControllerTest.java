package com.kkanggogo.facealbum.albumTest;

import com.amazonaws.services.s3.AmazonS3Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkanggogo.facealbum.album.domain.Album;
import com.kkanggogo.facealbum.album.service.AlbumService;
import com.kkanggogo.facealbum.album.web.dto.AlbumListEntityResponseDto;
import com.kkanggogo.facealbum.album.web.dto.AlbumListResponseDto;
import com.kkanggogo.facealbum.album.web.dto.AlbumRequestDto;
import com.kkanggogo.facealbum.login.config.auth.PrincipalDetails;
import com.kkanggogo.facealbum.login.config.jwt.JwtProperties;
import com.kkanggogo.facealbum.login.config.jwt.JwtProvider;
import com.kkanggogo.facealbum.login.domain.RoleType;
import com.kkanggogo.facealbum.login.domain.User;
import com.kkanggogo.facealbum.login.domain.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("local")
@Tag("integration")
public class AlbumControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private AlbumService albumService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private JwtProperties jwtProperties;

    @MockBean
    private UserRepository userRepository;

    private MockMvc securityMvc;

    private ObjectMapper objectMapper;

    private User user;

    private Album requestAlbum;

    private String authorizedUserToken;

    @Autowired
    public AmazonS3Client amazonS3Client;


    @BeforeEach
    public void init() {
        //given
        securityMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        user = User
                .builder()
                .username("ksb1")
                .password("1234")
                .email("ksb@gm")
                .role(RoleType.USER)
                .albumList(new ArrayList<>())
                .build();

        when(userRepository.searchUsername(anyString())).thenReturn(this.user);
        authorizedUserToken = getAuthorizedUserToken(this.user);
        objectMapper = new ObjectMapper();

        requestAlbum = new Album();
        requestAlbum.setId(1L);
    }

    public String getAuthorizedUserToken(User saveUser) {
        PrincipalDetails principalDetails = new PrincipalDetails(saveUser);

        String testToken = jwtProvider.createAccessToken(principalDetails);

        // 사용자를 SecurityContestHolder에 강제 저장
        if (testToken != null && jwtProvider.isTokenValid(testToken)) {
            Authentication authentication = jwtProvider.getAuthentication(testToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        return testToken;
    }

    @Test
    void 제목없는_앨범만들기_성공() throws Exception {

        //given
        requestAlbum.setTitle(LocalDate.now().toString());
        when(albumService.makeAlbum(any(User.class))).thenReturn(requestAlbum);

        String responseString = objectMapper.writeValueAsString(requestAlbum.toAlbumResponseDto());

        //when
        securityMvc.perform(post("/api/user/album/create")
                .header("access_token",authorizedUserToken)
                .contentType(MediaType.APPLICATION_JSON))

                //then
                .andExpect(status().isOk())
                .andExpect(content().string(responseString));
    }

    @Test
    void 제목있는_앨범만들기_성공() throws Exception {

        //given
        requestAlbum.setTitle("aaaaa");

        final AlbumRequestDto albumRequestDto = new AlbumRequestDto();
        albumRequestDto.setTitle("aaaaa");

        String requestBodyString = objectMapper.writeValueAsString(albumRequestDto);
        String responseBodyString = objectMapper.writeValueAsString(requestAlbum.toAlbumResponseDto());

        when(albumService.makeAlbum(any(User.class), eq("aaaaa"))).thenReturn(requestAlbum);

        //when
        securityMvc.perform(post("/api/user/album/create")
                .header("access_token",authorizedUserToken)
                .contentType(MediaType.APPLICATION_JSON).content(requestBodyString))

                //then
                .andExpect(status().isOk())
                .andExpect(content().string(responseBodyString));
    }

    @Test
    void 앨범리스트_받아오기() throws Exception {
        AlbumListEntityResponseDto albumListEntityResponseDto=new AlbumListEntityResponseDto();
        AlbumListResponseDto albumListResponseDto=new AlbumListResponseDto();
        albumListResponseDto.add(albumListEntityResponseDto);

        String responseBodyString = objectMapper.writeValueAsString(albumListResponseDto);

        when(albumService.findUserAlbum(eq(user))).thenReturn(albumListResponseDto);

        //when
        securityMvc.perform(get("/api/user/album-list")
                .header("access_token",authorizedUserToken)
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void 앨범_수정() throws Exception {

        //given
        final AlbumRequestDto albumRequestDto = new AlbumRequestDto();
        albumRequestDto.setTitle("aaaaa");

        String requestBodyString = objectMapper.writeValueAsString(albumRequestDto);

        requestAlbum.setTitle("aaaaa");
        String responseBodyString = objectMapper.writeValueAsString(requestAlbum.toAlbumResponseDto());

        Album album=new Album();
        album.setTitle("aaaaa");
        album.setId(1L);

        when(albumService.updateAlbumInfo(anyLong(),any(User.class),anyString())).thenReturn(album);
        //when
        securityMvc.perform(put("/api/user/album/1").header("access_token",authorizedUserToken)
                .contentType(MediaType.APPLICATION_JSON).content(requestBodyString))

                //then
                .andExpect(status().isOk())
                .andExpect(content().string(responseBodyString));
    }
}
