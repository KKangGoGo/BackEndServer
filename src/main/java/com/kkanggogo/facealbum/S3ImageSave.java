package com.kkanggogo.facealbum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3ImageSave implements SaveImage {

    public final S3Uploader s3Uploader;

    @Override
    public void save(Object file) {
        UUID uuid = UUID.randomUUID();
        String path="static/"+uuid+".jpg";
        String upload;
        if(file instanceof MultipartFile){
            upload=s3Uploader.upload(((MultipartFile) file), path);
        }
        else {
            upload=s3Uploader.upload((byte[]) file,path);
        }

        log.info(upload);
    }
}
