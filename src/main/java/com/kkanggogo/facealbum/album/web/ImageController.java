package com.kkanggogo.facealbum.album.web;

import com.kkanggogo.facealbum.album.ImageMultipartFileRequestDtoFactory;
import com.kkanggogo.facealbum.album.service.AlbumImageFacade;
import com.kkanggogo.facealbum.album.web.dto.AlbumImagesResponseDto;
import com.kkanggogo.facealbum.album.web.dto.ImageJsonRequestDto;
import com.kkanggogo.facealbum.login.config.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ImageController {

    private final AlbumImageFacade albumImageFacade;

    @GetMapping("/api/user/album/images")
    public AlbumImagesResponseDto getAlbumImages(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam Long albumId) {
        return albumImageFacade.getAlbumImage(principalDetails.getUser(), albumId);
    }

    @PostMapping("/api/user/album/images")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void uploadImage(@RequestParam("images") List<MultipartFile> files,
                            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        albumImageFacade.upload(ImageMultipartFileRequestDtoFactory.makeMultipartFileRequestDto(files), principalDetails);
    }

    /**
     * 앨범에 사진 업로드 요청
     *
     * @param files
     * @param albumId
     * @param principalDetails
     */
    @PostMapping("/api/user/album/images/{album-id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void uploadImage(@RequestParam("images") List<MultipartFile> files,
                            @PathVariable("album-id") Long albumId,
                            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        albumImageFacade.upload(ImageMultipartFileRequestDtoFactory.makeMultipartFileRequestDto(files), principalDetails, albumId);
    }

    @PostMapping("api/imageupload/jsondata")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void uploadImageForJson(@RequestBody ImageJsonRequestDto imageJsonRequestDto,
                                   @AuthenticationPrincipal PrincipalDetails principalDetails) {
        albumImageFacade.upload(imageJsonRequestDto, principalDetails);
    }
}
