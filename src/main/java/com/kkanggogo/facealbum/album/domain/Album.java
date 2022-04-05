package com.kkanggogo.facealbum.album.domain;

import com.kkanggogo.facealbum.album.web.dto.AlbumResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Album {

    @Id @GeneratedValue
    @Column(name = "albumId")
    private Long id;

    private String title;

    @OneToMany(mappedBy = "album")
    private List<AlbumImageMappingTable> albumImageMappingTableList =new ArrayList<>();

    @PrePersist
    public void title(){
        if(this.title==null){
            this.title= LocalDate.now().toString();
        }
    }

    public AlbumResponseDto toAlbumResponseDto(){
        AlbumResponseDto albumResponseDto =new AlbumResponseDto();
        albumResponseDto.setAlbumId(this.id);
        albumResponseDto.setAlbumTitle(this.title);
        return albumResponseDto;
    }
}
