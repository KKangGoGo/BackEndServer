package com.kkanggogo.facealbum.album.service;

import com.kkanggogo.facealbum.album.domain.Album;
import com.kkanggogo.facealbum.album.web.dto.AlbumImagesResponseDto;
import com.kkanggogo.facealbum.album.web.dto.ImageRequestDto;
import com.kkanggogo.facealbum.connection.dto.DetectMqRequestDto;
import com.kkanggogo.facealbum.connection.dto.TaskIdListDto;
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
    private final TaskIdListDto taskIdListDto;
    private final WebClient webClient;

    @Transactional
    public void upload(ImageRequestDto files, PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        Album album = albumService.makeAlbum(user);
        album.getAlbumImageMappingTableList().size();
        imageService.upload(files, principalDetails.getUser(), album);

        List<String> albumImagePaths = imageService.getImagePathList(album);
        if (user.getPhoto() != null) {
            sendDetectMq(album.getId(), albumImagePaths);
        }
    }

    @Transactional
    public void upload(ImageRequestDto files, PrincipalDetails principalDetails, Long albumId) {
        User user = principalDetails.getUser();
        Album album = albumService.findAlbum(albumId, user);
        album.getAlbumImageMappingTableList().size();
        List<String> albumImagePaths = imageService.upload(files, principalDetails.getUser(), album);

//        if (user.getPhoto() != null) {
//            sendDetectMq(album.getId(), albumImagePaths);
//        }
    }

    @Transactional
    public void shareAlbumImages(User user, Long albumId) {
        Album album = albumService.findAlbum(albumId, user);
        album.getAlbumImageMappingTableList().size();

        List<String> imagePaths = imageService.getAlbumImagePaths(album);

        if (user.getPhoto() != null) {
            sendDetectMq(albumId, imagePaths);
        }
    }


    @Transactional
    public AlbumImagesResponseDto getAlbumImage(User user, Long albumId) {
        Album album = albumService.findAlbum(albumId, user);
        album.getAlbumImageMappingTableList().size();
        List<String> albumImagePaths = imageService.getAlbumImagePaths(album);
        AlbumImagesResponseDto albumImagesResponseDto = new AlbumImagesResponseDto();
        albumImagesResponseDto.setAlbumTitle(album.getTitle());
        albumImagesResponseDto.setImages(albumImagePaths);
        return albumImagesResponseDto;
    }

    @Async
    @Transactional
    public void sharingImage(JSONObject jsonObject) {
        for (Object key : jsonObject.keySet()) {
            User user = userRepository.findByUsername((String) key).get();
            user.getAlbumList().size();
            Album album;
            if (user.isUserHaveSharingAlbum()) {
                album = user.getSharingAlbum();
            } else {
                album = albumService.makeSharingAlbum(user);
                album.setTitle("공유 앨범");
            }
            ArrayList<String> imagePathList = (ArrayList<String>) jsonObject.get((String) key);
            imagePathList.forEach(i -> imageService.sharingImage(i, album));
        }
    }

    private void sendDetectMq(Long albumId, List<String> imagePaths) {
        DetectMqRequestDto detectMqRequestDto = new DetectMqRequestDto();
        detectMqRequestDto.setAlbum_id(albumId);
        detectMqRequestDto.setImg_urls(imagePaths);


        Mono<String> stringMono = webClient.post().uri("/request/detect").bodyValue(detectMqRequestDto).retrieve().bodyToMono(String.class);
        stringMono.subscribe(i -> {
            log.debug("taskKey:{}", i);
            taskIdListDto.add(i);
        });
    }

}