package com.kkanggogo.facealbum.ImageTest;

import com.amazonaws.AmazonServiceException;
import com.kkanggogo.facealbum.album.AmazonS3Uploader;
import com.kkanggogo.facealbum.album.domain.Album;
import com.kkanggogo.facealbum.album.domain.AlbumImageMappingTable;
import com.kkanggogo.facealbum.album.domain.Image;
import com.kkanggogo.facealbum.album.domain.repository.AlbumImageMapRepository;
import com.kkanggogo.facealbum.album.domain.repository.ImageRepository;
import com.kkanggogo.facealbum.album.service.ImageService;
import com.kkanggogo.facealbum.album.ImageMultipartFileRequestDtoFactory;
import com.kkanggogo.facealbum.album.web.dto.ImageMultipartFileRequestDto;
import com.kkanggogo.facealbum.login.domain.RoleType;
import com.kkanggogo.facealbum.login.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @InjectMocks
    private ImageService imageService;

    @Mock
    private AmazonS3Uploader userAlbumAmazonS3Uploader;

    @Mock
    private ImageRepository imageRepository;
    @Mock
    private AlbumImageMapRepository albumImageMapRepository;

    private ImageMultipartFileRequestDto imageMultipartFileRequestDto;

    private  User user;

    private Album responseAlbum;

    @Test
    public void 이미지_업로드_성공_테스트() throws IOException {

        //given
        when(imageRepository.saveAll(anyList())).thenAnswer((Answer<List<Image>>) invocation -> {
            Image image = new Image();
            return Arrays.asList(image);
        });
        when(albumImageMapRepository.saveAll(anyList())).thenAnswer(new Answer<List<AlbumImageMappingTable>>() {
            @Override
            public List<AlbumImageMappingTable> answer(InvocationOnMock invocation) throws Throwable {
                return Arrays.asList(new AlbumImageMappingTable());
            }
        });

        //when
        imageService.upload(imageMultipartFileRequestDto,user,responseAlbum);

        //then
    }

    @Test
    public void S3롤백() throws IOException {

        //given
        when(userAlbumAmazonS3Uploader.s3Upload(any(Image.class))).thenThrow(new AmazonServiceException("테스트 exception"));

        //then
        assertThrows(AmazonServiceException.class,
                //when
                ()-> imageService.upload(imageMultipartFileRequestDto,user,responseAlbum));

    }

    @Test
    public void 앨범_지정_업로드() throws IOException {
        imageService.upload(imageMultipartFileRequestDto,user,responseAlbum);

    }

    @BeforeEach
    private void given() throws IOException {
        user = User
                .builder()
                .username("ksb1")
                .password("1234")
                .email("ksb@gm")
                .role(RoleType.USER)
                .build();

        String fileName = "balda";
        String contentType = "image/jpeg";
        String path = "./MockFile";
        MockMultipartFile mockMultipartFile = getMockMultipartFile(fileName, contentType, path);
        imageMultipartFileRequestDto = ImageMultipartFileRequestDtoFactory.makeMultipartFileRequestDto(Arrays.asList(mockMultipartFile));

        responseAlbum = new Album();
        responseAlbum.title();
        responseAlbum.setId(1L);
    }

    private MockMultipartFile getMockMultipartFile(String fileName, String contentType, String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path+"/"+fileName+"."+"jpg"));
        return new MockMultipartFile("images", fileName + "." + contentType, contentType, fileInputStream);
    }

}
