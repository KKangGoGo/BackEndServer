package com.kkanggogo.facealbum.album.service;

import com.kkanggogo.facealbum.album.domain.Album;
import com.kkanggogo.facealbum.album.domain.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;

    @Transactional
    public Album makeAlbum(Long userId, String title){
        Album album=new Album();
        album.setTitle(title);
        Album save = albumRepository.save(album);
        return save;
    }

    @Transactional
    public Album makeAlbum(Long userId){
        Album album=new Album();
        Album save=albumRepository.save(album);
        return save;
    }
}
