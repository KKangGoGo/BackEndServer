package com.kkanggogo.facealbum.connection.dto;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class TeskIdListDto {

    private static final List<String> teskKeyList= Collections.synchronizedList(new ArrayList<>());

    public void add(String string){
        teskKeyList.add(string);
    }

    public List<String> getTeskKeyList(){
        List<String> cloneList=new ArrayList<>(teskKeyList);
        Collections.copy(cloneList,teskKeyList);
        return cloneList;
    }

    public void remove(String teskKey) {
        teskKeyList.remove(teskKey);
    }
}
