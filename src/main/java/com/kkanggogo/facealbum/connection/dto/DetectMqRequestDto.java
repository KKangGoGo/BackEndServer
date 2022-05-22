package com.kkanggogo.facealbum.connection.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DetectMqRequestDto {
    Long album_id;
    List<String> img_urls=new ArrayList<>();
}
