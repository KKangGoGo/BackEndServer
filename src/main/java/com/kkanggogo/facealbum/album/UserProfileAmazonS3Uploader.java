package com.kkanggogo.facealbum.album;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserProfileAmazonS3Uploader extends AmazonS3Uploader{

    @Value("${cloud.aws.s3.profile-bucket}")
    public String bucket;

    @Override
    public String getBucket() {
        return bucket;
    }

    @Override
    public String getPrefixPath(){
        return String.format("s3.%s.amazonaws.com/%s/",getRegion(),bucket);
    }
}
