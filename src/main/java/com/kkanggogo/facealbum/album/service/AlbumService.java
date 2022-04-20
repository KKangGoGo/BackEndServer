package com.kkanggogo.facealbum.album.service;

import com.kkanggogo.facealbum.album.domain.Album;
import com.kkanggogo.facealbum.album.domain.repository.AlbumRepository;
import com.kkanggogo.facealbum.login.domain.User;
import com.kkanggogo.facealbum.login.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;

    private final UserRepository userRepository;



    @Transactional
    public Album makeAlbum(User findUser, String title){
        Album album=new Album();
        Optional<User> user = userRepository.searchId(findUser.getId());
        user.orElseThrow(()->new IllegalArgumentException("사용자가 없습니다."));
        album.setTitle(title);
        album.setUser(user.get());
        Album save = albumRepository.save(album);
        log.debug("AlbumService의 save 실행");
        return save;
    }

    @Transactional
    public Album makeAlbum(User findUser){
        Album album=new Album();
        Optional<User> user = userRepository.searchId(findUser.getId());
        user.orElseThrow(()->new IllegalArgumentException("사용자가 없습니다."));
        album.setUser(user.get());
        Album save=albumRepository.save(album);
        return save;
    }

    @Transactional
    public Album findAlbum(Long albumId,User user){
        Optional<Album> album = albumRepository.findById(albumId);
        Optional<User> findUser = userRepository.findById(user.getId());
        album.orElseThrow(()->new IllegalArgumentException("앨범을 찾을 수 없습니다."));
        findUser.orElseThrow(()->new IllegalArgumentException("사용자가 없습니다."));
        User user1 = findUser.get();
        Album album1 = album.get();
        user1.isItUserAlbum(album1);
        return album1;
    }

    @Transactional
    public Album updateAlbumInfo(Long albumId,User user,String title) {
        Album album = findAlbum(albumId, user);
        album.setTitle(title);
        return album;
    }
}
