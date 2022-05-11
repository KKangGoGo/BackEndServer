package com.kkanggogo.facealbum.album.web.dto;

import com.kkanggogo.facealbum.album.AmazonS3Uploader;
import com.kkanggogo.facealbum.album.domain.Album;
import com.kkanggogo.facealbum.album.domain.Image;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlbumListEntityResponseDto {
    private String albumId;
    private String title;
    private String image;


    public void setAlbumListEntityResponseDto(Album album, AmazonS3Uploader amazonS3Uploader) {
        this.albumId=String.valueOf(album.getId());
        this.title=String.valueOf(album.getTitle());
        if(album.getAlbumImageMappingTableList().size()==0){
            return;
        }
        Image image = album.getAlbumImageMappingTableList().get(0).getImage();
        this.image=amazonS3Uploader.getPrefixPath(image.getImagePath());
    }
}
