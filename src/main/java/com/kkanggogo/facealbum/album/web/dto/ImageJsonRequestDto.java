package com.kkanggogo.facealbum.album.web.dto;

import com.kkanggogo.facealbum.album.domain.Image;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
public class ImageJsonRequestDto implements ImageRequestDto {

    private List<String> originFileNames=new ArrayList<>();

    private List<String> images=new ArrayList<>();

    private Integer fileCount;

    @Override
    public List<Image> toImageEntity(Long userId){
        List<Image> imageList=new ArrayList<>();
        for(int i=0;i<fileCount;i++){
            Image image=new Image();
            image.makeS3Path(userId,originFileNames.get(i));
            image.changeStringToByte(images.get(i));
            imageList.add(image);
        }
        return imageList;
    }
}
