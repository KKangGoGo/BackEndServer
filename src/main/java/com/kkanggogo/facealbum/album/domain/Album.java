package com.kkanggogo.facealbum.album.domain;

import com.kkanggogo.facealbum.album.web.dto.AlbumResponseDto;
import com.kkanggogo.facealbum.login.domain.User;
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

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(mappedBy = "album",cascade = CascadeType.ALL)
    private List<AlbumImageMappingTable> albumImageMappingTableList =new ArrayList<>();

    public void setUser(User user) {
        this.user = user;
        user.getAlbumList().add(this);
    }

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
