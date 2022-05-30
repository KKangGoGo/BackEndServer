package com.kkanggogo.facealbum.album.service;

import com.kkanggogo.facealbum.album.AmazonS3Uploader;
import com.kkanggogo.facealbum.album.domain.Album;
import com.kkanggogo.facealbum.album.domain.SharingAlbum;
import com.kkanggogo.facealbum.album.domain.repository.AlbumRepository;
import com.kkanggogo.facealbum.album.web.dto.AlbumListEntityResponseDto;
import com.kkanggogo.facealbum.album.web.dto.AlbumListResponseDto;
import com.kkanggogo.facealbum.login.domain.User;
import com.kkanggogo.facealbum.login.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;

    private final UserRepository userRepository;

    private final AmazonS3Uploader userAlbumAmazonS3Uploader;


    @Transactional
    public Album makeAlbum(User findUser, String title) {
        Album album = new Album();
        Optional<User> user = userRepository.searchId(findUser.getId());
        user.orElseThrow(() -> new IllegalArgumentException("사용자가 없습니다."));
        album.setTitle(title);
        album.setUser(user.get());
        Album save = albumRepository.save(album);
        log.debug("AlbumService의 save 실행");
        return save;
    }

    @Transactional
    public Album makeAlbum(User findUser) {
        Album album = new Album();
        Optional<User> user = userRepository.searchId(findUser.getId());
        user.orElseThrow(() -> new IllegalArgumentException("사용자가 없습니다."));
        album.setUser(user.get());
        return albumRepository.save(album);
    }

    @Transactional
    public Album findAlbum(Long albumId, User user) {
        Optional<Album> album = albumRepository.findById(albumId);
        Optional<User> findUser = userRepository.findById(user.getId());
        album.orElseThrow(() -> new IllegalArgumentException("앨범을 찾을 수 없습니다."));
        findUser.orElseThrow(() -> new IllegalArgumentException("사용자가 없습니다."));
        User user1 = findUser.get();
        Album album1 = album.get();
        user1.isItUserAlbum(album1);
        return album1;
    }

    @Transactional
    public AlbumListResponseDto findUserAlbum(User paramUser) {
        Optional<User> optionalUser = userRepository.findById(paramUser.getId());
        optionalUser.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        User user = optionalUser.get();

        List<AlbumListEntityResponseDto> collect = user.getAlbumList().stream().map(element -> {
            AlbumListEntityResponseDto albumListEntityResponseDto = new AlbumListEntityResponseDto();
            albumListEntityResponseDto.setAlbumListEntityResponseDto(element, userAlbumAmazonS3Uploader);
            return albumListEntityResponseDto;
        }).collect(Collectors.toList());

        AlbumListResponseDto albumListResponseDto = new AlbumListResponseDto();
        albumListResponseDto.setAlbumlist(collect);
        return albumListResponseDto;
    }

    @Transactional
    public Album updateAlbumInfo(Long albumId, User user, String title) {
        Album album = findAlbum(albumId, user);
        album.setTitle(title);
        return album;
    }

    public Album makeSharingAlbum(User findUser) {
        SharingAlbum album = new SharingAlbum();
        Optional<User> user = userRepository.searchId(findUser.getId());
        user.orElseThrow(() -> new IllegalArgumentException("사용자가 없습니다."));
        album.setUser(user.get());
        return albumRepository.save(album);
    }
}
