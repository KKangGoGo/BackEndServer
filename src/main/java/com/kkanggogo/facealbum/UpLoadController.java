package com.kkanggogo.facealbum;

import com.kkanggogo.facealbum.model.ImageData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Iterator;
import java.util.List;

@RestController
@Slf4j
public class UpLoadController {

    @Autowired
    @Qualifier("s3ImageSave")
    public SaveImage saveImage;

    @PostMapping("/test")
    @ResponseStatus(code = HttpStatus.OK)
    public void uploadImage(@RequestParam("file") List<MultipartFile> files){
        for(MultipartFile file:files){
            saveImage.save(file);
        }
    }

    @PostMapping("/jsontest")
    @ResponseStatus(code = HttpStatus.OK)
    public void uploadImageForJson(@RequestBody ImageData imageData){
        for(byte[] bytes:imageData.getImages()){
            saveImage.save(bytes);
        }
    }
}
