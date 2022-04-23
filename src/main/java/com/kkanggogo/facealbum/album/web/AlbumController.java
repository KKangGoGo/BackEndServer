package com.kkanggogo.facealbum.album.web;

import com.kkanggogo.facealbum.album.domain.Album;
import com.kkanggogo.facealbum.album.service.AlbumService;
import com.kkanggogo.facealbum.album.web.dto.AlbumListResponseDto;
import com.kkanggogo.facealbum.album.web.dto.AlbumRequestDto;
import com.kkanggogo.facealbum.album.web.dto.AlbumResponseDto;
import com.kkanggogo.facealbum.login.config.auth.PrincipalDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
//@RequiredArgsConstructor
@Slf4j
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @PostMapping("/api/user/album/create")
    public AlbumResponseDto makeAlbum(@RequestBody(required = false) Optional<AlbumRequestDto> albumRequestDto,
                                      @AuthenticationPrincipal PrincipalDetails principalDetails){
        if(albumRequestDto.isPresent()){
            return albumService.makeAlbum(principalDetails.getUser(),albumRequestDto.get().getTitle()).toAlbumResponseDto();
        }
        return albumService.makeAlbum(principalDetails.getUser()).toAlbumResponseDto();
    }

    @GetMapping("/api/user/album-list")
    public AlbumListResponseDto getAlbumList(@AuthenticationPrincipal PrincipalDetails principalDetails){
        return albumService.findUserAlbum(principalDetails.getUser());
    }
  
    @PutMapping("/api/user/album/{album-id}")
    public AlbumResponseDto updateAlbum(@RequestBody AlbumRequestDto albumRequestDto, @PathVariable("album-id") Long albumId,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails){
        Album album = albumService.updateAlbumInfo(albumId, principalDetails.getUser(), albumRequestDto.getTitle());
        return album.toAlbumResponseDto();
    }
}
