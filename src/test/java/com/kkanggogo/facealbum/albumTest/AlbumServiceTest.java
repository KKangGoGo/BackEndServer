package com.kkanggogo.facealbum.albumTest;

import com.kkanggogo.facealbum.album.domein.Album;
import com.kkanggogo.facealbum.album.domein.repository.AlbumRepository;
import com.kkanggogo.facealbum.album.service.AlbumService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class AlbumServiceTest {

    @InjectMocks
    AlbumService albumService;
//
    @Mock
    AlbumRepository albumRepository;

    @Test
    public void  타이틀_지정안한_앨범만들기(){
        //given
        Album responseAlbum = new Album();
        responseAlbum.title();
        responseAlbum.setId(1L);
        when(albumRepository.save(any(Album.class))).thenReturn(responseAlbum);

        //when
        Album album = albumService.makeAlbum(1L);
        //then
        assertThat(responseAlbum.getTitle(),is(album.getTitle()));
        assertThat(responseAlbum.getId(),is(album.getId()));
    }

    @Test
    public void  타이틀_지정_앨범만들기(){
        //given
        Album responseAlbum = new Album();
        responseAlbum.setTitle("aaaaaa");
        responseAlbum.setId(1L);
        when(albumRepository.save(any(Album.class))).thenReturn(responseAlbum);

        //when
        Album album = albumService.makeAlbum(1L,"aaaaaa");
        //then
        assertThat(responseAlbum.getTitle(),is(album.getTitle()));
        assertThat(responseAlbum.getId(),is(album.getId()));
    }
}
