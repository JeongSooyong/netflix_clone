package com.soo.netflix_clone.model;

import com.soo.netflix_clone.vo.ActorVo;

public interface IActorDao {

    // 배우 조회
    public ActorVo selectActor(int actorNo);

}
