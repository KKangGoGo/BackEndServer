package com.kkanggogo.facealbum.album.service;

import com.kkanggogo.facealbum.album.AmazonS3Uploader;
import com.kkanggogo.facealbum.album.domain.Album;
import com.kkanggogo.facealbum.album.domain.AlbumImageMappingTable;
import com.kkanggogo.facealbum.album.domain.Image;
import com.kkanggogo.facealbum.album.domain.repository.AlbumImageMapRepository;
import com.kkanggogo.facealbum.album.domain.repository.ImageRepository;
import com.kkanggogo.facealbum.album.web.dto.ImageRequestDto;
import com.kkanggogo.facealbum.login.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {

    private final AmazonS3Uploader userAlbumAmazonS3Uploader;
    private final ImageRepository imageRepository;
    private final AlbumImageMapRepository albumImageMapRepository;


    @Transactional
    public List<String> upload(ImageRequestDto imageRequestDto, User user, Album album) {
        List<AlbumImageMappingTable> albumImageMappingTableList = new ArrayList<>();
        List<Image> images = imageRequestDto.toImageEntity(user.getUsername());

        for (Image image : images) {
            userAlbumAmazonS3Uploader.s3Upload(image);
            AlbumImageMappingTable albumImageMappingTable = new AlbumImageMappingTable();
            albumImageMappingTable.setImage(image);
            album.addAlbumImageMappingTable(albumImageMappingTable);
            albumImageMappingTableList.add(albumImageMappingTable);
        }

        imageRepository.saveAll(images);
        albumImageMapRepository.saveAll(albumImageMappingTableList);
        log.debug("imageService 저장 실행");
        return images.stream().map(Image::getImagePath).collect(Collectors.toList());
    }


    @Transactional
    public void sharingImage(String imagePath, Album album) {
        Optional<Image> byImagePath = imageRepository.findByImagePath(imagePath.replace("https://kkanggogo-face-bucket.s3.ap-northeast-2.amazonaws.com/", ""));
        Image image = byImagePath.get();
        AlbumImageMappingTable albumImageMappingTable = new AlbumImageMappingTable();
        albumImageMappingTable.setImage(image);
        album.addAlbumImageMappingTable(albumImageMappingTable);

        albumImageMapRepository.save(albumImageMappingTable);
    }

    public List<String> getAlbumImagePaths(Album album) {
        return album.getAlbumImageMappingTableList().stream().map(element -> userAlbumAmazonS3Uploader.getPrefixPath(element.getImage().getImagePath())).collect(Collectors.toList());
    }

    public List<String> getImagePathList(Album album) {
        return album.getAlbumImageMappingTableList().stream().map(element -> element.getImage().getImagePath()).collect(Collectors.toList());
    }
}
