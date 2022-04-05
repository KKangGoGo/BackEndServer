package com.kkanggogo.facealbum.album.web;

import com.kkanggogo.facealbum.album.service.AlbumService;
import com.kkanggogo.facealbum.album.web.dto.AlbumRequestDto;
import com.kkanggogo.facealbum.album.web.dto.AlbumResponseDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
//@RequiredArgsConstructor
@Slf4j
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @PostMapping("/album/makeAlbum")
    public AlbumResponseDto makeAlbum(@RequestBody(required = false) Optional<AlbumRequestDto> albumRequestDto){
        Long userId=1L;
        if(albumRequestDto.isPresent()){
            return albumService.makeAlbum(userId,albumRequestDto.get().getTitle()).toAlbumResponseDto();
        }
        return albumService.makeAlbum(userId).toAlbumResponseDto();
    }
}
