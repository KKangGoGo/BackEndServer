package com.kkanggogo.facealbum.album.domain.repository;

import com.kkanggogo.facealbum.album.domain.AlbumImageMappingTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumImageMapRepository extends JpaRepository<AlbumImageMappingTable,Long> {
}
