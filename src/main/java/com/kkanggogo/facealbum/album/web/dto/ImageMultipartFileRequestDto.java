package com.kkanggogo.facealbum.album.web.dto;

import com.kkanggogo.facealbum.album.domain.Image;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ImageMultipartFileRequestDto implements ImageRequestDto {
    private List<byte[]> imageByteDates=new ArrayList<>();
    List<String> originFileNames=new ArrayList<>();
    Integer fileCount;


    @Override
    public List<Image> toImageEntity(Long userId) {
        List<Image> imageList=new ArrayList<>();
        for(int i=0;i<fileCount;i++){
            Image image=new Image();
            image.makeS3Path(userId,originFileNames.get(i));
            image.setImageByte(imageByteDates.get(i));
            imageList.add(image);
        }
        return imageList;
    }
}
