package com.kkanggogo.facealbum.album.web.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AlbumImagesResponseDto {
    String albumTitle;
    List<String> images=new ArrayList<>();
}
