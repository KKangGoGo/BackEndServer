package com.kkanggogo.facealbum.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
@Getter
public class ImageData {

    private List<byte[]> images=new ArrayList<>();

    public void setImages(List<String> imageStrings) {
        for(String str:imageStrings){
            byte[] bytes=Base64.getDecoder().decode(str);
            images.add(bytes);
        }
    }
}
