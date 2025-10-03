package com.soo.netflix_clone.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.soo.netflix_clone.service.ActorServiceImpl;
import com.soo.netflix_clone.vo.ActorVo;

@RestController
public class ActorRestController {

    // Actor 서비스 자동주입
    @Autowired
    private ActorServiceImpl actorService;

    // 배우 검색
    @GetMapping("/api/actors/search")
    @ResponseBody
    public List<ActorVo> searchActor(@RequestParam(value = "keyword", required = false) String keyword) {
        
        // 검색 키워드가 null 일때
        if (keyword == null || keyword.trim().isEmpty()) {
            return actorService.selectAllActor();
        } 
        return actorService.searchActor(keyword);
    }

}
