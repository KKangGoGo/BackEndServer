package com.kkanggogo.facealbum.album.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class AlbumImageMappingTable {

    @Id @GeneratedValue
    @Column(name = "albumImageMapId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "imageId")
    private Image image;

    @ManyToOne
    @JoinColumn(name = "albumId")
    private Album album;

    public void addAlbumAndImage(Album album,Image image){
        album.getAlbumImageMappingTableList().add(this);
        this.image=image;
        this.album=album;
    }

}
