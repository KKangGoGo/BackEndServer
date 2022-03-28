package com.kkanggogo.facealbum;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    public String upload(MultipartFile multipartFile,String filePath){
        ObjectMetadata metadata=new ObjectMetadata();
        metadata.setContentType(MediaType.IMAGE_JPEG_VALUE);
        try {
            amazonS3Client.putObject(new PutObjectRequest(bucket,filePath,multipartFile.getInputStream(),metadata));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return amazonS3Client.getUrl(bucket,filePath).toString();
    }

    public String upload(byte[] bytes,String filePath){
        ObjectMetadata metadata=new ObjectMetadata();
        metadata.setContentType(MediaType.IMAGE_JPEG_VALUE);
        InputStream inputStream=new ByteArrayInputStream(bytes);
        amazonS3Client.putObject(new PutObjectRequest(bucket,filePath,inputStream,metadata));
        return amazonS3Client.getUrl(bucket,filePath).toString();
    }

}
