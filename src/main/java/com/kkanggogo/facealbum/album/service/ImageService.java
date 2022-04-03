package com.kkanggogo.facealbum.album.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.kkanggogo.facealbum.album.domein.Album;
import com.kkanggogo.facealbum.album.domein.AlbumImageMappingTable;
import com.kkanggogo.facealbum.album.domein.Image;
import com.kkanggogo.facealbum.album.web.dto.ImageJsonRequestDto;
import com.kkanggogo.facealbum.album.domein.repository.AlbumImageMapRepository;
import com.kkanggogo.facealbum.album.domein.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

//이미지를 업로드한다.
@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    private final AlbumService albumService;
    public final ImageRepository imageRepository;
    public final AlbumImageMapRepository albumImageMapRepository;

    @Async
    @Transactional
    public void upload(ImageJsonRequestDto imageJsonRequestDto){
        Long userId=1L;
        List<AlbumImageMappingTable> albumImageMappingTableList =new ArrayList<>();
        Album album = albumService.makeAlbum(userId);
        List<Image> images = imageJsonRequestDto.toImageEntity(userId);

        for(Image image:images){
            s3Upload(image);
            AlbumImageMappingTable albumImageMappingTable =new AlbumImageMappingTable(image,album);
            albumImageMappingTableList.add(albumImageMappingTable);
        }

        imageRepository.saveAll(images);
        albumImageMapRepository.saveAll(albumImageMappingTableList);
    }

    @Transactional
    public void upload(List<MultipartFile> files) {
        Long userId=1L;
        List<AlbumImageMappingTable> albumImageMappingTableList =new ArrayList<>();
        List<Image> images=new ArrayList<>();
        Album album = albumService.makeAlbum(userId);

        for(MultipartFile file:files){
            Image image=new Image();
            image.setImagePath(userId,file.getOriginalFilename());
            try {
                image.setImageByte(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            s3Upload(image);
            images.add(image);
            AlbumImageMappingTable albumImageMappingTable =new AlbumImageMappingTable(image,album);
            albumImageMappingTableList.add(albumImageMappingTable);
        }

        imageRepository.saveAll(images);
        albumImageMapRepository.saveAll(albumImageMappingTableList);
    }

    public String s3Upload(Image image){
        ObjectMetadata metadata=new ObjectMetadata();
        metadata.setContentType(MediaType.IMAGE_JPEG_VALUE);
        InputStream inputStream=new ByteArrayInputStream(image.getImageByte());
        metadata.setContentLength(image.getImageByte().length);
        amazonS3Client.putObject(new PutObjectRequest(bucket,image.getImagePath(),inputStream,metadata));
        return amazonS3Client.getUrl(bucket,image.getImagePath()).toString();
    }
}
