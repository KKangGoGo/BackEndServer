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
}
