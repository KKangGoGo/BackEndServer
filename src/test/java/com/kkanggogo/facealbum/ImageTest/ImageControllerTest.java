package com.kkanggogo.facealbum.ImageTest;

import com.kkanggogo.facealbum.album.service.ImageService;
import com.kkanggogo.facealbum.album.web.ImageController;
import com.kkanggogo.facealbum.album.web.dto.ImageMultipartFileRequestDto;
import com.kkanggogo.facealbum.album.web.dto.ImageRequestDto;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class ImageControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private ImageController imageController;

    @Mock
    private ImageService imageService;


    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
    }


    @Test
    public void multipartFileImageUploadTest() throws Exception {
        //given
        String fileName="balda";
        String contentType="jpg";
        String path="./MockFile";

        MockMultipartFile mockMultipartFile = getMockMultipartFile(fileName,contentType,path);
        //when
        mockMvc.perform(MockMvcRequestBuilders.multipart("/test")
                .file(mockMultipartFile))
                //then
                .andExpect(status().is(202));

    }

    @Test
    public void multipartFileImageListUploadTest() throws Exception {
        //given
        String fileName="balda";
        String contentType="jpg";
        String path="./MockFile";

        MockMultipartFile mockMultipartFile = getMockMultipartFile(fileName,contentType,path);
        MockMultipartFile mockMultipartFile2 = getMockMultipartFile(fileName,contentType,path);
        //when
        mockMvc.perform(MockMvcRequestBuilders.multipart("/test")
                .file(mockMultipartFile)
                .file(mockMultipartFile2))
                //then
                .andExpect(status().is(202));

    }

    private MockMultipartFile getMockMultipartFile(String fileName,String contentType, String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path+"/"+fileName+"."+contentType));
        return new MockMultipartFile("images", fileName + "." + contentType, contentType, fileInputStream);
    }
}
