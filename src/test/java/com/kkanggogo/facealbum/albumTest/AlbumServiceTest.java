package com.kkanggogo.facealbum.albumTest;

import com.kkanggogo.facealbum.album.domain.Album;
import com.kkanggogo.facealbum.album.domain.repository.AlbumRepository;
import com.kkanggogo.facealbum.album.service.AlbumService;
import com.kkanggogo.facealbum.login.domain.User;
import com.kkanggogo.facealbum.login.domain.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class AlbumServiceTest {

    @InjectMocks
    private AlbumService albumService;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private UserRepository userRepository;

    private User foundUser;
    private User requestUser;
    private Album responseAlbum;

    @BeforeEach
    public void init(){
        //given
        responseAlbum = new Album();
        responseAlbum.title();
        responseAlbum.setId(1L);

        foundUser = User.builder().id(1L).email("k@h").username("ksb").password("1234").albumList(new ArrayList<>()).build();
        requestUser = User.builder().id(1L).email("k@h").username("ksb").password("1234").albumList(new ArrayList<>()).build();
    }

    @Test
    public void  타이틀_지정안한_앨범만들기(){
        //given

        Optional<User> user = Optional.of(foundUser);

        when(userRepository.searchId(anyLong())).thenReturn(user);
        when(albumRepository.save(any(Album.class))).thenReturn(responseAlbum);

        //when
        Album album = albumService.makeAlbum(requestUser);

        //then
        assertThat(responseAlbum.getTitle(),is(album.getTitle()));
        assertThat(responseAlbum.getId(),is(album.getId()));
    }

    @Test
    public void  타이틀_지정_앨범만들기(){
        //given
        Optional<User> user = Optional.of(foundUser);

        when(userRepository.searchId(anyLong())).thenReturn(user);
        when(albumRepository.save(any(Album.class))).thenReturn(responseAlbum);


        //when
        Album album = albumService.makeAlbum(requestUser,"aaaaaa");

        //then
        assertThat(responseAlbum.getTitle(),is(album.getTitle()));
        assertThat(responseAlbum.getId(),is(album.getId()));
    }

    @Transactional
    @Test
    public void  앨범_찾기(){
        Long albumId=1L;
        Album album = albumService.findAlbum(albumId,requestUser);
        assertThat(album.getId(),is(responseAlbum.getId()));
        assertThat(album.getTitle(),is(responseAlbum.getTitle()));
    }

    @Test
    public void  사용자_못찾음(){
        //given
        Optional<User> user = Optional.empty();

        when(userRepository.searchId(anyLong())).thenReturn(user);

        IllegalArgumentException illegalArgumentException
                = assertThrows(IllegalArgumentException.class, () -> albumService.makeAlbum(requestUser, "aaaaaa"));
        assertThat(illegalArgumentException.getMessage(),is("사용자가 없습니다."));
    }

}
