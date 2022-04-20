package com.kkanggogo.facealbum.login.domain;


import com.kkanggogo.facealbum.album.domain.Album;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;




@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class User {

    @Id //private key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //프로젝트에 연결된 DB의 넘버링 방식을 따른다.
    @Column(name = "userId")

    private Long id; //oracle은 시퀀스, mysql은 auto_increment방식. 이 프로젝트는 mysql의 auto_increment방식을 따름


    @Column(nullable = false, length = 100, unique = true)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String email;

    @Enumerated(EnumType.STRING)
    private RoleType role; //enum 타입으로 설정한 RoleType을 사용

    @CreationTimestamp //시간이 자동 입력됨
    private Timestamp createDate;

    @Column(nullable = true, length = 300)
    private String photo;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Album> albumList=new ArrayList<>();

    public void isItUserAlbum(Album album) throws IllegalArgumentException{
        boolean contains = albumList.contains(album);
        log.debug("containsIsSame?:{}",contains);
        if(!contains){
            throw new IllegalArgumentException("사용자의 앨범이 아닙니다.");
        }
    }
}
