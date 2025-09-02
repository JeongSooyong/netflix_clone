package com.soo.netflix_clone.service;

import com.soo.netflix_clone.vo.ActorVo;

public interface IActorService {

    // 배우 조회
    public ActorVo selectActor(int actorNo);

}
