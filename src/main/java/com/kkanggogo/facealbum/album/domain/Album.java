package com.kkanggogo.facealbum.album.domain;

import com.kkanggogo.facealbum.album.web.dto.AlbumResponseDto;
import com.kkanggogo.facealbum.login.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(mappedBy = "album",cascade = CascadeType.ALL)
    @Column(updatable = false)
    private List<AlbumImageMappingTable> albumImageMappingTableList =new ArrayList<>();

    public void addAlbumImageMappingTable(AlbumImageMappingTable albumImageMappingTable){
        albumImageMappingTable.setAlbum(this);
        this.albumImageMappingTableList.add(albumImageMappingTable);

    }

    public void setUser(User user) {
        this.user = user;
        user.getAlbumList().add(this);
    }

    @PrePersist
    public void title(){
        if(this.title==null){
            this.title= "제목없음";
        }
    }

    public AlbumResponseDto toAlbumResponseDto(){
        AlbumResponseDto albumResponseDto =new AlbumResponseDto();
        albumResponseDto.setAlbumId(this.id);
        albumResponseDto.setTitle(this.title);
        return albumResponseDto;
    }
}
