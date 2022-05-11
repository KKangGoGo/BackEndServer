package com.kkanggogo.facealbum.album.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AlbumListResponseDto {

    public void add(AlbumListEntityResponseDto albumListEntityResponseDto) {
        albumlist.add(albumListEntityResponseDto);
    }

    private List<AlbumListEntityResponseDto> albumlist=new ArrayList<>();

}
