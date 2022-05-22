package com.kkanggogo.facealbum.album.domain.repository;

import com.kkanggogo.facealbum.album.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image,Long> {

    Optional<Image> findByImagePath(String imagePath);

}
