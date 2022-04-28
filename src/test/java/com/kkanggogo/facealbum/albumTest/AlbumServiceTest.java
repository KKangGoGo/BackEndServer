package com.kkanggogo.facealbum.albumTest;

import com.kkanggogo.facealbum.album.AmazonS3Uploader;
import com.kkanggogo.facealbum.album.domain.Album;
import com.kkanggogo.facealbum.album.domain.AlbumImageMappingTable;
import com.kkanggogo.facealbum.album.domain.Image;
import com.kkanggogo.facealbum.album.domain.repository.AlbumRepository;
import com.kkanggogo.facealbum.album.service.AlbumService;
import com.kkanggogo.facealbum.album.web.dto.AlbumListEntityResponseDto;
import com.kkanggogo.facealbum.album.web.dto.AlbumListResponseDto;
import com.kkanggogo.facealbum.login.domain.User;
import com.kkanggogo.facealbum.login.domain.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
@Tag("unit")
public class AlbumServiceTest {

    @InjectMocks
    private AlbumService albumService;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AmazonS3Uploader amazonS3Uploader;

    private User foundUser;
    private User requestUser;
    private Album responseAlbum;

    @BeforeEach
    public void init() {
        //given
        responseAlbum = new Album();
        responseAlbum.title();
        responseAlbum.setId(1L);

        foundUser = User.builder().id(1L).email("k@h").username("ksb").password("1234").albumList(new ArrayList<>()).build();
        requestUser = User.builder().id(1L).email("k@h").username("ksb").password("1234").albumList(new ArrayList<>()).build();
    }

    @Test
    public void 타이틀_지정안한_앨범만들기() {
        //given

        Optional<User> user = Optional.of(foundUser);

        when(userRepository.searchId(anyLong())).thenReturn(user);
        when(albumRepository.save(any(Album.class))).thenReturn(responseAlbum);

        //when
        Album album = albumService.makeAlbum(requestUser);

        //then
        assertThat(responseAlbum.getTitle(), is(album.getTitle()));
        assertThat(responseAlbum.getId(), is(album.getId()));
    }

    @Test
    public void 타이틀_지정_앨범만들기() {
        //given
        Optional<User> user = Optional.of(foundUser);

        when(userRepository.searchId(anyLong())).thenReturn(user);
        when(albumRepository.save(any(Album.class))).thenReturn(responseAlbum);


        //when
        Album album = albumService.makeAlbum(requestUser, "aaaaaa");

        //then
        assertThat(responseAlbum.getTitle(), is(album.getTitle()));
        assertThat(responseAlbum.getId(), is(album.getId()));
    }

    @Test
    public void  앨범_찾기(){
        //given
        Long albumId=1L;
        foundUser.getAlbumList().add(responseAlbum);
        Optional<User> user = Optional.of(foundUser);
        when(userRepository.findById(anyLong())).thenReturn(user);
        when(albumRepository.findById(anyLong())).thenReturn(Optional.of(responseAlbum));
        //when
        Album album = albumService.findAlbum(albumId,requestUser);
        //then
        assertThat(album.getId(),is(responseAlbum.getId()));
        assertThat(album.getTitle(),is(responseAlbum.getTitle()));
    }

    @Test
    public void  유저앨범_리스트_가져오기(){

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

        when(amazonS3Uploader.getPrefixPath(anyString())).thenReturn("https://s3.ap-northeast-2.amazonaws.com/com.kkanggogo.facealbum.testbukit/ksb/82647bf0-9061-4213-b623-e1bce614f0050.jpg");
        AlbumListEntityResponseDto albumListEntityResponseDto=new AlbumListEntityResponseDto();
        albumListEntityResponseDto.setAlbumListEntityResponseDto(album,amazonS3Uploader);

        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));

        //when
        AlbumListResponseDto userAlbum = albumService.findUserAlbum(user);

        assertThat(userAlbum.getAlbumlist().get(0).getAlbumId(),is(albumListEntityResponseDto.getAlbumId()));
        assertThat(userAlbum.getAlbumlist().get(0).getTitle(),is(albumListEntityResponseDto.getTitle()));
        assertThat(userAlbum.getAlbumlist().get(0).getImage(),is(albumListEntityResponseDto.getImage()));
        System.out.println("albumListEntityResponseDto.getImage() = " + albumListEntityResponseDto.getImage());
    }

    @Test
    public void 사용자_못찾음() {
        //given
        Optional<User> user = Optional.empty();

        when(userRepository.searchId(anyLong())).thenReturn(user);

        IllegalArgumentException illegalArgumentException
                = assertThrows(IllegalArgumentException.class, () -> albumService.makeAlbum(requestUser, "aaaaaa"));
        assertThat(illegalArgumentException.getMessage(), is("사용자가 없습니다."));
    }

}
