package com.kkanggogo.facealbum.album.domain.repository;

import com.kkanggogo.facealbum.album.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album,Long> {
}
