package com.kkanggogo.facealbum;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkanggogo.facealbum.album.web.dto.AlbumRequestDto;
import com.kkanggogo.facealbum.album.web.dto.AlbumResponseDto;
import com.kkanggogo.facealbum.album.domein.repository.AlbumImageMapRepository;
import com.kkanggogo.facealbum.album.domein.repository.AlbumRepository;
import com.kkanggogo.facealbum.album.domein.repository.ImageRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AlbumTest {
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    AlbumImageMapRepository albumImageMapRepository;

    @Autowired
    private MockMvc mvc;


    @Test
    public void albumSaveTest() throws Exception {
        Long userId=1L;
        ObjectMapper objectMapper=new ObjectMapper();
        AlbumRequestDto albumRequestDto=new AlbumRequestDto();
        albumRequestDto.setTitle("aaaaaaaa");

        AlbumResponseDto albumResponseDto=new AlbumResponseDto();
        albumResponseDto.setAlbumTitle("aaaaaaaa");
        albumResponseDto.setAlbumId(1L);
        String s = objectMapper.writeValueAsString(albumRequestDto);
        String s1 = objectMapper.writeValueAsString(albumResponseDto);
        mvc.perform(post("/album/makeAlbum")
                .contentType(MediaType.APPLICATION_JSON).content(s))
                .andExpect(status().isOk())
                .andExpect(content().string(s1));
    }

}
