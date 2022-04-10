package com.kkanggogo.facealbum.album.web;

import com.kkanggogo.facealbum.album.service.ImageService;
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


    private final ImageService imageService;

    @PostMapping("/api/user/album-list")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void uploadImage(@RequestParam("images") List<MultipartFile> files,
                            @AuthenticationPrincipal PrincipalDetails principalDetails){
        imageService.upload(ImageMultipartFileRequestDtoFactory.makeMultipartFileRequestDto(files),principalDetails.getUser());
    }

    @PostMapping("api/imageupload/jsondata")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void uploadImageForJson(@RequestBody ImageJsonRequestDto imageJsonRequestDto,
                                   @AuthenticationPrincipal PrincipalDetails principalDetails){
        imageService.upload(imageJsonRequestDto,principalDetails.getUser());
    }
}
