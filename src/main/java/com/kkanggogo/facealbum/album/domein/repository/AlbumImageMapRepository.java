package com.kkanggogo.facealbum.album.domein.repository;

import com.kkanggogo.facealbum.album.domein.AlbumImageMappingTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumImageMapRepository extends JpaRepository<AlbumImageMappingTable,Long> {
}
