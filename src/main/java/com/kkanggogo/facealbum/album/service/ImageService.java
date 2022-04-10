package com.kkanggogo.facealbum.album.service;

import com.kkanggogo.facealbum.album.AmazonS3Uploader;
import com.kkanggogo.facealbum.album.domain.Album;
import com.kkanggogo.facealbum.album.domain.AlbumImageMappingTable;
import com.kkanggogo.facealbum.album.domain.Image;
import com.kkanggogo.facealbum.album.domain.repository.AlbumImageMapRepository;
import com.kkanggogo.facealbum.album.domain.repository.ImageRepository;
import com.kkanggogo.facealbum.album.web.dto.ImageRequestDto;
import com.kkanggogo.facealbum.login.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {

    private final AmazonS3Uploader userAlbumAmazonS3Uploader;
    private final AlbumService albumService;
    private final ImageRepository imageRepository;
    private final AlbumImageMapRepository albumImageMapRepository;

    @Async
    @Transactional
    public void upload(ImageRequestDto imageRequestDto, User user){
        List<AlbumImageMappingTable> albumImageMappingTableList =new ArrayList<>();
        Album album = albumService.makeAlbum(user);
        List<Image> images = imageRequestDto.toImageEntity(user.getUsername());

        for(Image image:images){
            userAlbumAmazonS3Uploader.s3Upload(image);
            AlbumImageMappingTable albumImageMappingTable =new AlbumImageMappingTable(image,album);
            albumImageMappingTableList.add(albumImageMappingTable);
        }

        imageRepository.saveAll(images);
        albumImageMapRepository.saveAll(albumImageMappingTableList);
        log.debug("imageService 저장 실행");
    }
}
