package com.kkanggogo.facealbum.connection.dto;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class TaskIdListDto {

    private static final List<String> taskKeyList = Collections.synchronizedList(new ArrayList<>());

    public void add(String string) {
        taskKeyList.add(string);
    }

    public List<String> getTaskKeyList() {
        List<String> cloneList = new ArrayList<>(taskKeyList);
        Collections.copy(cloneList, taskKeyList);
        return cloneList;
    }

    public void remove(String taskKey) {
        taskKeyList.remove(taskKey);
    }
}
