package com.kkanggogo.facealbum;

import com.kkanggogo.facealbum.album.domein.Image;
import com.kkanggogo.facealbum.album.domein.repository.ImageRepository;
import com.kkanggogo.facealbum.album.service.ImageService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.core.Is.is;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ImageServiceTest {
    ImageService imageService;


    @Test
    @Transactional
    public void imageSaveTest(){
        //given
        //when
        //then
    }

    @Test
    @Transactional
    public void imageGetTest(){
    }

}
