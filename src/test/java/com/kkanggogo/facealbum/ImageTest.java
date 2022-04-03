package com.kkanggogo.facealbum;

import com.kkanggogo.facealbum.album.domein.Image;
import com.kkanggogo.facealbum.album.domein.repository.ImageRepository;
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
public class ImageTest {
    @Autowired
    ImageRepository imageRepository;

    @Test
    @Transactional
    public void imageSaveTest(){
        Image image=new Image();
        Long userId=1L;
        image.setImagePath(userId,"aaaa.jpg");
        imageRepository.save(image);
        Optional<Image> image2 =imageRepository.findById(image.getId());
        assertThat(image.getImagePath(),is(image2.get().getImagePath()));
        assertThat(image.getId(),is(image2.get().getId()));
    }

    @Test
    @Transactional
    public void imageGetTest(){
        String path="aaa.jpg";
        Long id=2L;
        Optional<Image> image2 =imageRepository.findById(id);
        assertThat(path,is(image2.get().getImagePath()));
        assertThat(id,is(image2.get().getId()));
    }

}
