package com.kkanggogo.facealbum.album;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserAlbumAmazonS3Uploader extends AmazonS3Uploader{

    @Value("${cloud.aws.s3.albumbucket}")
    public String bucket;

    @Override
    public String getBucket() {
        return bucket;
    }
}
