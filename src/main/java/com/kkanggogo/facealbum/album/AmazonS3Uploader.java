package com.kkanggogo.facealbum.album;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.kkanggogo.facealbum.album.domain.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
public abstract class AmazonS3Uploader {

    @Autowired
    public AmazonS3Client amazonS3Client;



    public String s3Upload(Image image) {
        String bucket= getBucket();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(MediaType.IMAGE_JPEG_VALUE);
        InputStream inputStream = new ByteArrayInputStream(image.getImageByte());
        metadata.setContentLength(image.getImageByte().length);
        amazonS3Client.putObject(new PutObjectRequest(bucket, image.getImagePath(), inputStream, metadata));
        return amazonS3Client.getUrl(bucket, image.getImagePath()).toString();
    }

    public abstract String getBucket();
}