package com.soo.netflix_clone.model;

import java.util.List;

import com.soo.netflix_clone.vo.ActorVo;

public interface IActorDao {

    // 영화 출연 배우
    List<ActorVo> selectActorsByMovie(int movieNo);

}
