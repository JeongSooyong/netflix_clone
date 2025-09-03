package com.soo.netflix_clone.service;

import java.util.List;

import com.soo.netflix_clone.vo.ActorVo;

public interface IActorService {

    // 영화 출연 배우
    List<ActorVo> selectActorsByMovie(int movieNo);

}
