package com.kkanggogo.facealbum.album.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.MediaType;

import javax.persistence.*;
import java.util.Base64;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Image {

    @Id @GeneratedValue
    @Column(name = "imageId")
    private Long id;

    private String imagePath;

    @Transient
    private byte[] imageByte;

    @Transient
    private String mediaType;


    public void makeS3Path(String userName, String imagePath) {
        UUID uuid=UUID.randomUUID();
        this.imagePath = String.format("%s/%s%s",userName,uuid,imagePath);
    }

    public void changeStringToByte(String imageByteString){
        this.imageByte =Base64.getDecoder().decode(imageByteString);
    }
}
