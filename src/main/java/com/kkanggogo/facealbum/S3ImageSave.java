package com.kkanggogo.facealbum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class S3ImageSave implements SaveImage {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    @Override
    public void save(Object file) {
      log.debug("S3bucket={}",bucket);
    }
}
