package com.kkanggogo.facealbum.album.web;

import com.amazonaws.util.IOUtils;
import com.kkanggogo.facealbum.album.web.dto.ImageMultipartFileRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ImageMultipartFileRequestDtoFactory {

    public static ImageMultipartFileRequestDto makeMultipartFileRequestDto(List<MultipartFile> files) {
        ImageMultipartFileRequestDto imageMultipartFileRequestDto = new ImageMultipartFileRequestDto();
        imageMultipartFileRequestDto.setFileCount(files.size());
        imageMultipartFileRequestDto.setOriginFileNames(getOriginFileNames(files));
        imageMultipartFileRequestDto.setImageByteDates(getImageByteDates(files));
        return imageMultipartFileRequestDto;
    }

    private static List<byte[]> getImageByteDates(List<MultipartFile> files) {
        return files.stream().map(file -> {
            try {
                return IOUtils.toByteArray(file.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    private static List<String> getOriginFileNames(List<MultipartFile> files) {
        return files.stream().map(file ->
                file.getOriginalFilename()).collect(Collectors.toList());
    }
}