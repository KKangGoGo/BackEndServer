package com.kkanggogo.facealbum.album.domein;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public void makeS3Path(Long userId, String imagePath) {
        UUID uuid=UUID.randomUUID();
        this.imagePath = String.format("%d/%s%s",userId,uuid,imagePath);
    }
    public void changeStringToByte(String imageByteString){
        this.imageByte =Base64.getDecoder().decode(imageByteString);
    }
}
