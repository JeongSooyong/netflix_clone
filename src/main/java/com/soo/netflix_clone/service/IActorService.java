package com.soo.netflix_clone.service;

import java.util.List;

import com.soo.netflix_clone.vo.ActorVo;
import com.soo.netflix_clone.vo.MovieVo;

public interface IActorService {

    // 영화 출연 배우
    public List<ActorVo> selectActorsByMovie(int movieNo);

    // 배우 정보
    public ActorVo selectActor(int actorNo);

    // 배우 등록
    public int insertActor(ActorVo vo);

    // 배우 전체 조회
    public List<ActorVo> selectAllActor();

    // 배우 검색
    public List<ActorVo> searchActor(String keyword);

    // 출연 배우 insert
    public int insertMovieActors(int movieNo, List<Integer> actorNos);

    // 출연 배우 조회
    public List<ActorVo> getActorsByMovie(int movieNo);

    // 배우 출연 영화 조회
    public List<MovieVo> selectMoviesByActorNo(int actorNo);

    // 배우 정보 수정
    public int updateActor(ActorVo vo);

    // 배우 비공개
    public int actorPrivate(int actorNo);
    

}
