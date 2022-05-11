package com.kkanggogo.facealbum.ImageTest;

import com.amazonaws.services.s3.AmazonS3Client;
import com.kkanggogo.facealbum.album.AmazonS3Uploader;
import com.kkanggogo.facealbum.album.UserAlbumAmazonS3Uploader;
import com.kkanggogo.facealbum.album.config.AmazonS3Config;
import com.kkanggogo.facealbum.album.domain.Album;
import com.kkanggogo.facealbum.album.domain.AlbumImageMappingTable;
import com.kkanggogo.facealbum.album.domain.Image;
import com.kkanggogo.facealbum.album.service.AlbumImageFacade;
import com.kkanggogo.facealbum.album.service.AlbumService;
import com.kkanggogo.facealbum.album.service.ImageService;
import com.kkanggogo.facealbum.album.web.dto.AlbumImagesResponseDto;
import com.kkanggogo.facealbum.album.web.dto.AlbumListEntityResponseDto;
import com.kkanggogo.facealbum.login.domain.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
@Tag("unit")
public class AlbumImageFacadeTest {

    @InjectMocks
    AlbumImageFacade albumImageFacade;

    @Mock
    private AlbumService albumService;

    @Mock
    private ImageService imageService;

    @Mock
    private static AmazonS3Uploader amazonS3Uploader;

    @BeforeEach
    public void init(){
        when(amazonS3Uploader.getPrefixPath(anyString())).thenReturn("https://s3.ap-northeast-2.amazonaws.com/com.kkanggogo.facealbum.testbukit/ksb/82647bf0-9061-4213-b623-e1bce614f0050.jpg");
    }

    @Test
    public void 앨범이미지가져오기(){
        //given

        User user=new User();
        user.setId(1L);

        Album album=new Album();
        album.setUser(user);
        album.title();
        album.setId(1L);

        Image image=new Image();
        image.setImagePath("/ksb/82647bf0-9061-4213-b623-e1bce614f0050.jpg");

        AlbumImageMappingTable albumImageMappingTable = new AlbumImageMappingTable();
        albumImageMappingTable.addAlbumAndImage(album,image);

        AlbumListEntityResponseDto albumListEntityResponseDto=new AlbumListEntityResponseDto();
        albumListEntityResponseDto.setAlbumListEntityResponseDto(album,amazonS3Uploader);

        List<String> collect = album.getAlbumImageMappingTableList().stream().map(element -> String.format("%s%s", "https://s3.ap-northeast-2.amazonaws.com/com.kkanggogo.facealbum.testbukit", element.getImage().getImagePath())).collect(Collectors.toList());

        AlbumImagesResponseDto albumImagesResponseDto=new AlbumImagesResponseDto();
        albumImagesResponseDto.setImages(collect);

        when(albumService.findAlbum(anyLong(),any(User.class))).thenReturn(album);
        when(imageService.getAlbumImagePaths(any(Album.class))).thenReturn(collect);

        //when
        AlbumImagesResponseDto albumImage = albumImageFacade.getAlbumImage(user, album.getId());
        //then
        assertThat(albumImage,is(albumImagesResponseDto));
    }
}
