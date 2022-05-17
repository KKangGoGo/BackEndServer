package com.kkanggogo.facealbum.album.service;

import com.kkanggogo.facealbum.album.domain.Album;
import com.kkanggogo.facealbum.album.web.dto.AlbumImagesResponseDto;
import com.kkanggogo.facealbum.album.web.dto.ImageRequestDto;
import com.kkanggogo.facealbum.connection.web.DetectConnection;
import com.kkanggogo.facealbum.login.config.auth.PrincipalDetails;
import com.kkanggogo.facealbum.login.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class AlbumImageFacade {

    private final AlbumService albumService;
    private final ImageService imageService;
    private final WebClient webClient;


    @Transactional
    public void upload(ImageRequestDto files, PrincipalDetails principalDetails) {
        Album album = albumService.makeAlbum(principalDetails.getUser());
        album.getAlbumImageMappingTableList().size();
        imageService.upload(files, principalDetails.getUser(), album);

        List<String> albumImagePaths = imageService.getAlbumImagePaths(album);
        AlbumImagesResponseDto albumImagesResponseDto=new AlbumImagesResponseDto();
        albumImagesResponseDto.setImages(albumImagePaths);

//        Mono<String> stringMono = webClient.post().uri("/request/detect").bodyValue(albumImagesResponseDto).retrieve().bodyToMono(String.class);
//        stringMono.subscribe(i-> log.debug("connection 확인:{}",i));
    }

    @Transactional
    public void upload(ImageRequestDto files, PrincipalDetails principalDetails, Long albumId) {
        Album album = albumService.findAlbum(albumId, principalDetails.getUser());
        album.getAlbumImageMappingTableList().size();
        imageService.upload(files, principalDetails.getUser(), album);
    }

    @Transactional
    public AlbumImagesResponseDto getAlbumImage(User user, Long albumId) {
        Album album = albumService.findAlbum(albumId, user);
        album.getAlbumImageMappingTableList().size();
        List<String> albumImagePaths = imageService.getAlbumImagePaths(album);
        AlbumImagesResponseDto albumImagesResponseDto=new AlbumImagesResponseDto();
        albumImagesResponseDto.setImages(albumImagePaths);
        return albumImagesResponseDto;
    }
}