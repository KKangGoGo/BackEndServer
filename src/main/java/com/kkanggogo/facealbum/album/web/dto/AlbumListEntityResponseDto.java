package com.kkanggogo.facealbum.album.web.dto;

import com.kkanggogo.facealbum.album.domain.Album;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlbumListEntityResponseDto {
    private String albumId;
    private String title;
    private String image;


    public void setAlbumListEntityResponseDto(Album album, String prefixPath) {
        this.albumId=String.valueOf(album.getId());
        this.title=String.valueOf(album.getTitle());
        if(album.getAlbumImageMappingTableList().size()==0){
            return;
        }
        this.image=String.format("%s%s",prefixPath, album.getAlbumImageMappingTableList().get(0).getImage().getImagePath());
    }
}
