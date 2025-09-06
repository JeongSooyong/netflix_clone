package com.soo.netflix_clone.service;

import java.util.List;

import com.soo.netflix_clone.vo.ActorVo;

public interface IActorService {

    // 영화 출연 배우
    public List<ActorVo> selectActorsByMovie(int movieNo);

    // 배우 정보
    public ActorVo selectActor(int actorNo);

    // 배우 등록
    public int insertActor(ActorVo vo);

    

}
