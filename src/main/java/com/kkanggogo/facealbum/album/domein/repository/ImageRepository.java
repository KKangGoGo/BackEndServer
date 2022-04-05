package com.kkanggogo.facealbum.album.domein.repository;

import com.kkanggogo.facealbum.album.domein.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {
}
