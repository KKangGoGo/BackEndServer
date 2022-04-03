package com.kkanggogo.facealbum.album.domein.repository;

import com.kkanggogo.facealbum.album.domein.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album,Long> {

}
