package com.kkanggogo.facealbum.album.web.dto;

import com.kkanggogo.facealbum.album.domein.Image;
import java.util.List;

public interface ImageRequestDto {

    public List<Image> toImageEntity(Long userId);

}
