package com.kkanggogo.facealbum.album.web;

import com.kkanggogo.facealbum.album.service.ImageService;
import com.kkanggogo.facealbum.album.web.dto.ImageJsonRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UpLoadController {


    private final ImageService imageService;


    @PostMapping("/test")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void uploadImage(@RequestParam("images") List<MultipartFile> files){
        log.error("imagesname:{}",files.get(0).getOriginalFilename());
        imageService.upload(files);
    }

    @PostMapping("/jsontest")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void uploadImageForJson(@RequestBody ImageJsonRequestDto imageJsonRequestDto){
        imageService.upload(imageJsonRequestDto);
    }

}
