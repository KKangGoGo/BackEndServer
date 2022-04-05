package com.kkanggogo.facealbum.album.service;

import com.kkanggogo.facealbum.album.AmazonS3Uploader;
import com.kkanggogo.facealbum.album.domain.Album;
import com.kkanggogo.facealbum.album.domain.AlbumImageMappingTable;
import com.kkanggogo.facealbum.album.domain.Image;
import com.kkanggogo.facealbum.album.domain.repository.AlbumImageMapRepository;
import com.kkanggogo.facealbum.album.domain.repository.ImageRepository;
import com.kkanggogo.facealbum.album.web.dto.ImageRequestDto;
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

    private final AmazonS3Uploader amazonS3Uploader;
    private final AlbumService albumService;
    private final ImageRepository imageRepository;
    private final AlbumImageMapRepository albumImageMapRepository;

    @Async
    @Transactional
    public void upload(ImageRequestDto imageRequestDto){
        Long userId=1L;
        List<AlbumImageMappingTable> albumImageMappingTableList =new ArrayList<>();
        Album album = albumService.makeAlbum(userId);
        List<Image> images = imageRequestDto.toImageEntity(userId);

        for(Image image:images){
            String amazonS3path = amazonS3Uploader.s3Upload(image);
            image.setImagePath(amazonS3path);
            AlbumImageMappingTable albumImageMappingTable =new AlbumImageMappingTable(image,album);
            albumImageMappingTableList.add(albumImageMappingTable);
        }

        imageRepository.saveAll(images);
        albumImageMapRepository.saveAll(albumImageMappingTableList);
    }
}
