package com.kkanggogo.facealbum.albumTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkanggogo.facealbum.album.domain.Album;
import com.kkanggogo.facealbum.album.service.AlbumService;
import com.kkanggogo.facealbum.album.web.AlbumController;
import com.kkanggogo.facealbum.album.web.dto.AlbumRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class AlbumControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    AlbumController albumController;

    @Mock
    AlbumService albumService;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(albumController).build();
    }


    @Test
    void 제목없는_앨범만들기_성공() throws Exception {
        //given
        Album requestAlbum = new Album();
        requestAlbum.setTitle(LocalDate.now().toString());
        requestAlbum.setId(1L);
        when(albumService.makeAlbum(anyLong())).thenReturn(requestAlbum);

        ObjectMapper objectMapper = new ObjectMapper();
        String responseString = objectMapper.writeValueAsString(requestAlbum.toAlbumResponseDto());

        //when
        mockMvc.perform(post("/album/makeAlbum")
                .contentType(MediaType.APPLICATION_JSON))

                //then
                .andExpect(status().isOk())
                .andExpect(content().string(responseString));
    }

    @Test
    void 제목있는_앨범만들기_성공() throws Exception {
        //given
        final AlbumRequestDto albumRequestDto = new AlbumRequestDto();
        albumRequestDto.setTitle("aaaaa");

        Album requestAlbum = new Album();
        requestAlbum.setTitle("aaaaa");
        requestAlbum.setId(1L);

        when(albumService.makeAlbum(anyLong(), eq("aaaaa"))).thenReturn(requestAlbum);

        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(albumRequestDto);
        String responseString = objectMapper.writeValueAsString(requestAlbum.toAlbumResponseDto());

        //when
        mockMvc.perform(post("/album/makeAlbum")
                .contentType(MediaType.APPLICATION_JSON).content(s))

                //then
                .andExpect(status().isOk())
                .andExpect(content().string(responseString));
    }
}
