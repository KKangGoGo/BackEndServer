package com.kkanggogo.facealbum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

@Component
@Slf4j
public class LocalImageSave implements SaveImage{

    @Override
    public void save(Object file) {
        UUID uuid = UUID.randomUUID();
        File dest=new File("./image/"+uuid+".jpg");
        if(file instanceof MultipartFile){
            try {
                makeFile(dest,((MultipartFile) file).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            makeFile(dest, (byte[]) file);
        }
    }

    private void makeFile(File dest,byte[] bytes) {
        try {
            FileOutputStream fileOutputStream=new FileOutputStream(dest);
            BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(fileOutputStream);
            bufferedOutputStream.write(bytes);
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
