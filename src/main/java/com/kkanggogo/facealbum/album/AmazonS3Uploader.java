package com.kkanggogo.facealbum.album;

import com.amazonaws.AmazonServiceException;
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



    public String s3Upload(Image image) throws AmazonServiceException {
        String bucket= getBucket();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getMediaType());
        InputStream inputStream = new ByteArrayInputStream(image.getImageByte());
        metadata.setContentLength(image.getImageByte().length);
        amazonS3Client.putObject(new PutObjectRequest(bucket, image.getImagePath(), inputStream, metadata));
        return amazonS3Client.getUrl(bucket, image.getImagePath()).toString();

    }

    public String getRegion(){
        return amazonS3Client.getRegion().toAWSRegion().toString();
    }

    public abstract String getBucket();

    public String getPrefixPath(String imagePath){
        return String.format("http://s3.%s.amazonaws.com/%s/%s",getRegion(),getBucket(),imagePath);
    }
}