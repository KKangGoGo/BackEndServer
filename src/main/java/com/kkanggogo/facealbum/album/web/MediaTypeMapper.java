package com.kkanggogo.facealbum.album.web;

import org.springframework.http.MediaType;

import java.util.regex.Pattern;

public class MediaTypeMapper {

    private static final String[] imageExtensions={".jpg",".jpeg",".png",".gif"};

    public static String mapping(String originFileName) throws IllegalArgumentException{
        for (String imageExtension:imageExtensions){
            boolean matches = Pattern.matches(String.format("([^^\\s]+(\\.(?i)(%s))$)",imageExtension), originFileName);
            if(matches){
                switch (imageExtension){
                    case ".jpg":
                    case ".jpeg":
                        return MediaType.IMAGE_JPEG_VALUE;
                    case ".png":
                        return MediaType.IMAGE_PNG_VALUE;
                    case ".gif":
                        return MediaType.IMAGE_GIF_VALUE;
                }
            }
        }
        throw new IllegalArgumentException("저장이 가능한 확장자가 아닙니다.");
    }
}
