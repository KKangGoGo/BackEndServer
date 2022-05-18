package com.kkanggogo.facealbum.album.service;

import com.kkanggogo.facealbum.album.domain.Album;
import com.kkanggogo.facealbum.album.web.dto.AlbumImagesResponseDto;
import com.kkanggogo.facealbum.album.web.dto.ImageRequestDto;
import com.kkanggogo.facealbum.connection.dto.DetectMqRequestDto;
import com.kkanggogo.facealbum.connection.dto.TeskIdListDto;
import com.kkanggogo.facealbum.login.config.auth.PrincipalDetails;
import com.kkanggogo.facealbum.login.domain.User;
import com.kkanggogo.facealbum.login.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class AlbumImageFacade {

    private final AlbumService albumService;
    private final ImageService imageService;
    private final UserRepository userRepository;
    private final TeskIdListDto teskIdListDto;
    private final WebClient webClient;


    @Transactional
    public void upload(ImageRequestDto files, PrincipalDetails principalDetails) {
        Album album = albumService.makeAlbum(principalDetails.getUser());
        album.getAlbumImageMappingTableList().size();
        imageService.upload(files, principalDetails.getUser(), album);

        List<String> albumImagePaths = imageService.getAlbumImagePaths(album);
        DetectMqRequestDto detectMqRequestDto=new DetectMqRequestDto();
        detectMqRequestDto.setAlbum_id(album.getId());
        detectMqRequestDto.setImg_urls(albumImagePaths);

        Mono<String> stringMono = webClient.post().uri("/request/detect").bodyValue(detectMqRequestDto).retrieve().bodyToMono(String.class);
        stringMono.subscribe(i->{
            teskIdListDto.add(i);
        });
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

    @Async
    @Transactional
    public void sharingImage(JSONObject jsonObject){
        for(Object key:jsonObject.keySet()){
            Album album = albumService.makeAlbum(userRepository.findByUsername((String) key).get());
            ArrayList<String> imagePathList = (ArrayList<String>)jsonObject.get((String) key);
            imagePathList.stream().forEach(i->{
                imageService.sharingImage(i,album);
            });

        }
    }
}