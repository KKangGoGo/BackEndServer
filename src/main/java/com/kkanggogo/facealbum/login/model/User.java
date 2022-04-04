package com.kkanggogo.facealbum.login.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id //private key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //프로젝트에 연결된 DB의 넘버링 방식을 따른다.
    @Column(name = "userId")
    private int id; //oracle은 시퀀스, mysql은 auto_increment방식. 이 프로젝트는 mysql의 auto_increment방식을 따름

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
}
