package com.github.homework.program.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ViewCount {


    private final Map<Long,Integer> views;


    ViewCount(){
        views=new ConcurrentHashMap<>();
    }


    public Map<Long, Integer> getFieldViews(){
        return views;
    }

    public int getViews(Long id){
        return views.get(id);
    }

    public int increaseView(Long id){
        Integer integer = views.get(id);
        views.put(id,integer+1);
        return integer+1;
    }


    public void setViews(Long id, Integer count){
        views.put(id, count);
    }
}
