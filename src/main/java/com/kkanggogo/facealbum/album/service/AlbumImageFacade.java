package com.kkanggogo.facealbum.album.service;

import com.kkanggogo.facealbum.album.domain.Album;
import com.kkanggogo.facealbum.album.web.dto.AlbumImagesResponseDto;
import com.kkanggogo.facealbum.album.web.dto.ImageRequestDto;
import com.kkanggogo.facealbum.login.config.auth.PrincipalDetails;
import com.kkanggogo.facealbum.login.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class AlbumImageFacade {

    private final AlbumService albumService;
    private final ImageService imageService;


    @Transactional
    public void upload(ImageRequestDto files, PrincipalDetails principalDetails) {
        Album album = albumService.makeAlbum(principalDetails.getUser());
        album.getAlbumImageMappingTableList().size();
        imageService.upload(files, principalDetails.getUser(), album);
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