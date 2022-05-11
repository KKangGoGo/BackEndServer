package com.kkanggogo.facealbum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FaceAlbumApplication {

    public static void main(String[] args) {
        SpringApplication.run(FaceAlbumApplication.class, args);
    }

}
