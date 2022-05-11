package com.kkanggogo.facealbum.album.service;

import com.kkanggogo.facealbum.album.domain.Album;
import com.kkanggogo.facealbum.album.web.dto.ImageRequestDto;
import com.kkanggogo.facealbum.login.config.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Component
@RequiredArgsConstructor
public class ImageUploadFacade {

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
}