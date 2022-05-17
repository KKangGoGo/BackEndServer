package com.kkanggogo.facealbum.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class DetectPoolingScheduler {

    private List<String> arrayList= Collections.synchronizedList(new ArrayList<>());

    @Scheduled(fixedDelay = 1000)
    public void pooling(){
        for(int i=0;i<arrayList.size();i++){
            log.debug("스케줄러실행 값:{}",arrayList.get(0));
            arrayList.remove(0);
        }
    }

    public void add(String string){
        arrayList.add(string);
    }

}
